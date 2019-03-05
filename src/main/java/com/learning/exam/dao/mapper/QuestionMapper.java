package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbQuestion;
import com.learning.exam.model.vo.QuestionContentVo;
import com.learning.exam.model.vo.QuestionResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 题目持久层mapper
 * @author liuzihao
 * @date 2019-02-16  14:56
 */
@Mapper
public interface QuestionMapper {
    /**
     * 获取已出考试结果题目
     * @param id 题目id
     * @return 考试结果题目
     */
    QuestionResultVo findQuestionResultById(@Param("id")Integer id);

    /**
     * 获取题目题干list
     * @param qtype 题目类型
     * @param qdbIds 题库id
     * @return 题目题干list
     */
    List<QuestionContentVo> findQuestionIdsByQdbIdAndQtype(@Param("qtype")Integer qtype, @Param("qdbIds")List<Integer> qdbIds);

    /**
     * 获取题目题干
     * @param id 题目id
     * @return 题干
     */
    QuestionContentVo findQuestionContentById(@Param("id")Integer id);

    /**
     * 获取所有题目题干
     * @param qdbKey 条件查询：题库id
     * @param typeKey 条件查询：题目类型
     * @param levelKey 条件查询：题目等级
     * @param statusKey 条件查询：题目状态
     * @param contentKey 条件查询：题干内容
     * @return list
     */
    List<QuestionContentVo> findQuestionContentsByCondition(@Param("qdbKey") String qdbKey, @Param("typeKey") String typeKey, @Param("levelKey") String levelKey,@Param("statusKey")String statusKey, @Param("contentKey") String contentKey);

    /**
     * 获取所有题目题干list
     * @return list
     */
    List<QuestionContentVo> findQuestionContents();

    /**
     * 获取所有题目
     * @param qdbKey 条件查询：题库id
     * @param typeKey 条件查询：题目类型
     * @param levelKey 条件查询：题目等级
     * @param statusKey 条件查询：题目状态
     * @param contentKey 条件查询：题干内容
     * @return
     */
    List<TbQuestion> findQuestionsByCondition(@Param("qdbKey") String qdbKey, @Param("typeKey") String typeKey, @Param("levelKey") String levelKey,@Param("statusKey")String statusKey, @Param("contentKey") String contentKey);
}
