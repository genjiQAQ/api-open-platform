package com.yupi.project.model.dto.interfaceinfo;


import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * id
     */

    private Long id;

    private String userRequestParams;



    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;


}