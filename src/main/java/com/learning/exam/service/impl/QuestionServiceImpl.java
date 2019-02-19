package com.learning.exam.service.impl;

import com.learning.exam.dao.jpa.*;
import com.learning.exam.dao.mapper.QuestionDbMapper;
import com.learning.exam.dao.mapper.QuestionMapper;
import com.learning.exam.framework.enums.QlevelEnum;
import com.learning.exam.framework.enums.QtypeEnum;
import com.learning.exam.framework.enums.converter.QlevelEnumConverter;
import com.learning.exam.framework.enums.converter.QtypeEnumConverter;
import com.learning.exam.framework.enums.converter.RoleEnumConverter;
import com.learning.exam.framework.exception.ValidationHtmlException;
import com.learning.exam.model.dto.QuestionDto;
import com.learning.exam.model.entity.*;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.vo.QuestionDbVo;
import com.learning.exam.model.vo.QuestionVo;
import com.learning.exam.model.vo.TbUserVo;
import com.learning.exam.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

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
    private UserJpa userJpa;

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
        //选择题更新选项
        String optKey = null;
        if(questionDto.getQType().equals(QtypeEnum.EXCLUSIVE_CHOICE.getId())
                ||questionDto.getQType().equals(QtypeEnum.MULTIPLE_CHOICE.getId())){
            String[] qOptions = questionDto.getQ_options();
            String[] orderNum = questionDto.getOrderNum();
            if(orderNum.length!=qOptions.length){
                throw new ValidationHtmlException(CodeMsg.Q_OPT_ERROR);
            }
            List<Integer> oldOptIds = new ArrayList<>(Arrays.asList(questionOptJpa.findIdsByQId(questionDto.getId())));
            List<String> oldOptKeys = new ArrayList<>(Arrays.asList(questionDto.getOptKey().split(",")));
            List<String> optKeyId = new ArrayList<>();
            TbQuestionOpt tbQuestionOpt;
            for (int i=0;i<qOptions.length;i++){
                String[] content = qOptions[i].split("#&#");
                Integer optId = Integer.parseInt(content[1].replaceAll(":",""));
                tbQuestionOpt = new TbQuestionOpt();
                tbQuestionOpt.setOptionContent(content[0]);
                tbQuestionOpt.setOrderNum(Integer.parseInt(orderNum[i]));
                tbQuestionOpt.setQId(questionDto.getId());
                if(!content[1].startsWith(":")&&oldOptIds.contains(optId)){
                    tbQuestionOpt.setId(optId);
                }
                TbQuestionOpt questionOpt = questionOptJpa.save(tbQuestionOpt);
                if(oldOptKeys.contains(content[1])){
                    optKeyId.add(questionOpt.getId().toString());
                }
                if(!content[1].startsWith(":"))oldOptIds.remove(optId);
            }
            optKey = String.join(",",optKeyId);
            for (Integer optId:oldOptIds){
                questionOptJpa.deleteById(optId);
            }
        }
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
        tbQuestion.setOptKey(optKey!=null?optKey:questionDto.getOptKey());
        tbQuestion.setKeyInfo(questionDto.getKeyInfo());
        boolean updateFlag = questionDto.getId()!=null&&questionDto.getId()!=0;
        if(updateFlag){
            tbQuestion.setId(questionDto.getId());
            questionJpa.updateQuestion(tbQuestion);
        }else {
            tbQuestion.setCUid(tbUserVo.getId());
            questionJpa.insertQuestion(tbQuestion);
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
        questionVo.setQType(QtypeEnumConverter.converter(tbQuestion.getQType()));
        questionVo.setQLevel(QlevelEnumConverter.converter(tbQuestion.getQLevel()));
        if(userFlag){
            questionVo.setCUserName(userName(map,tbQuestion.getCUid()));
            questionVo.setUUserName(userName(map,tbQuestion.getUUid()));
        }
        questionVo.setOptKey(tbQuestion.getOptKey().split(","));
        List<TbQuestionOpt> tbQuestionOpts = questionOptJpa.findByQId(tbQuestion.getId());
        questionVo.setOpts(tbQuestionOpts.size()==0?null:tbQuestionOpts);
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
        List<TbQuestionDb> tbQuestionDbs = null;
        if(!StringUtils.isEmpty(courseKey)){
            tbQuestionDbs = questionDbMapper.findQdbsByCondition(nameKey+'%',userNameKey+'%',Integer.parseInt(courseKey),statusKey);
        }else {
            tbQuestionDbs = questionDbMapper.findQdbsByCondition(nameKey+'%',userNameKey+'%',null,statusKey);
        }
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
