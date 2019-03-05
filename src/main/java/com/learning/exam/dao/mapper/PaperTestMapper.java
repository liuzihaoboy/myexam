package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbPaperTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author liuzihao
 * @date 2019-02-27  14:32
 */
@Mapper
public interface PaperTestMapper {
    Integer findIdByPaperUserId(@Param("paperUserId")Integer paperUserId);
    int insertPaperTest(TbPaperTest tbPaperTest);

}
