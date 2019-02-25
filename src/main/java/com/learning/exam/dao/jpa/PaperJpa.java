package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * @author liuzihao
 * @date 2019-02-21  11:16
 */
@Repository
public interface PaperJpa extends JpaRepository<TbPaper,Integer> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from tb_paper where id=:id")
    int deletePaperById(@Param("id")Integer id);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_paper(paper_name, course_id, start_time, end_time, paper_minute, show_key, paper_type, to_user, pass_score, config_status, c_uid) values (:#{#tbPaper.paperName},:#{#tbPaper.courseId},:#{#tbPaper.startTime},:#{#tbPaper.endTime},:#{#tbPaper.paperMinute},:#{#tbPaper.showKey},:#{#tbPaper.paperType},:#{#tbPaper.toUser},:#{#tbPaper.passScore},:#{#tbPaper.configStatus},:#{#tbPaper.CUid})")
    int insertPaper(@Param("tbPaper")TbPaper tbPaper);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_paper set paper_name=:#{#tbPaper.paperName},course_id=:#{#tbPaper.courseId},start_time=:#{#tbPaper.startTime},end_time=:#{#tbPaper.endTime},paper_minute=:#{#tbPaper.paperMinute},show_key=:#{#tbPaper.showKey},paper_type=:#{#tbPaper.paperType},to_user=:#{#tbPaper.toUser},pass_score=:#{#tbPaper.passScore},config_status=:#{#tbPaper.configStatus} where id=:#{#tbPaper.id}")
    int updatePaper(@Param("tbPaper")TbPaper tbPaper);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_paper set config_status=:configStatus where id=:id")
    int updatePaperConfigStatus(@Param("configStatus")Integer configStatus,@Param("id")Integer id);
    @Query(nativeQuery = true,value = "select paper_type from tb_paper where id=:id")
    Integer findPaperTypeById(@Param("id")Integer id);
    @Query(nativeQuery = true,value = "select id from tb_paper where id=:id")
    Integer findPaperIdById(@Param("id")Integer id);
    @Query(nativeQuery = true,value = "select start_time from tb_paper where id=:id")
    Date findPaperStartTimeById(@Param("id") Integer id);
    TbPaper findByIdAndConfigStatus(Integer id,Integer configStatus);
}
