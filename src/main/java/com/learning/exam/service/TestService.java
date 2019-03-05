package com.learning.exam.service;

import com.learning.exam.model.entity.TbPaperUser;
import com.learning.exam.model.vo.QuestionTestVo;
import com.learning.exam.model.vo.StudentPaperVo;

import java.util.List;
import java.util.Map;

/**
 * @author liuzihao
 * @date 2019-02-24  11:42
 */
public interface TestService {
    String exposeUrl(String userId,String paperId);
    void writeTestInfo(String userId,String paperId);
    StudentPaperVo getPaperTestQuestion(String userId, String paperId);
    void savePaperTest(String userId,String paperId,String saveKey);
    void submitPaperTest(String userId, String paperId, String optKey);
}
