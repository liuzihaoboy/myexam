package com.learning.exam.model.vo;

import com.learning.exam.model.entity.TbPermission;
import com.learning.exam.model.entity.TbRole;
import lombok.Data;

import java.util.List;

/**
 * @author liuzihao
 * @date 2019-01-05  17:02
 */
@Data
public class TbUserVo {
    private Integer id;
    private String account;
    private String name;
    private String phone;
    private String email;
    private TbRole tbRole;
    private List<TbPermission> tbPermissions;
}
