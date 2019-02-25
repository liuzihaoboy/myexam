package com.learning.exam.service.impl;

import com.learning.exam.dao.jpa.*;
import com.learning.exam.dao.mapper.PaperMapper;
import com.learning.exam.dao.mapper.QuestionMapper;
import com.learning.exam.framework.enums.PaperTypeEnum;
import com.learning.exam.framework.enums.PaperUserStatusEnum;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.framework.enums.converter.PaperTypeEnumConverter;
import com.learning.exam.framework.enums.converter.QtypeEnumConverter;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.framework.redis.keys.PaperKey;
import com.learning.exam.framework.redis.keys.QuestionKey;
import com.learning.exam.framework.redis.keys.UserKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.dto.PaperDto;
import com.learning.exam.model.dto.PaperSectionDto;
import com.learning.exam.model.entity.*;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.vo.*;
import com.learning.exam.service.PaperService;
import com.learning.exam.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liuzihao
 * @date 2019-02-21  13:24
 */
@Service
@Slf4j
public class PaperServiceImpl implements PaperService {
    @Autowired
    private PaperJpa paperJpa;
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private PaperSectionJpa paperSectionJpa;
    @Autowired
    private PaperQuestionJpa paperQuestionJpa;
    @Autowired
    private CourseJpa courseJpa;
    @Autowired
    private QuestionJpa questionJpa;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionDbJpa questionDbJpa;
    @Autowired
    private QuestionOptJpa questionOptJpa;
    @Autowired
    private UserJpa userJpa;
    @Autowired
    private PaperUserJpa paperUserJpa;
    @Autowired
    private PaperResultJpa paperResultJpa;
    @Autowired
    private RedisService redisService;

    @Override
    public TbPaperUser getPaperUser(Integer userId, Integer paperId) {
        return paperUserJpa.findByUserIdAndPaperId(userId,paperId);
    }

