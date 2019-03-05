package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbPaperUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户考试关系持久层mapper
 * @author liuzihao
 * @date 2019-02-27  14:44
 */
@Mapper
public interface PaperUserMapper {
    /**
     * 获取用户考试关系
     * @param userId 用户id
     * @param paperId 试卷id
     * @return 用户考试关系
     */
    TbPaperUser findByUserIdAndPaperId(@Param("userId")Integer userId, @Param("paperId") Integer paperId);

    /**
     * 获取用户所有已出结果考试
     * @param userId 用户id
     * @return 用户考试关系list
     */
    List<TbPaperUser> findByUserIdSubmit(@Param("userId") Integer userId);

    /**
     * 获取用户所有未出结果考试
     * @param userId 用户id
     * @return 用户考试关系list
     */
    List<TbPaperUser> findByUserIdNoSubmit(@Param("userId") Integer userId);

    /**
     * 获取用户考试状态
     * @param userId 用户id
     * @param paperId 试卷id
     * @return 状态
     */
    int findStatusByUserIdAndPaperId(@Param("userId")Integer userId,@Param("paperId")Integer paperId);
    /**
     * 悲观锁
     * 获取用户考试状态
     * @param userId 用户id
     * @param paperId 试卷id
     * @return 状态
     */
    int findStatusByUserIdAndPaperIdForUpdate(@Param("userId")Integer userId,@Param("paperId")Integer paperId);

    /**
     * 删除用户考试关系
     * @param paperId 考试id
     * @return 修改行数
     */
    int deletePaperUserByPaperId(@Param("paperId")Integer paperId);

    /**
     * 修改用户考试状态
     * @param status 状态
     * @param id 用户考试关系id
     * @return 修改行数
     */
    int updateStatus(@Param("status") Integer status,@Param("id")Integer id);

    /**
     * 插入用户考试关系
     * @param userId 用户id
     * @param paperId 试卷id
     * @param status 状态
     * @return 修改行数
     */
    int insertPaperUser(@Param("userId")Integer userId,@Param("paperId")Integer paperId,@Param("status")Integer status);
}
