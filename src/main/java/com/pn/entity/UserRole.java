package com.pn.entity;

/**
 * @author ljj
 * @date 2023/8/24 11:06
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 角色用户表的实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRole{

    private Integer userRoleId;

    private Integer roleId;

    private Integer userId;

}