    @Override
    public List<PaperResultVo> getPaperResultByUserIdSubmit(Integer userId) {
        List<TbPaperUser> tbPaperUsers = paperUserJpa.findByUserIdSubmit(userId);
        List<PaperResultVo> paperResultVos = new ArrayList<>();
        for (TbPaperUser tbPaperUser :tbPaperUsers){
            TbPaperResult tbPaperResult = redisService.get();
            if(tbPaperResult==null){
                tbPaperResult = paperResultJpa.findByPaperUserId(tbPaperUser.getId());
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PaperTestVo> getPaperTestByUserIdNoSubmit(Integer userId) {
        List<TbPaperUser> tbPaperUsers = paperUserJpa.findByUserIdNoSubmit(userId);
        List<PaperTestVo> paperUserVos = new ArrayList<>();
        for (TbPaperUser tbPaperUser:tbPaperUsers) {
            PaperTestVo paperTestVo = redisService.get(PaperKey.paperById,Integer.toString(tbPaperUser.getPaperId()),PaperTestVo.class);
            if(paperTestVo == null){
                paperTestVo = paperTestVo(paperJpa.findByIdAndConfigStatus(tbPaperUser.getPaperId(),0));
            }
            if(paperTestVo!=null){
                if(paperTestVo.getEndTime().getTime()<=System.currentTimeMillis()){
                    Integer status = tbPaperUser.getStatus();
                    if(status.equals(PaperUserStatusEnum.WAIT_START.getId())) {
                        paperUserJpa.updateStatus(PaperUserStatusEnum.NO_JOIN.getId(), tbPaperUser.getId());
                    }else if(status.equals(PaperUserStatusEnum.BE_IN.getId())){
                        paperUserJpa.updateStatus(PaperUserStatusEnum.HAD_SUBMIT.getId(), tbPaperUser.getId());
                    }
                    continue;
                }
                int expire = (int)((paperTestVo.getEndTime().getTime()-System.currentTimeMillis())/1000);
                //开始时间在一天之内，则存入缓存
                if(paperTestVo.getStartTime().getTime()<=(System.currentTimeMillis()+86400000)){
                    redisService.setex(UserKey.userByPaperUserId,tbPaperUser.getUserId()+":"+tbPaperUser.getPaperId(),tbPaperUser,expire);
                    redisService.setex(PaperKey.paperById,Integer.toString(paperTestVo.getPaperId()),paperTestVo,expire);
                }
                paperTestVo.setSections(null);
                paperUserVos.add(paperTestVo);
            }
        }
        return paperUserVos;
    }
    private PaperTestVo paperTestVo(TbPaper tbPaper){
        if(tbPaper == null){
            return null;
        }
        PaperTestVo paperTestVo = new PaperTestVo();
        paperTestVo.setPaperId(tbPaper.getId());
        paperTestVo.setStartTime(tbPaper.getStartTime());
        paperTestVo.setEndTime(tbPaper.getEndTime());
        paperTestVo.setPaperName(tbPaper.getPaperName());
        paperTestVo.setPaperMinute(tbPaper.getPaperMinute());
        paperTestVo.setPaperType(tbPaper.getPaperType());
        paperTestVo.setPaperTypeName(PaperTypeEnumConverter.converter(tbPaper.getPaperType()).getType());
        paperTestVo.setPassScore(tbPaper.getPassScore());
        paperTestVo.setTotalScore(getPaperTotalScore(tbPaper.getId()));
        paperTestVo.setSections(sectionTestVos(tbPaper.getId()));
        return paperTestVo;
    }
    private List<SectionTestVo> sectionTestVos(Integer paperId){
        List<SectionTestVo> sectionTestVos = new ArrayList<>();
        SectionTestVo sectionTestVo = null;
        List<TbPaperSection> tbPaperSections = paperSectionJpa.findByPaperId(paperId);
        for (TbPaperSection tbPaperSection : tbPaperSections){
            sectionTestVo = new SectionTestVo();
            sectionTestVo.setQuestionNum(tbPaperSection.getQuestionNum());
            sectionTestVo.setQuestionScore(tbPaperSection.getQuestionScore());
            sectionTestVo.setSectionType(tbPaperSection.getSectionType());
            sectionTestVo.setSectionTypeName(QtypeEnumConverter.converter(tbPaperSection.getSectionType()).getType());
            String qdbIds = tbPaperSection.getQdbIds();
            String levelScale = tbPaperSection.getLevelScale();
            if("0".equals(qdbIds)&&"0,0,0,0,0".equals(levelScale)){
                //获取普通试卷题目
                String questionIdsStr = paperQuestionJpa.findQuestionIdsBySectionId(tbPaperSection.getId());
                if(!StringUtils.isEmpty(questionIdsStr)){
                    String[] questionIds = questionIdsStr.split(",");
                    List<QuestionTestVo> questions = new ArrayList<>(questionIds.length);
                    for (String id:questionIds){
                        QuestionTestVo questionTestVo = questionTestVo(Integer.parseInt(id));
                        if(questionTestVo!=null){
                            questions.add(questionTestVo);
                        }
                    }
                    sectionTestVo.setQuestions(questions);
                    sectionTestVos.add(sectionTestVo);
                }
            }else {
                sectionTestVo.setQdbIds(qdbIds);
                sectionTestVo.setLevelScale(levelScale);
                sectionTestVos.add(sectionTestVo);
            }
        }
        return sectionTestVos;
    }
    private QuestionTestVo questionTestVo(Integer questionId){
        QuestionTestVo questionTestVo = redisService.get(QuestionKey.questionById,Integer.toString(questionId),QuestionTestVo.class);
        if(questionTestVo!=null){
            return questionTestVo;
        }
        QuestionContentVo question = questionMapper.findQuestionContentById(questionId);
        if(question == null){
            return null;
        }
        questionTestVo = new QuestionTestVo();
        questionTestVo.setId(question.getId());
        questionTestVo.setQuestionContent(question.getQuestionContent());
        questionTestVo.setQuestionType(question.getQType());
        questionTestVo.setQuestionTypeName(QtypeEnumConverter.converter(question.getQType()).getType());
        List<TbQuestionOpt> opts = questionOptJpa.findByQId(questionId);
        questionTestVo.setOpts(opts);
        redisService.set(QuestionKey.questionById,Integer.toString(questionId),questionTestVo);
        return questionTestVo;
    }
    @Override
    public List<Integer> getPaperSectionIdByPaperId(Integer paperId) {
        return paperSectionJpa.findPaperSectionIdByPaperId(paperId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePaperSection(Integer paperId, String sectionId) {
        if(sectionId.contains(",")){
            String[] sectionIds = sectionId.split(",");
            for (String id:sectionIds){
                paperSectionJpa.deletePaperSectionByIdAndPaperId(paperId,Integer.parseInt(id));
                paperQuestionJpa.deletePaperQuestionBySectionId(Integer.parseInt(id));
            }
        }else {
            paperSectionJpa.deletePaperSectionByIdAndPaperId(paperId,Integer.parseInt(sectionId));
            paperQuestionJpa.deletePaperQuestionBySectionId(Integer.parseInt(sectionId));
        }
        int paperSectionNum = paperSectionJpa.findPaperSectionNumByPaerId(paperId);
        if(paperSectionNum<=0){
            paperJpa.updatePaperConfigStatus(1,paperId);
        }
    }

    @Override
    public void submitPaperSection(PaperSectionDto paperSectionDto) {
        Integer paperType = paperJpa.findPaperTypeById(paperSectionDto.getPaperId());
        PaperTypeEnum paperTypeEnum = PaperTypeEnumConverter.converter(paperType);
        TbPaperSection tbPaperSection = new TbPaperSection();
        tbPaperSection.setQuestionScore(paperSectionDto.getQuestionScore());
        if(paperTypeEnum == PaperTypeEnum.RANDOM_TYPE){
            tbPaperSection.setQuestionNum(paperSectionDto.getQuestionNum());
            tbPaperSection.setQdbIds(paperSectionDto.getQdbIds());
            List<String> levelScaleStrs = Arrays.stream(paperSectionDto.getLevelScale()).map(String::valueOf).collect(Collectors.toList());
            tbPaperSection.setLevelScale(levelScaleStrs.stream().collect(Collectors.joining(",")));
        }
        if(paperSectionDto.getId()==null||paperSectionDto.getId()==0){
            tbPaperSection.setPaperId(paperSectionDto.getPaperId());
            tbPaperSection.setSectionType(paperSectionDto.getSectionType());
            if(paperTypeEnum == PaperTypeEnum.NORMAL_TYPE){
                tbPaperSection.setQuestionNum(0);
                tbPaperSection.setQdbIds("0");
                tbPaperSection.setLevelScale("0,0,0,0,0");
            }
            paperSectionJpa.insertPaperSection(tbPaperSection);
            paperJpa.updatePaperConfigStatus(0,paperSectionDto.getPaperId());
        }else {
            tbPaperSection.setId(paperSectionDto.getId());
            if(paperTypeEnum == PaperTypeEnum.NORMAL_TYPE){
                paperSectionJpa.updateNormalPaperSectionById(tbPaperSection);
            }else if(paperTypeEnum == PaperTypeEnum.RANDOM_TYPE){
                paperSectionJpa.updateRandomPaperSectionById(tbPaperSection);
            }
        }

    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addQuestionIdBySectionId(Integer sectionId, String questionIdsStr) {
        Set<Integer> set = Arrays.stream(questionIdsStr.split(",")).map(Integer::parseInt)
                .collect(Collectors.toSet());
        String oldQuestionIdsStr = paperQuestionJpa.findQuestionIdsBySectionId(sectionId);
        if(!StringUtils.isEmpty(oldQuestionIdsStr)){
            List<Integer> oldQuestionIds = Arrays.stream(oldQuestionIdsStr.split(","))
                    .map(Integer::parseInt).collect(Collectors.toList());
            set.addAll(oldQuestionIds);
        }
        Iterator<Integer> iterator = set.iterator();
        QtypeEnum qtypeEnum = QtypeEnumConverter.converter(paperSectionJpa.findSectionTypeById(sectionId));
        for (;iterator.hasNext();){
            Integer qid = iterator.next();
            Integer qtype = questionJpa.findQuestionTypeById(qid);
            if(!qtypeEnum.getId().equals(qtype)){
                iterator.remove();
            }
        }
        String newQuestionIdsStr = set.stream().map(String::valueOf).collect(Collectors.joining(","));
        if(oldQuestionIdsStr!=null){
            paperQuestionJpa.updateQuestionsBySectionId(sectionId,newQuestionIdsStr);
        }else {
            paperQuestionJpa.insertQuestionsBySectionId(sectionId,newQuestionIdsStr);
        }
        paperSectionJpa.updateQuestionNumById(sectionId,set.size());
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void deleteSectionQuestionBySectionId(Integer sectionId, String questionIdsStr, Integer questionNum) {
        paperSectionJpa.updateQuestionNumById(sectionId,questionNum);
        paperQuestionJpa.updateQuestionsBySectionId(sectionId,questionIdsStr);
    }

    @Override
    public PaperSectionVo getPaperSectionById(Integer sectionId, PaperTypeEnum paperTypeEnum) {
        TbPaperSection tbPaperSection = paperSectionJpa.findOne(sectionId);
        return paperSectionVo(paperTypeEnum,tbPaperSection);
    }

    @Override
    public List<PaperSectionVo> getPaperSectionsByPaperId(Integer paperId,PaperTypeEnum paperTypeEnum) {
        return paperSectionVos(paperTypeEnum,paperId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitPaper(PaperDto paperDto, Integer cUserId,PaperTypeEnum paperTypeEnum) {
        TbPaper tbPaper = new TbPaper();
        tbPaper.setPaperName(paperDto.getPaperName());
        tbPaper.setCourseId(paperDto.getCourseId());
        tbPaper.setPaperMinute(paperDto.getPaperMinute());
        tbPaper.setShowKey(paperDto.getShowKey());
        tbPaper.setPaperType(paperTypeEnum.getId());
        tbPaper.setToUser(paperDto.getToUserIds());
        tbPaper.setPassScore(paperDto.getPassScore());
        tbPaper.setConfigStatus(paperDto.getConfigStatus());
        tbPaper.setStartTime(paperDto.getStartTime());
        tbPaper.setEndTime(paperDto.getEndTime());
        tbPaper.setCUid(cUserId);
        if(paperDto.getId()!=null&&paperDto.getId()!=0){
            tbPaper.setId(paperDto.getId());
            paperUserJpa.deletePaperUserByPaperId(paperDto.getId());
        }
        TbPaper paper = paperJpa.save(tbPaper);
        String[] userIds = paperDto.getToUserIds().split(",");
        if(userIds.length!=0){
            for (String userIdStr:userIds){
                paperUserJpa.insertPaperUser(Integer.parseInt(userIdStr),paper.getId(),PaperUserStatusEnum.WAIT_START.getId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePaper(String id) {
        if(id.contains(",")){
            String[] ids = id.split(",");
            for (String paperIdStr:ids){
                Integer paperId = Integer.parseInt(paperIdStr);
                validStartTime(paperId);
                List<Integer> sectionIds = getPaperSectionIdByPaperId(paperId);
                for (Integer sectionId:sectionIds){
                    paperQuestionJpa.deletePaperQuestionBySectionId(sectionId);
                    paperSectionJpa.deletePaperSectionByIdAndPaperId(paperId,sectionId);
                }
                paperUserJpa.deletePaperUserByPaperId(paperId);
                paperJpa.deletePaperById(paperId);
            }
        }else {
            Integer paperId = Integer.parseInt(id);
            validStartTime(paperId);
            List<Integer> sectionIds = getPaperSectionIdByPaperId(paperId);
            for (Integer sectionId:sectionIds){
                paperQuestionJpa.deletePaperQuestionBySectionId(sectionId);
                paperSectionJpa.deletePaperSectionByIdAndPaperId(paperId,sectionId);
            }
            paperUserJpa.deletePaperUserByPaperId(paperId);
            paperJpa.deletePaperById(paperId);
        }
    }
    private void deletePaper(Integer id){

    }
    private void validStartTime(Integer id){
        Date startTime = getPaperStartTimeById(id);
        if(startTime!=null){
            if(startTime.getTime()<=System.currentTimeMillis()){
                throw new ValidationJsonException(CodeMsg.PAPER_STARTTIME_OVER);
            }
            //开始前2小时
            if(startTime.getTime()<=(System.currentTimeMillis()+3600000)){
                throw new ValidationJsonException(CodeMsg.PAPER_STARTTIME_LIMIT);
            }
        }
    }

    @Override
    public Date getPaperStartTimeById(Integer paperId) {
        return paperJpa.findPaperStartTimeById(paperId);
    }

    /**
     * 加分钟
     */
    private Date endTime(Date startTime,Integer minute){
        return new Date(startTime.getTime()+minute*60000);
    }
    @Override
    public PaperVo getPaper(Integer paperId) {
        TbPaper tbPaper = paperJpa.findOne(paperId);
        return paperVo(tbPaper,false);
    }

    @Override
    public Integer getPaperIdById(Integer paperId) {
        return paperJpa.findPaperIdById(paperId);
    }

    @Override
    public Integer getPaperTotalScore(Integer paperId) {
        List<Integer> questionNums = paperSectionJpa.findQuestionNumsByPaperId(paperId);
        List<Integer> questionScores = paperSectionJpa.findQuestionScoresByPaperId(paperId);
        Integer totalScore = 0;
        for (int i = 0; i < questionNums.size(); i++) {
            totalScore+=questionNums.get(i)*questionScores.get(i);
        }
        return totalScore;
    }

    @Override
    public List<PaperVo> getPapers() {
        List<TbPaper> tbPapers = paperJpa.findAll();
        List<PaperVo> paperVos = new ArrayList<>();
        PaperVo paperVo;
        for (TbPaper tbPaper:tbPapers){
            paperVo = paperVo(tbPaper,false);
            if(paperVo != null){
                paperVos.add(paperVo);
            }
        }
        return paperVos;
    }

    @Override
    public List<PaperVo> getPapersByCondition(String nameKey, String userNameKey, String courseKey, String typeKey, String configkey) {
        List<TbPaper> tbPapers = paperMapper.findPapersByCondition("%"+nameKey+"%",userNameKey+"%",courseKey,typeKey,configkey);
        List<PaperVo> paperVos = new ArrayList<>();
        PaperVo paperVo;
        for (TbPaper tbPaper:tbPapers){
            paperVo = paperVo(tbPaper,false);
            if(paperVo != null){
                paperVos.add(paperVo);
            }
        }
        return paperVos;
    }

    private PaperVo paperVo(TbPaper tbPaper,boolean paperSectionFlag){
        if(tbPaper == null){
            return null;
        }
        PaperVo paperVo = new PaperVo();
        paperVo.setId(tbPaper.getId());
        paperVo.setPaperName(tbPaper.getPaperName());
        paperVo.setCourseId(tbPaper.getCourseId());
        TbCourse tbCourse = courseJpa.findById(tbPaper.getCourseId());
        if(tbCourse != null){
            paperVo.setCourseId(tbCourse.getId());
            paperVo.setCourseName(tbCourse.getCourseName());
        }
        TbUser tbUser = userJpa.findUserById(tbPaper.getCUid());
        if(tbUser != null){
            paperVo.setCUserName(tbUser.getName());
        }
        paperVo.setCDate(tbPaper.getCDate());
        paperVo.setStartTime(DateTimeUtils.getStringInstance().dateDefaultFormat(tbPaper.getStartTime()));
        paperVo.setEndTime(DateTimeUtils.getStringInstance().dateDefaultFormat(tbPaper.getEndTime()));
        paperVo.setPaperMinute(tbPaper.getPaperMinute());
        paperVo.setShowKey(tbPaper.getShowKey());
        PaperTypeEnum paperTypeEnum = PaperTypeEnumConverter.converter(tbPaper.getPaperType());
        paperVo.setPaperType(paperTypeEnum);
        paperVo.setPassScore(tbPaper.getPassScore());
        paperVo.setConfigStatus(tbPaper.getConfigStatus());
        paperVo.setToUser(tbPaper.getToUser());
        List<Integer> toUserIds = Arrays.stream(tbPaper.getToUser().split(",")).map(Integer::parseInt).collect(Collectors.toList());
        paperVo.setToUserIds(toUserIds);
        paperVo.setUserNum(toUserIds!=null?toUserIds.size():0);
        //是否需要显示更多详情信息
        if(paperSectionFlag){
            List<PaperSectionVo> sectionVos = paperSectionVos(paperTypeEnum,tbPaper.getId());
            paperVo.setSectionVos(sectionVos);
        }
        return paperVo;
    }
    private List<PaperSectionVo> paperSectionVos(PaperTypeEnum paperTypeEnum,Integer paperId){
        List<TbPaperSection> tbPaperSections = paperSectionJpa.findByPaperId(paperId);
        List<PaperSectionVo> sectionVos = new ArrayList<>();
        PaperSectionVo sectionVo;
        Integer paperScore=0;
        for (TbPaperSection tbPaperSection:tbPaperSections){
            sectionVo = paperSectionVo(paperTypeEnum,tbPaperSection);
            if(sectionVo != null){
                paperScore+=sectionVo.getTotalScore();
                sectionVos.add(sectionVo);
            }
        }
        for (PaperSectionVo paperSectionVo:sectionVos){
            paperSectionVo.setPaperScore(paperScore);
        }
        return sectionVos;
    }
    private PaperSectionVo paperSectionVo(PaperTypeEnum paperType,TbPaperSection tbPaperSection){
        if(tbPaperSection == null){
            return null;
        }
        PaperSectionVo paperSectionVo = new PaperSectionVo();
        paperSectionVo.setId(tbPaperSection.getId());
        Integer questionNum = tbPaperSection.getQuestionNum();
        paperSectionVo.setQuestionNum(questionNum);
        paperSectionVo.setQuestionScore(tbPaperSection.getQuestionScore());
        if(questionNum == null || questionNum == 0){
            paperSectionVo.setTotalScore(0);
        }else {
            paperSectionVo.setTotalScore(questionNum*tbPaperSection.getQuestionScore());
        }
        QtypeEnum qtypeEnum = QtypeEnumConverter.converter(tbPaperSection.getSectionType());
        paperSectionVo.setSectionTypeId(qtypeEnum.getId());
        paperSectionVo.setSectionTypeName(qtypeEnum.getType());
        paperSectionVo.setLevelScale(tbPaperSection.getLevelScale());
        paperSectionVo.setPaperId(tbPaperSection.getPaperId());
        paperSectionVo.setPaperType(paperType.getId());
        paperSectionVo.setLevelScales(Arrays.stream(tbPaperSection.getLevelScale().split(",")).map(Integer::parseInt).collect(Collectors.toList()));
        if(paperType == PaperTypeEnum.RANDOM_TYPE){
            List<Integer> qdbIds = Arrays.stream(tbPaperSection.getQdbIds().split(",")).map(Integer::parseInt).collect(Collectors.toList());
            List<QuestionDbNameVo> qdbs = new ArrayList<>();
            QuestionDbNameVo questionDbNameVo;
            for (Integer qdbId:qdbIds){
                String qdbName = questionDbJpa.findQdbNameById(qdbId);
                if(qdbName != null){
                    questionDbNameVo = new QuestionDbNameVo();
                    questionDbNameVo.setId(qdbId);
                    questionDbNameVo.setQdbName(qdbName);
                    qdbs.add(questionDbNameVo);
                }
            }
            paperSectionVo.setQdbs(qdbs);
        }
        return paperSectionVo;
    }
}
