package com.bruce;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Bruce
 * @create 2023/12/13
 * @description jar包实体
 */
@Data
@AllArgsConstructor
public class JarDTO {
    /**
     * jar包下载路径
     */
    String downloadUrl;

    /**
     * jar包地址
     */
    String patch;
}
