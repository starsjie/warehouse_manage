package com.pn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ljj
 * @date 2023/8/25 19:16
 */

/**
 * 仓库表store表对应的实体类:
 * @author ljj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Store implements Serializable {

    private Integer storeId;//仓库id

    private String storeName;//仓库名称

    private String storeNum;//仓库编码

    private String storeAddress;//仓库地址

    private String concat;//仓库联系人

    private String phone;//仓库联系电话
}
