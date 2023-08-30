package com.pn.entity;

/**
 * @author ljj
 * @create 2023/8/27 14:10
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计实体类:
 * @author ljj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private Integer storeId;//仓库id

    private String storeName;//仓库名称

    private Integer totalInvent;//仓库商品库存数
}
