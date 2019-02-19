package com.learning.exam.service;

import com.learning.exam.dao.jpa.UserJpa;
import com.learning.exam.framework.enums.RoleEnum;
import com.learning.exam.model.entity.TbUser;
import com.learning.exam.model.vo.TbUserVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liuzihao
 * @date 2019-01-31  10:34
 */
public interface UserService {
    Integer updatePwd (String userName,String passWord);
    TbUserVo getUserVoFromTb(TbUser tbUser);
    TbUser getUser(String userName,String passWord,RoleEnum roleEnum);
}
