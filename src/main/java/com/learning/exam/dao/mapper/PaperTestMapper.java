package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbPaperTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 用户考试信息持久层mapper
 * @author liuzihao
 * @date 2019-02-27  14:32
 */
@Mapper
public interface PaperTestMapper {
    /**
     * 插入用户考试信息实体
     * @param tbPaperTest 考试信息实体
     * @return 修改行数
     */
    int insertPaperTest(TbPaperTest tbPaperTest);

}
