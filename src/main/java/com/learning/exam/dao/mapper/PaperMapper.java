package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-22  11:04
 */
@Mapper
public interface PaperMapper {
    List<TbPaper> findPapersByCondition(@Param("nameKey")String nameKey, @Param("userNameKey")String userNameKey, @Param("courseKey")String courseKey, @Param("typeKey")String typeKey, @Param("configKey")String configKey);
}
