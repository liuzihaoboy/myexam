package com.learning.exam.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liuzihao
 * @date 2019-02-21  19:45
 */
@Data
public class QuestionContentVo {
    private Integer id;
    private String questionContent;
    private String optKey;
    private Integer qType;
    private Integer qLevel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionContentVo that = (QuestionContentVo) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
