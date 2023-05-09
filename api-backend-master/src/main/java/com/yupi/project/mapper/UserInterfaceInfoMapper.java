package com.yupi.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.yuapicommon.model.entity.InterfaceInfo;
import com.yupi.yuapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author Administrator
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-05-06 19:46:27
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterface(int limit);

}




