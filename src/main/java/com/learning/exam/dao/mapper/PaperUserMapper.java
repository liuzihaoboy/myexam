package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbPaperUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-27  14:44
 */
@Mapper
public interface PaperUserMapper {
    TbPaperUser findByUserIdAndPaperId(@Param("userId")Integer userId, @Param("paperId") Integer paperId);
    List<TbPaperUser> findByUserIdSubmit(@Param("userId") Integer userId);
    List<TbPaperUser> findByUserIdNoSubmit(@Param("userId") Integer userId);
    int findStatusByUserIdAndPaperId(@Param("userId")Integer userId,@Param("paperId")Integer paperId);
    int findStatusByUserIdAndPaperIdForUpdate(@Param("userId")Integer userId,@Param("paperId")Integer paperId);
    int deletePaperUserByPaperId(@Param("paperId")Integer paperId);
    int updateStatus(@Param("status") Integer status,@Param("id")Integer id);
    int insertPaperUser(@Param("userId")Integer userId,@Param("paperId")Integer paperId,@Param("status")Integer status);
}
