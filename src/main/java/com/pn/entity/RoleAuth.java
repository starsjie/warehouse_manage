package com.pn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ljj
 * @date 2023/8/24 20:14
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleAuth {
    private Integer roleAuthId;

    private Integer roleId;

    private Integer authId;

}
