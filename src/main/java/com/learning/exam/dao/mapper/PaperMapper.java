package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbPaper;
import com.learning.exam.model.vo.ScoreVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-22  11:04
 */
@Mapper
public interface PaperMapper {
    List<ScoreVo> findPaperHadStart(@Param("nowTime") Date nowTime,@Param("nameKey")String nameKey);
    int deletePaperById(@Param("id")Integer id);
    int insertPaper(@Param("tbPaper")TbPaper tbPaper);
    int updatePaper(@Param("tbPaper")TbPaper tbPaper);
    int updatePaperConfigStatus(@Param("configStatus")Integer configStatus, @Param("id")Integer id);
    Integer findPaperTypeById(@Param("id")Integer id);
    Integer findPaperIdById(@Param("id")Integer id);
    Date findPaperStartTimeById(@Param("id") Integer id);
    TbPaper findByIdAndConfigStatus(@Param("id")Integer id,@Param("configStatus")Integer configStatus);
    List<TbPaper> findPapersByCondition(@Param("nameKey")String nameKey, @Param("userNameKey")String userNameKey, @Param("courseKey")String courseKey, @Param("typeKey")String typeKey, @Param("configKey")String configKey);
    List<TbPaper> findPapers();

}
