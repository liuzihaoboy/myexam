package com.learning.exam.dao.jpa;

import com.learning.exam.model.entity.TbPaperSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-21  11:17
 */
@Repository
public interface PaperSectionJpa extends JpaRepository<TbPaperSection,Integer> {
    @Query(nativeQuery = true,value = "select tps.* from tb_paper_section tps where tps.paper_id=:paperId")
    List<TbPaperSection> findByPaperId(@Param("paperId")Integer paperId);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_paper_section set question_num=:questionNum where id=:id")
    int updateQuestionNumById(@Param("id")Integer id,@Param("questionNum")Integer questionNum);
    @Query(nativeQuery = true,value = "select section_type from tb_paper_section where  id=:id")
    Integer findSectionTypeById(@Param("id")Integer id);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_paper_section set question_score=:#{#tbPaperSection.questionScore} where id=:#{#tbPaperSection.id}")
    int updateNormalPaperSectionById(@Param("tbPaperSection")TbPaperSection tbPaperSection);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update tb_paper_section set question_num=:#{#tbPaperSection.questionNum},question_score=:#{#tbPaperSection.questionScore},qdb_ids=:#{#tbPaperSection.qdbIds},level_scale=:#{#tbPaperSection.levelScale} where id=:#{#tbPaperSection.id}")
    int updateRandomPaperSectionById(@Param("tbPaperSection")TbPaperSection tbPaperSection);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_paper_section(paper_id, section_type, question_num, question_score, qdb_ids, level_scale) values(:#{#tbPaperSection.paperId},:#{#tbPaperSection.sectionType},:#{#tbPaperSection.questionNum},:#{#tbPaperSection.questionScore},:#{#tbPaperSection.qdbIds},:#{#tbPaperSection.levelScale})")
    int insertPaperSection(@Param("tbPaperSection")TbPaperSection tbPaperSection);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from tb_paper_section where id=:id and paper_id=:paperId")
    int deletePaperSectionByIdAndPaperId(@Param("paperId")Integer paperId,@Param("id")Integer sectionId);
    @Query(nativeQuery = true,value = "select count(id) from tb_paper_section where paper_id=:paperId")
    int findPaperSectionNumByPaerId(@Param("paperId")Integer paperId);
    @Query(nativeQuery = true,value = "select id from tb_paper_section where paper_id=:paperId")
    List<Integer> findPaperSectionIdByPaperId(@Param("paperId")Integer paperId);
    @Query(nativeQuery = true,value = "select paper_id from tb_paper_section where id=:id")
    Integer findPaperIdById(@Param("id") Integer id);
    @Query(nativeQuery = true,value = "select question_score from tb_paper_section where paper_id=:paperId order by id")
    List<Integer> findQuestionScoresByPaperId(@Param("paperId") Integer paperId);
    @Query(nativeQuery = true,value = "select question_num from tb_paper_section where paper_id=:paperId order by id")
    List<Integer> findQuestionNumsByPaperId(@Param("paperId") Integer paperId);
}

