package com.yupi.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuapicommon.model.entity.InterfaceInfo;


/**
* @author Administrator
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-05-03 21:30:05
*/
public interface InterfaceInfoService extends IService<com.yupi.yuapicommon.model.entity.InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
