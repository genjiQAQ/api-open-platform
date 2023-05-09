package com.yupi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.project.annotation.AuthCheck;
import com.yupi.project.common.BaseResponse;
import com.yupi.project.common.ResultUtils;
import com.yupi.project.mapper.UserInterfaceInfoMapper;
import com.yupi.project.model.vo.InterfaceInfoVO;
import com.yupi.project.service.InterfaceInfoService;
import com.yupi.yuapicommon.model.entity.InterfaceInfo;
import com.yupi.yuapicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {


    @Resource
    private UserInterfaceInfoMapper userinterfaceInfoMapper;
    @Resource
    private InterfaceInfoService InterfaceInfoService;

    @GetMapping("top/interface/invoke")

    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterface(){
        List<UserInterfaceInfo> interfaceInfos = userinterfaceInfoMapper.listTopInvokeInterface(10);

        Map<Long, List<UserInterfaceInfo>> collect = interfaceInfos.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo>queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",collect.keySet());
        List<InterfaceInfo> list = InterfaceInfoService.list(queryWrapper);

        if(list == null || list.isEmpty()){
            throw new RuntimeException("接口信息不存在");
        }
        List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            int totalNum = collect.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOList);

    }

}
