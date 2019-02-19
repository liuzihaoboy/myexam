package com.learning.exam.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author liuzihao
 * @date 2019-01-31  11:11
 */
@Data
public class UserDto {
    @NotEmpty(message = "用户名不能为空")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    private String passWord;
    @NotNull(message = "角色不能为空")
    private Integer role;
    @NotEmpty(message = "验证码不能为空")
    private String verifyCode;
}
