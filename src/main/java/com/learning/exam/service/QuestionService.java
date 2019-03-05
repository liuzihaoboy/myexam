package com.learning.exam.service;

import com.learning.exam.model.dto.QuestionDto;
import com.learning.exam.model.entity.TbCourse;
import com.learning.exam.model.entity.TbQuestionDb;
import com.learning.exam.model.vo.*;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-11  13:58
 */
public interface QuestionService {
    QuestionResultVo getQuestionResultById(Integer id);
    List<Integer> getQuestionIdsBySectionId(Integer sectionId);
    List<QuestionContentVo> getQuestionContentsBySectionId(Integer sectionId);
    List<QuestionContentVo> getQuestionContentsByCondition(String qdbKey, String typeKey, String levelKey, String statusKey, String contentKey);
    List<QuestionContentVo> getQuestionContents();
    void deleteQuestion(String id);
    void submitQuestion(QuestionDto questionDto, TbUserVo tbUserVo);
    QuestionVo getQuestionById(Integer id);
    List<QuestionVo> getQuestions();
    List<QuestionVo> getQuestionsByCondition(String qdbKey, String typeKey, String levelKey, String statusKey, String contentKey);
    List<TbCourse> getCourses();
    TbCourse getCourseById(Integer id);
    void insertCourse(String courseName);
    void updateCourse(String courseName,Integer id);
    void deleteCourse(String id);
    void updateQuestionDb(TbQuestionDb tbQuestionDb);
    void insertQuestionDb(TbQuestionDb tbQuestionDb);
    void deleteQuestionDb(String id);
    Integer getQdbIdById(Integer id);
    List<TbQuestionDb> getTbQdbs();
    QuestionDbVo getQdbById(Integer id);
    List<QuestionDbVo> getQdbs();
    List<QuestionDbVo> getQdbsByCondition(String nameKey,String userNameKey,String courseKey,String statusKey);
}
