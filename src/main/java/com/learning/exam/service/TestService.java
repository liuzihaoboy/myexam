package com.learning.exam.service;

import com.learning.exam.model.entity.TbPaperUser;
import com.learning.exam.model.vo.QuestionTestVo;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-02-24  11:42
 */
public interface TestService {
    String exposeUrl(String sessionId,Integer paperId);
    void writeTestInfo(Integer userId,Integer paperId);
    List<QuestionTestVo> getPaperTestQuestion(Integer userId,Integer paperId);
}
