package com.learning.exam.service.impl;

import com.learning.exam.dao.jpa.*;
import com.learning.exam.dao.mapper.QuestionDbMapper;
import com.learning.exam.dao.mapper.QuestionMapper;
import com.learning.exam.dao.mapper.QuestionOptMapper;
import com.learning.exam.framework.enums.QlevelEnum;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.framework.enums.converter.QlevelEnumConverter;
import com.learning.exam.framework.enums.converter.QtypeEnumConverter;
import com.learning.exam.framework.enums.converter.RoleEnumConverter;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.model.dto.QuestionDto;
import com.learning.exam.model.entity.*;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.vo.*;
import com.learning.exam.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;
import static org.codehaus.groovy.runtime.DefaultGroovyMethods.tr;

/**
 * @author liuzihao
 * @date 2019-02-11  13:58
 */
@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private CourseJpa courseJpa;
    @Autowired
    private QuestionDbMapper questionDbMapper;
    @Autowired
    private QuestionDbJpa questionDbJpa;
    @Autowired
    private QuestionJpa questionJpa;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionOptJpa questionOptJpa;
    @Autowired
    private QuestionOptMapper questionOptMapper;
    @Autowired
    private UserJpa userJpa;
    @Autowired
    private PaperQuestionJpa paperQuestionJpa;

    @Override
    public QuestionResultVo getQuestionResultById(Integer id) {
        QuestionResultVo questionResultVo = questionMapper.findQuestionResultById(id);
        QtypeEnum qtypeEnum = QtypeEnumConverter.converter(questionResultVo.getQuestionType());
        questionResultVo.setQuestionTypeName(qtypeEnum.getType());
        if(qtypeEnum == QtypeEnum.EXCLUSIVE_CHOICE||
                qtypeEnum == QtypeEnum.MULTIPLE_CHOICE){
            List<QuestionOptVo> opts = questionOptMapper.findQuestionOptVosByQId(id);
            questionResultVo.setOpts(opts);
        }
        return questionResultVo;
    }

    @Override
    public List<Integer> getQuestionIdsBySectionId(Integer sectionId) {
        String questionIdsStr = paperQuestionJpa.findQuestionIdsBySectionId(sectionId);
        if(StringUtils.isEmpty(questionIdsStr)) {
            return null;
        }
        return Arrays.stream(questionIdsStr.split(","))
                .map(Integer::parseInt).collect(Collectors.toList());
    }

    @Override
    public List<QuestionContentVo> getQuestionContentsBySectionId(Integer sectionId) {
        //普通试卷需要获取试题
        List<Integer> questionIds = getQuestionIdsBySectionId(sectionId);
        if(questionIds == null){
            return new ArrayList<>();
        }
        List<QuestionContentVo> questionContentVos = new ArrayList<>();
        QuestionContentVo questionContentVo = null;
        for (Integer questionId:questionIds){
            questionContentVo  = questionMapper.findQuestionContentById(questionId);
            if(questionContentVo != null){
                questionContentVos.add(questionContentVo);
            }
        }
        return questionContentVos;
    }

    @Override
    public List<QuestionContentVo> getQuestionContentsByCondition(String qdbKey, String typeKey, String levelKey, String statusKey, String contentKey) {
        return questionMapper.findQuestionContentsByCondition(qdbKey,typeKey,levelKey,statusKey,contentKey);
    }

    @Override
    public List<QuestionContentVo> getQuestionContents() {
        return questionMapper.findQuestionContents();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQuestion(String id) {
        if(id.contains(",")){
           String[] ids=id.split(",");
            for(String qId:ids){
                questionJpa.delete(Integer.parseInt(qId));
            }
        }else{
            questionJpa.delete(Integer.parseInt(id));
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitQuestion(QuestionDto questionDto, TbUserVo tbUserVo) {
        TbQuestion tbQuestion = new TbQuestion();
        TbQuestionDb tbQuestionDb = questionDbJpa.findOne(questionDto.getQdbId());
        if(tbQuestionDb == null){
            throw new ValidationHtmlException(CodeMsg.DB_SELECT_ERROR);
        }
        tbQuestion.setQContent(questionDto.getQContent());
        tbQuestion.setQdbId(questionDto.getQdbId());
        tbQuestion.setQType(questionDto.getQType());
        tbQuestion.setQLevel(QlevelEnumConverter.converter(questionDto.getQLevel()).getId());
        tbQuestion.setQStatus(questionDto.getQStatus());
        tbQuestion.setUUid(tbUserVo.getId());
        tbQuestion.setOptKey(questionDto.getOptKey());
        tbQuestion.setKeyInfo(questionDto.getKeyInfo());
        boolean updateFlag = questionDto.getId()!=null&&questionDto.getId()!=0;
        if(updateFlag){
            tbQuestion.setId(questionDto.getId());
        }else {
            tbQuestion.setCUid(tbUserVo.getId());
        }
        questionJpa.save(tbQuestion);
        //选择题更新选项
        if(questionDto.getQType().equals(QtypeEnum.EXCLUSIVE_CHOICE.getId())
                ||questionDto.getQType().equals(QtypeEnum.MULTIPLE_CHOICE.getId())){
            //提交的选项内容
            String[] qOptions = questionDto.getQ_options();
            //选项排序
            String[] orderNum = questionDto.getOrderNum();
            if(orderNum.length!=qOptions.length){
                throw new ValidationHtmlException(CodeMsg.Q_OPT_ERROR);
            }
            //更新前数据库选项id集合
            List<Integer> oldOptIds = new ArrayList<>(Arrays.asList(questionOptJpa.findIdsByQId(questionDto.getId())));
            //提交的答案id集合
            List<String> optKeys = new ArrayList<>(Arrays.asList(questionDto.getOptKey().split(",")));
            //更新后的答案选项id集合
            List<String> optKeyId = new ArrayList<>();
            TbQuestionOpt tbQuestionOpt;
            //更新选项
            for (int i=0;i<qOptions.length;i++){
                //选项内容切割（内容+#&#+id）（如果id前有:表示是更新的选项，否则是原来没有改动的选项）
                String[] content = qOptions[i].split("#&#");
                //得到正确id的整数类型
                Integer optId = Integer.parseInt(content[1].replaceAll(":",""));
                tbQuestionOpt = new TbQuestionOpt();
                //选项内容
                tbQuestionOpt.setOptionContent(content[0]);
                tbQuestionOpt.setOrderNum(Integer.parseInt(orderNum[i]));
                tbQuestionOpt.setQId(tbQuestion.getId());
                //如果是原来没有改动的选项并且数据库存在
                if(!content[1].startsWith(":")&&oldOptIds.contains(optId)){
                    tbQuestionOpt.setId(optId);
                }
                TbQuestionOpt questionOpt = questionOptJpa.save(tbQuestionOpt);
                //答案匹配选项id
                if(optKeys.contains(content[1])){
                    //获取选项插入数据库后返回的id
                    optKeyId.add(questionOpt.getId().toString());
                }
                //移除操作过的id
                if(!content[1].startsWith(":")){
                    oldOptIds.remove(optId);
                }
            }
            String optKey = String.join(",",optKeyId);
            //删除其他没有旧的选项
            for (Integer optId:oldOptIds){
                questionOptJpa.deleteById(optId);
            }
            questionJpa.updateOptKey(optKey,tbQuestion.getId());
        }
    }
    @Override
    public QuestionVo getQuestionById(Integer id) {
        TbQuestion tbQuestion = questionJpa.findOne(id);
        Map<String,Object> map = new HashMap<>(8);
        return questionVo(tbQuestion,map,false);
    }

    @Override
    public List<QuestionVo> getQuestions() {
        List<TbQuestion> tbQuestions = questionJpa.findAll();
        List<QuestionVo> questionVos = new ArrayList<>();
        Map<String,Object> map = new HashMap<>(32);
        for (TbQuestion tbQuestion:tbQuestions){
            QuestionVo questionVo = questionVo(tbQuestion,map,true);
            questionVos.add(questionVo);
        }
        return questionVos;
    }
    @Override
    public List<QuestionVo> getQuestionsByCondition(String qdbKey, String typeKey, String levelKey, String statusKey, String contentKey) {
        List<TbQuestion> tbQuestions = questionMapper.findQuestionsByCondition(qdbKey,typeKey,levelKey,statusKey,"%"+contentKey+"%");
        Map<String,Object> map = new HashMap<>(32);
        List<QuestionVo> questionVos = new ArrayList<>();
        for (TbQuestion tbQuestion:tbQuestions){
            QuestionVo questionVo = questionVo(tbQuestion,map,true);
            questionVos.add(questionVo);
        }
        return questionVos;
    }

    private QuestionVo questionVo(TbQuestion tbQuestion,Map<String,Object> map,boolean userFlag){
        if(tbQuestion == null){
            return null;
        }
        QuestionVo questionVo = new QuestionVo();
        questionVo.setId(tbQuestion.getId());
        questionVo.setQContent(tbQuestion.getQContent());
        questionVo.setQStatus(tbQuestion.getQStatus());
        questionVo.setCDate(tbQuestion.getCDate());
        questionVo.setUDate(tbQuestion.getUDate());
        questionVo.setCUid(tbQuestion.getCUid());
        questionVo.setUUid(tbQuestion.getUUid());
        questionVo.setKeyInfo(tbQuestion.getKeyInfo());
        questionVo.setQdbId(tbQuestion.getQdbId());
        TbQuestionDb tbQuestionDb = (TbQuestionDb)map.get("qdb:"+tbQuestion.getQdbId());
        if(tbQuestionDb == null){
            tbQuestionDb = questionDbJpa.findOne(tbQuestion.getQdbId());
            map.put("qdb:"+tbQuestion.getQdbId(),tbQuestionDb);
        }
        questionVo.setQdbName(tbQuestionDb.getQdbName());
        QtypeEnum qtypeEnum = QtypeEnumConverter.converter(tbQuestion.getQType());
        questionVo.setQType(qtypeEnum);
        questionVo.setQLevel(QlevelEnumConverter.converter(tbQuestion.getQLevel()));
        if(userFlag){
            questionVo.setCUserName(userName(map,tbQuestion.getCUid()));
            questionVo.setUUserName(userName(map,tbQuestion.getUUid()));
        }
        questionVo.setOptKey(tbQuestion.getOptKey().split(","));
        if(qtypeEnum == QtypeEnum.EXCLUSIVE_CHOICE||
                qtypeEnum == QtypeEnum.MULTIPLE_CHOICE){
            List<QuestionOptVo> opts = questionOptMapper.findQuestionOptVosByQId(tbQuestion.getId());
            questionVo.setOpts(opts.size()==0?null:opts);
        }
        return questionVo;
    }
    @Override
    public List<TbCourse> getCourses() {
        return courseJpa.findAll();
    }

    @Override
    public TbCourse getCourseById(Integer id) {
        return courseJpa.findById(id);
    }

    @Override
    public void insertCourse(String courseName) {
        courseJpa.insertCourse(courseName);
    }

    @Override
    public void updateCourse(String courseName,Integer id) {
        courseJpa.updateCourse(courseName,id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourse(String id) {
        if(id.contains(",")){
            String[] ids = id.split(",");
            for(String courseIdStr:ids){
                Integer courseId = Integer.parseInt(courseIdStr);
                courseJpa.delete(courseId);
            }
        }else{
            Integer courseId = Integer.parseInt(id);
            courseJpa.delete(courseId);
        }
    }
    @Override
    public void updateQuestionDb(TbQuestionDb tbQuestionDb) {
        questionDbJpa.updateQuestionDb(tbQuestionDb);
    }

    @Override
    public void insertQuestionDb(TbQuestionDb tbQuestionDb) {
        questionDbJpa.insertQuestionDb(tbQuestionDb);
    }
    @Override
    public QuestionDbVo getQdbById(Integer id) {
        TbQuestionDb tbQuestionDb = questionDbJpa.findOne(id);
        Map<String,Object> map =new HashMap<>(4);
        return questionDbVo(tbQuestionDb,map,false);
    }

    @Override
    public List<QuestionDbVo> getQdbs() {
        List<TbQuestionDb> tbQuestionDbs= questionDbJpa.findAll();
        List<QuestionDbVo> questionDbVos = new ArrayList<>();
        Map<String,Object> map =new HashMap<>(8);
        for (TbQuestionDb tbQuestionDb:tbQuestionDbs){
            QuestionDbVo questionDbVo = questionDbVo(tbQuestionDb,map,true);
            questionDbVos.add(questionDbVo);
        }
        return questionDbVos;
    }
    @Override
    public List<QuestionDbVo> getQdbsByCondition(String nameKey, String userNameKey, String courseKey,String statusKey) {
        List<TbQuestionDb> tbQuestionDbs = questionDbMapper.findQdbsByCondition("%"+nameKey+'%',userNameKey+'%',courseKey,statusKey);
        List<QuestionDbVo> questionDbVos = new ArrayList<>();
        Map<String,Object> map =new HashMap<>(8);
        for (TbQuestionDb tbQuestionDb:tbQuestionDbs){
            QuestionDbVo questionDbVo = questionDbVo(tbQuestionDb,map,true);
            questionDbVos.add(questionDbVo);
        }
        return questionDbVos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQuestionDb(String id) {
        if(id.contains(",")){
            String[] ids = id.split(",");
            for(String qdbId:ids){
                questionDbJpa.delete(Integer.parseInt(qdbId));
            }
        }else{
            questionDbJpa.delete(Integer.parseInt(id));
        }
    }

    @Override
    public Integer getQdbIdById(Integer id) {
        return questionDbJpa.findQdbIdById(id);
    }


    @Override
    public List<TbQuestionDb> getTbQdbs() {
        return questionDbJpa.findAll();
    }

    private QuestionDbVo questionDbVo(TbQuestionDb tbQuestionDb, Map<String,Object> map,boolean userFlag){
        if(tbQuestionDb == null){
            return null;
        }
        QuestionDbVo questionDbVo = new QuestionDbVo();
        questionDbVo.setId(tbQuestionDb.getId());
        questionDbVo.setQdbName(tbQuestionDb.getQdbName());
        questionDbVo.setQdbInfo(tbQuestionDb.getQdbInfo());
        questionDbVo.setQdbStatus(tbQuestionDb.getQdbStatus());
        questionDbVo.setCDate(tbQuestionDb.getCDate());
        questionDbVo.setUDate(tbQuestionDb.getUDate());
        questionDbVo.setCUid(tbQuestionDb.getCUid());
        questionDbVo.setUUid(tbQuestionDb.getUUid());
        Integer courseId = tbQuestionDb.getCourseId();
        TbCourse tbCourse = (TbCourse)map.get("course:"+courseId);
        if(tbCourse == null){
            tbCourse = courseJpa.findById(courseId);
            map.put("course:"+courseId,tbCourse);
        }
        questionDbVo.setTbCourse(tbCourse);
        if(userFlag){
            questionDbVo.setCUserName(userName(map,questionDbVo.getCUid()));
            questionDbVo.setUUserName(userName(map,questionDbVo.getUUid()));
        }
        return questionDbVo;
    }
    private  String userName(Map<String,Object> map,Integer userId){
        TbUser cUser = (TbUser)map.get("user:"+userId);
        if(cUser == null){
            cUser = userJpa.findUserById(userId);
            map.put("user:"+userId,cUser);
        }
        return cUser.getName()+"("+RoleEnumConverter.converter(cUser.getRoleId()).getRoleName()+")";
    }
}
