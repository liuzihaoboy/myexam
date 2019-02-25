package com.learning.exam.service.impl;

import com.learning.exam.dao.jpa.*;
import com.learning.exam.dao.mapper.PaperMapper;
import com.learning.exam.dao.mapper.QuestionMapper;
import com.learning.exam.framework.cache.SessionCacheName;
import com.learning.exam.framework.enums.PaperTypeEnum;
import com.learning.exam.framework.enums.PaperUserStatusEnum;
import com.learning.exam.framework.enums.converter.PaperTypeEnumConverter;
import com.learning.exam.framework.enums.converter.QtypeEnumConverter;
import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.framework.redis.keys.PaperKey;
import com.learning.exam.framework.redis.keys.QuestionKey;
import com.learning.exam.framework.redis.keys.SessionKey;
import com.learning.exam.framework.redis.keys.UserKey;
import com.learning.exam.framework.redis.service.RedisService;
import com.learning.exam.model.entity.TbPaper;
import com.learning.exam.model.entity.TbPaperTest;
import com.learning.exam.model.entity.TbPaperUser;
import com.learning.exam.model.entity.TbQuestionOpt;
import com.learning.exam.model.result.CodeMsg;
import com.learning.exam.model.vo.*;
import com.learning.exam.service.TestService;
import com.learning.exam.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author liuzihao
 * @date 2019-02-24  11:42
 */
@Service
public class TestServiceImpl implements TestService {
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
    private PaperTestJpa paperTestJpa;
    @Autowired
    private PaperResultJpa paperResultJpa;
    @Autowired
    private RedisService redisService;

    @Override
    public String exposeUrl(String sessionId,Integer paperId) {
        TbUserVo tbUserVo = redisService.hget(SessionKey.sessionById,sessionId,SessionCacheName.LOGIN_USER,TbUserVo.class);
        TbPaperUser tbPaperUser = redisService.get(UserKey.userByPaperUserId,tbUserVo.getId()+":"+paperId,TbPaperUser.class);
        if(tbPaperUser == null){
            tbPaperUser = paperUserJpa.findByUserIdAndPaperId(tbUserVo.getId(),paperId);
            if(tbPaperUser == null){
                throw new ValidationJsonException(CodeMsg.PAPER_USER_JOIN_ERROR);
            }
        }
        String uuid = redisService.hget(SessionKey.sessionByUserId,Integer.toString(tbUserVo.getId()),SessionCacheName.UUID,String.class);
        if(StringUtils.isEmpty(uuid)){
            uuid = SnowFlake.getInstance().nextId();
            redisService.hset(SessionKey.sessionByUserId,Integer.toString(tbUserVo.getId()),SessionCacheName.UUID,uuid);
        }
        return  "../test/"+paperId+"?t="+uuid;
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void writeTestInfo(Integer userId,Integer paperId) {
        String questionIdsStr = redisService.get(UserKey.userByPaperTestId,userId+":"+paperId,String.class);
        if(!StringUtils.isEmpty(questionIdsStr)){
            return;
        }
        TbPaperUser tbPaperUser = redisService.get(UserKey.userByPaperUserId,userId+":"+paperId,TbPaperUser.class);
        PaperTestVo paperTestVo = redisService.get(PaperKey.paperById,Integer.toString(paperId),PaperTestVo.class);
        int expire = (int)((paperTestVo.getEndTime().getTime()-System.currentTimeMillis())/1000);
        if(tbPaperUser.getStatus().equals(PaperUserStatusEnum.WAIT_START.getId())){
            paperUserJpa.updateStatus(PaperUserStatusEnum.BE_IN.getId(),tbPaperUser.getId());
            tbPaperUser.setStatus(PaperUserStatusEnum.BE_IN.getId());
            redisService.setex(UserKey.userByPaperUserId,tbPaperUser.getUserId()+":"+tbPaperUser.getPaperId(),tbPaperUser,expire);
        }
        PaperTypeEnum typeEnum = PaperTypeEnumConverter.converter(paperTestVo.getPaperType());
        List<String> questionIds = new ArrayList<>(32);
        List<SectionTestVo> sections = paperTestVo.getSections();
        if(typeEnum == PaperTypeEnum.NORMAL_TYPE){
            //获取章节
            for(SectionTestVo sectionTestVo:sections){
                List<QuestionTestVo> questions = sectionTestVo.getQuestions();
                if(!CollectionUtils.isEmpty(questionIds)){
                    for (QuestionTestVo questionTestVo:questions){
                        questionIds.add(Integer.toString(questionTestVo.getId()));
                    }
                }
            }
            questionIdsStr = questionIds.stream().collect(Collectors.joining(","));
        }else if(typeEnum == PaperTypeEnum.RANDOM_TYPE){
            //随机组卷
            questionIdsStr = randomPaperGroup(sections);
        }
        if(questionIdsStr!=null){
            paperTestJpa.insertPaperTest(tbPaperUser.getId(),questionIdsStr);
            //写入缓存
            redisService.setex(UserKey.userByPaperTestId,userId+":"+paperId,questionIdsStr,expire);
        }
    }

    @Override
    public List<QuestionTestVo> getPaperTestQuestion(Integer userId, Integer paperId) {
        String questionIdsStr = redisService.get(UserKey.userByPaperTestId,userId+":"+paperId,String.class);
        if(StringUtils.isEmpty(questionIdsStr)){
            return null;
        }
        String[] questionIds = questionIdsStr.split(",");
        List<QuestionTestVo> questions = new ArrayList<>(questionIds.length);
        for (String questionId:questionIds){
            QuestionTestVo questionTestVo = redisService.get(QuestionKey.questionById,questionId,QuestionTestVo.class);
            if(questionTestVo==null){
                Integer id = Integer.parseInt(questionId);
                QuestionContentVo question = questionMapper.findQuestionContentById(id);
                if(question == null){
                    continue;
                }
                questionTestVo = new QuestionTestVo();
                questionTestVo.setId(question.getId());
                questionTestVo.setQuestionContent(question.getQuestionContent());
                questionTestVo.setQuestionType(question.getQType());
                questionTestVo.setQuestionTypeName(QtypeEnumConverter.converter(question.getQType()).getType());
                List<TbQuestionOpt> opts = questionOptJpa.findByQId(id);
                questionTestVo.setOpts(opts);
                redisService.set(QuestionKey.questionById,questionId,questionTestVo);
            }
            questions.add(questionTestVo);
        }
        return questions;
    }

    //TODO
    private String randomPaperGroup(List<SectionTestVo> sections){
        return null;
    }
}
