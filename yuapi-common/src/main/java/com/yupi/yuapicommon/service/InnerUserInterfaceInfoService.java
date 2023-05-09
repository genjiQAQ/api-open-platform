package com.yupi.yuapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuapicommon.model.entity.UserInterfaceInfo;


/**
* @author Administrator
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-05-06 19:46:27
*/
public interface InnerUserInterfaceInfoService  {
    boolean vaildInvoke(long interfaceInfoId ,long userId);

    boolean invoke(long interfaceInfoId ,long userId);

}
