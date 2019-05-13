package com.learning.exam.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.learning.exam.dao.jpa.*;
import com.learning.exam.dao.mapper.*;
import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.enums.PaperTypeEnum;
import com.learning.exam.framework.enums.PaperUserStatusEnum;
import com.learning.exam.framework.enums.QlevelEnum;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.framework.enums.converter.PaperTypeEnumConverter;
import com.learning.exam.framework.enums.converter.PaperUserStatusEnumConverter;
import com.learning.exam.framework.enums.converter.QlevelEnumConverter;
import com.learning.exam.framework.enums.converter.QtypeEnumConverter;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.framework.redis.keys.PaperKey;
import com.learning.exam.framework.redis.keys.QuestionKey;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.keys.UserKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.entity.*;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.result.PaperRandomVo;
import com.learning.exam.model.vo.*;
import com.learning.exam.service.QuestionService;
import com.learning.exam.service.TestService;
import com.learning.exam.util.DateTimeUtils;
import com.learning.exam.util.SnowFlake;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author liuzihao
 * @date 2019-02-24  11:42
 */
@Service
@Slf4j
public class TestServiceImpl implements TestService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionOptMapper questionOptMapper;
    @Autowired
    private PaperUserMapper paperUserMapper;
    @Autowired
    private PaperTestMapper paperTestMapper;
    @Autowired
    private PaperResultMapper paperResultMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public String exposeUrl(String userId,String paperId) {
        TbPaperUser tbPaperUser = redisService.get(PaperKey.paperByUserPaperId,paperId+":"+userId,TbPaperUser.class);
        if(tbPaperUser == null){
            tbPaperUser = paperUserMapper.findByUserIdAndPaperId(Integer.parseInt(userId),Integer.parseInt(paperId));
            if(tbPaperUser == null){
                throw new ValidationJsonException(CodeMsg.PAPER_USER_JOIN_ERROR);
            }
        }
        PaperTestVo paperTestVo = redisService.get(PaperKey.paperById,paperId,PaperTestVo.class);
        if(paperTestVo == null){
            throw new ValidationJsonException(CodeMsg.PAPER_TEST_NO_START);
        }
        if((paperTestVo.getStartTime().getTime()-1000)>System.currentTimeMillis()){
            throw new ValidationJsonException(CodeMsg.PAPER_TEST_NO_START);
        }
        //正在参加
        TbPaperTest tbPaperTest = redisService.hget(UserKey.userById,userId,paperId,TbPaperTest.class);
        if(tbPaperTest!=null){
            Date testStartTime = tbPaperTest.getStartTime();
            if ((testStartTime.getTime()+paperTestVo.getPaperMinute()*60000)<=System.currentTimeMillis()){
                String saveKey = redisService.hget(UserKey.userById,userId,"saveKey",String.class);
                submitPaperTest(userId,paperId,saveKey);
                throw new ValidationJsonException(CodeMsg.PAPER_MINUTE_OVER);
            }
        }
        //已结束
        if(paperTestVo.getEndTime().getTime()<=System.currentTimeMillis()){
            Integer status = tbPaperUser.getStatus();
            if(status.equals(PaperUserStatusEnum.WAIT_START.getId())) {
                paperUserMapper.updateStatus(PaperUserStatusEnum.NO_JOIN.getId(), tbPaperUser.getId());
            }else if(status.equals(PaperUserStatusEnum.BE_IN.getId())){
                paperUserMapper.updateStatus(PaperUserStatusEnum.HAD_SUBMIT.getId(), tbPaperUser.getId());
            }
            throw new ValidationJsonException(CodeMsg.PAPER_TEST_HAD_END);
        }
        //已经参加
        PaperUserStatusEnum statusEnum = PaperUserStatusEnumConverter.converter(tbPaperUser.getStatus());
        if(statusEnum == PaperUserStatusEnum.HAD_SUBMIT){
            throw new ValidationJsonException(CodeMsg.PAPET_TEST_HAD_JOIN);
        }
        String uuid = redisService.hget(UserKey.userById,userId,SessionCacheName.UUID,String.class);
        if(StringUtils.isEmpty(uuid)){
            uuid = SnowFlake.getInstance().nextId();
            redisService.hset(UserKey.userById,userId,SessionCacheName.UUID+":"+paperId,uuid);
        }
        return  "../../test/"+paperId+"/"+uuid+".html";
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void writeTestInfo(String userId,String paperId) {
        TbPaperTest tbPaperTest = redisService.hget(UserKey.userById,userId,paperId,TbPaperTest.class);
        PaperTestVo paperTestVo = redisService.get(PaperKey.paperById,paperId,PaperTestVo.class);
        //正在参加
        if(tbPaperTest!=null){
            if ((tbPaperTest.getStartTime().getTime()+paperTestVo.getPaperMinute()*60000)<=System.currentTimeMillis()){
                String saveKey = redisService.hget(UserKey.userById,userId,"saveKey",String.class);
                submitPaperTest(userId,paperId,saveKey);
                throw new ValidationJsonException(CodeMsg.PAPER_MINUTE_OVER);
            }
        }
        if(tbPaperTest!=null&&!StringUtils.isEmpty(tbPaperTest.getQuestionIds())){
            return;
        }
        TbPaperUser tbPaperUser = redisService.get(PaperKey.paperByUserPaperId,paperId+":"+userId,TbPaperUser.class);
        int expire = (int)(redisService.getExpire(PaperKey.paperByUserPaperId,paperId+":"+userId)/1000);
        //等待考试状态
        if(tbPaperUser.getStatus().equals(PaperUserStatusEnum.WAIT_START.getId())){
            paperUserMapper.updateStatus(PaperUserStatusEnum.BE_IN.getId(),tbPaperUser.getId());
            tbPaperUser.setStatus(PaperUserStatusEnum.BE_IN.getId());
            redisService.setex(PaperKey.paperByUserPaperId,paperId+":"+userId,tbPaperUser,expire);
        }
        PaperTypeEnum typeEnum = PaperTypeEnumConverter.converter(paperTestVo.getPaperType());
        String questionIdsStr = null;
        if(typeEnum == PaperTypeEnum.NORMAL_TYPE){
            //获取章节
            questionIdsStr = paperTestVo.getQuestionIds();
        }else if(typeEnum == PaperTypeEnum.RANDOM_TYPE){
            //随机组卷
            List<SectionTestVo> sections = paperTestVo.getSections();
            questionIdsStr = randomPaperGroup(paperId,sections);
        }
        if(!StringUtils.isEmpty(questionIdsStr)){
            tbPaperTest = new TbPaperTest();
            tbPaperTest.setPaperUserId(tbPaperUser.getId());
            tbPaperTest.setQuestionIds(questionIdsStr);
            tbPaperTest.setStartTime(new Date(System.currentTimeMillis()));
            paperTestMapper.insertPaperTest(tbPaperTest);
            //写入缓存
            redisService.hset(UserKey.userById,userId,paperId,tbPaperTest);
            redisService.expire(UserKey.userById,userId,expire);
        }
    }
    /**
     *按比例权重生成试卷题目id
    */
    private String randomPaperGroup(String paperId, List<SectionTestVo> sections){
        List<Integer> questionIds = new ArrayList<>();
        for (SectionTestVo section:sections){
            Integer questionNum = section.getQuestionNum();
            QtypeEnum qtypeEnum = QtypeEnumConverter.converter(section.getSectionType());
            Integer questionScore = section.getQuestionScore();
            List<Integer> levelScale = Arrays.stream(section.getLevelScale().split(",")).map(Integer::parseInt).collect(Collectors.toList());
            Integer levelScaleSum = levelScale.stream().mapToInt(Integer::intValue).sum();
            List<Integer> qdbIds = Arrays.stream(section.getQdbIds().split(",")).map(Integer::parseInt).collect(Collectors.toList());
            List<QuestionContentVo> questions = questionMapper.findQuestionIdsByQdbIdAndQtype(qtypeEnum.getId(),qdbIds);
            //作为随机的容器
            PaperRandomVo randomVo = new PaperRandomVo(questionNum);
            for (QuestionContentVo questionContentVo:questions){
                QlevelEnum qlevelEnum = QlevelEnumConverter.converter(questionContentVo.getQLevel());
                randomVo.addOne(qlevelEnum,questionContentVo);
            }
            //产生数量
            Integer[] scale = randomVo.produceScale(levelScaleSum,levelScale);
            //产生随机题目
            List<QuestionContentVo> questionContentVos = randomVo.random(scale);
            QuestionTestVo questionTestVo = null;
            for (QuestionContentVo question:questionContentVos){
                Integer questionId = question.getId();
                questionIds.add(questionId);
                questionTestVo = redisService.get(QuestionKey.byPaperId,paperId+":"+questionId,QuestionTestVo.class);
                if(questionTestVo==null){
                    questionTestVo = new QuestionTestVo();
                    questionTestVo.setId(question.getId());
                    questionTestVo.setQuestionContent(question.getQuestionContent());
                    questionTestVo.setQuestionType(question.getQType());
                    questionTestVo.setQuestionTypeName(qtypeEnum.getType());
                    questionTestVo.setOptKey(question.getOptKey());
                    questionTestVo.setQuestionScore(questionScore);
                    if(qtypeEnum == QtypeEnum.EXCLUSIVE_CHOICE||
                            qtypeEnum == QtypeEnum.MULTIPLE_CHOICE){
                        List<QuestionOptVo> opts = questionOptMapper.findQuestionOptVosByQId(questionId);
                        questionTestVo.setOpts(opts);
                    }else if(qtypeEnum == QtypeEnum.FILL_BLANKS){
                        String[] optKeys = question.getOptKey().split(",");
                         questionTestVo.setBlankNum(optKeys.length);
                    }
                    redisService.set(QuestionKey.byPaperId,paperId+":"+questionId,questionTestVo);
                }
            }
        }
        return questionIds.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
    @Override
    public StudentPaperVo getPaperTestQuestion(String userId,String paperId) {
        TbPaperTest tbPaperTest = redisService.hget(UserKey.userById,userId,paperId,TbPaperTest.class);
        if(tbPaperTest == null){
            throw new ValidationJsonException(CodeMsg.PAPER_TEST_GET_ERROR);
        }
        String questionIdsStr = tbPaperTest.getQuestionIds();
        if(StringUtils.isEmpty(questionIdsStr)){
            throw new ValidationJsonException(CodeMsg.PAPER_TEST_GET_ERROR);
        }
        String[] questionIds = questionIdsStr.split(",");
        List<QuestionTestVo> questions = new ArrayList<>(questionIds.length);
        for (String questionId:questionIds){
            QuestionTestVo questionTestVo = redisService.get(QuestionKey.byPaperId,paperId+":"+questionId,QuestionTestVo.class);
            //移除答案
            questionTestVo.setOptKey(null);
            questions.add(questionTestVo);
        }
        StudentPaperVo studentPaperVo = new StudentPaperVo();
        studentPaperVo.setQuestions(questions);
        PaperTestVo paperTestVo = redisService.get(PaperKey.paperById,paperId,PaperTestVo.class);
        Date saveTime = redisService.hget(UserKey.userById,userId,"saveTime",Date.class);
        Date testStartTime = tbPaperTest.getStartTime();
        Date paperEndTime = paperTestVo.getEndTime();
        Integer paperMinute = paperTestVo.getPaperMinute();
        //倒计时
        if(saveTime!=null){
            studentPaperVo.setEndTime(saveTime);
        }else{
            if(((paperMinute-1)*60000+testStartTime.getTime())>paperEndTime.getTime()){
                studentPaperVo.setEndTime(paperEndTime);
            }else{
                studentPaperVo.setEndTime(new Date(paperMinute*60000+System.currentTimeMillis()));
            }
            redisService.hset(UserKey.userById,userId,"saveTime",studentPaperVo.getEndTime());
        }
        String saveKey = redisService.hget(UserKey.userById,userId,"saveKey",String.class);
        if(!StringUtils.isEmpty(saveKey)){
            studentPaperVo.setSaveKey(saveKey);
        }
        return studentPaperVo;
    }

    @Override
    public void savePaperTest(String userId, String paperId, String saveKey) {
        if(!StringUtils.isEmpty(saveKey)) {
            redisService.hset(UserKey.userById,userId,"saveKey",saveKey);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitPaperTest(String userId, String paperId, String optKey) {
        int status = paperUserMapper.findStatusByUserIdAndPaperIdForUpdate(Integer.parseInt(userId),Integer.parseInt(paperId));
        PaperUserStatusEnum statusEnum = PaperUserStatusEnumConverter.converter(status);
        if(statusEnum == PaperUserStatusEnum.HAD_SUBMIT
                ||statusEnum == PaperUserStatusEnum.NO_JOIN){
            throw new ValidationJsonException(CodeMsg.PAPER_HAD_SUBMIT);
        }
        TbPaperTest tbPaperTest = redisService.hget(UserKey.userById,userId,paperId,TbPaperTest.class);
        TbPaperUser tbPaperUser = redisService.get(PaperKey.paperByUserPaperId,paperId+":"+userId,TbPaperUser.class);
        Date submitTime = new Date();
        int resultMinuteSecond = (int)(submitTime.getTime()-tbPaperTest.getStartTime().getTime())/1000;
        int resultMinute = resultMinuteSecond/60+resultMinuteSecond%60>30?1:0;
        String[] questionIds = tbPaperTest.getQuestionIds().split(",");
        List<QuestionTestVo> questions = new ArrayList<>(questionIds.length);
        for (String questionId:questionIds){
            QuestionTestVo questionTestVo = redisService.get(QuestionKey.byPaperId,paperId+":"+questionId,QuestionTestVo.class);
            questions.add(questionTestVo);
        }
        optKey = optKey.replaceAll("null","[\"\"]");
        int resultScore  = getUserTestScore(optKey,questions);
        TbPaperResult tbPaperResult = new TbPaperResult();
        tbPaperResult.setPaperTestId(tbPaperTest.getId());
        tbPaperResult.setSubmitTime(submitTime);
        tbPaperResult.setResultKeys(StringUtils.isEmpty(optKey)?"":optKey);
        tbPaperResult.setResultMinute(resultMinute);
        tbPaperResult.setResultScore(resultScore);
        tbPaperResult.setQuestionScore(questions.stream().map(QuestionTestVo::getQuestionScore).map(String::valueOf).collect(Collectors.joining(",")));
        paperUserMapper.updateStatus(PaperUserStatusEnum.HAD_SUBMIT.getId(),tbPaperUser.getId());
        paperResultMapper.insertPaperResult(tbPaperResult);
        redisService.delete(PaperKey.paperByUserPaperId,paperId+":"+userId);
        redisService.delete(UserKey.userById,userId);
    }

    private Integer getUserTestScore(String optKey, List<QuestionTestVo> questions){
        if(StringUtils.isEmpty(optKey)){
            return 0;
        }
        List<String> userOptKey = JSONArray.parseArray(optKey,String.class);
        Integer userTestScore = 0;
        for (int i = 0; i < questions.size(); i++) {
            QuestionTestVo question = questions.get(i);
            List<String> key = Arrays.stream(question.getOptKey().split(",")).collect(Collectors.toList());
            List<String> userKey = JSONArray.parseArray(userOptKey.get(i),String.class);
            boolean t = true;
            if(key.size()!=userKey.size()) {
                t = false;
            }else{
                for (String s:key){
                    if(!userKey.contains(s)){
                        t=false;break;
                    }
                }
            }
            if(t){
                userTestScore+=question.getQuestionScore();
            }
        }
        return userTestScore;
    }
}
