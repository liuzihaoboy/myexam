package com.learning.exam.dao.mapper;

import com.learning.exam.model.entity.TbPaper;
import com.learning.exam.model.vo.ScoreVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

/**
 * 试卷持久层mapper
 * @author liuzihao
 * @date 2019-02-22  11:04
 */
@Mapper
public interface PaperMapper {
    /**
     * 获取所有已经开始的考试用户成绩
     * @param nowTime 当前时间
     * @param nameKey 条件查询：用户账户
     * @return 用户成绩 list
     */
    List<ScoreVo> findPaperHadStart(@Param("nowTime") Date nowTime,@Param("nameKey")String nameKey);

    /**
     * 删除试卷信息
     * @param id 试卷id
     * @return 修改行数
     */
    int deletePaperById(@Param("id")Integer id);

    /**
     * 插入试卷实体
     * @param tbPaper 试卷实体
     * @return 修改行数
     */
    int insertPaper(@Param("tbPaper")TbPaper tbPaper);

    /**
     * 修改试卷实体
     * @param tbPaper 试卷实体
     * @return 修改行数
     */
    int updatePaper(@Param("tbPaper")TbPaper tbPaper);

    /**
     * 修改试卷是否配置
     * @param configStatus 0已配置1未配置
     * @param id 试卷id
     * @return 修改行数
     */
    int updatePaperConfigStatus(@Param("configStatus")Integer configStatus, @Param("id")Integer id);

    /**
     * 获取试卷类型
     * @param id 试卷id
     * @return 类型
     */
    Integer findPaperTypeById(@Param("id")Integer id);

    /**
     * 获取试卷id
     * @param id 试卷id
     * @return id
     */
    Integer findPaperIdById(@Param("id")Integer id);

    /**
     * 获取试卷开始时间
     * @param id 试卷id
     * @return 开始时间
     */
    Date findPaperStartTimeById(@Param("id") Integer id);

    /**
     * 获取试卷实体
     * @param id 试卷id
     * @param configStatus 是否配置（0已配置1未配置）
     * @return 试卷实体
     */
    TbPaper findByIdAndConfigStatus(@Param("id")Integer id,@Param("configStatus")Integer configStatus);

    /**
     * 获取试卷list
     * @param nameKey 条件查询：试卷名
     * @param userNameKey 条件查询：创建人名称
     * @param courseKey 条件查询：归属课程id
     * @param typeKey 条件查询：试卷类型
     * @param configKey 条件查询：是否配置（0已配置1未配置）
     * @return 试卷list
     */
    List<TbPaper> findPapersByCondition(@Param("nameKey")String nameKey, @Param("userNameKey")String userNameKey, @Param("courseKey")String courseKey, @Param("typeKey")String typeKey, @Param("configKey")String configKey);

    /**
     * 获取所有试卷
     * @return 试卷list
     */
    List<TbPaper> findPapers();

}
