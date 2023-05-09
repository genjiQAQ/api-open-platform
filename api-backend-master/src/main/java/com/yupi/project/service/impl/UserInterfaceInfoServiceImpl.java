package com.yupi.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.exception.BusinessException;

import com.yupi.project.mapper.UserInterfaceInfoMapper;


import com.yupi.project.service.UserInterfaceInfoService;
import com.yupi.yuapicommon.model.entity.UserInterfaceInfo;

import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-05-06 19:46:27
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {


    @Override
    public boolean invoke(long interfaceInfoId, long userId) {
        if(interfaceInfoId<0||userId<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }

        UpdateWrapper<UserInterfaceInfo> QueryWrapper=new UpdateWrapper<>();
        QueryWrapper.eq("interfaceInfoId",interfaceInfoId);
        QueryWrapper.eq("userId",userId);
        QueryWrapper.gt("leftNum",0);
        QueryWrapper.setSql("leftNum=leftNum-1,totalNum=totalNum+1");
        return this.update(QueryWrapper);
    }

    @Override
    public void validUserInterfaceInfo(com.yupi.yuapicommon.model.entity.UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {

            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 创建时，所有参数必须非空
        if (add) {
            if(userInterfaceInfo.getInterfaceInfoId()<=0||userInterfaceInfo.getUserId()<=0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口用户不存在");
            }

        }
        if(userInterfaceInfo.getLeftNum()<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"次数不正确");
        }
    }


}




