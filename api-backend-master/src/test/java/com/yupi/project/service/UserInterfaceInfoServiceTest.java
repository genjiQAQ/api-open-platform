package com.yupi.project.service;


import com.yupi.project.service.impl.InnerUserInterfaceInfoServiceImpl;
import com.yupi.yuapicommon.service.InnerUserInterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest
public class UserInterfaceInfoServiceTest {
    @Resource
    private InnerUserInterfaceInfoService inneruserInterfaceInfoService;

    @Test
    public void invoke() {
        boolean invoke = inneruserInterfaceInfoService.invoke(1l, 1l);
        System.out.println(invoke);

    }
}