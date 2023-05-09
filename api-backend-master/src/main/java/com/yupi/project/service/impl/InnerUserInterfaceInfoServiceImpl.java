package com.yupi.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.project.service.UserInterfaceInfoService;
import com.yupi.yuapicommon.model.entity.UserInterfaceInfo;
import com.yupi.yuapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean vaildInvoke(long interfaceInfoId, long userId) {
        QueryWrapper<UserInterfaceInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("interface_info_id",interfaceInfoId);
        queryWrapper.eq("user_id",userId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
        if(userInterfaceInfo==null||userInterfaceInfo.getLeftNum()<=0){
            return false;

        }
        return true;
    }

    @Override
    public boolean invoke(long interfaceInfoId, long userId) {
        boolean invoke = userInterfaceInfoService.invoke(interfaceInfoId, userId);
        return invoke;
    }
}
