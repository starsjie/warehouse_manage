package com.pn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ljj
 * @date 2023/8/25 19:14
 */
/**
 * 品牌表brand表对应的实体类:
 * @author ljj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Brand implements Serializable {

    private Integer brandId;//品牌id

    private String brandName;//品牌名称

    private String brandLeter;//品牌首字母

    private String brandDesc;//品牌描述
}
