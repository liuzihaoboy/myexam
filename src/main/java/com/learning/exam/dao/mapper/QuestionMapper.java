package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbQuestion;
import com.learning.exam.model.vo.QuestionContentVo;
import com.learning.exam.model.vo.QuestionResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author liuzihao
 * @date 2019-02-16  14:56
 */
@Mapper
public interface QuestionMapper {
    QuestionResultVo findQuestionResultById(@Param("id")Integer id);
    List<QuestionContentVo> findQuestionIdsByQdbIdAndQtype(@Param("qtype")Integer qtype, @Param("qdbIds")List<Integer> qdbIds);
    QuestionContentVo findQuestionContentById(@Param("id")Integer id);
    List<QuestionContentVo> findQuestionContentsByCondition(@Param("qdbKey") String qdbKey, @Param("typeKey") String typeKey, @Param("levelKey") String levelKey,@Param("statusKey")String statusKey, @Param("contentKey") String contentKey);
    List<QuestionContentVo> findQuestionContents();
    List<TbQuestion> findQuestionsByCondition(@Param("qdbKey") String qdbKey, @Param("typeKey") String typeKey, @Param("levelKey") String levelKey,@Param("statusKey")String statusKey, @Param("contentKey") String contentKey);
}
