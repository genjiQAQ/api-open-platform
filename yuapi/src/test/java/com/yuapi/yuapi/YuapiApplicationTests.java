package com.yuapi.yuapi;



import com.yupi.yuapiclientsdk.client.YuApiClient;
import com.yupi.yuapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;

@SpringBootTest
class YuapiApplicationTests {
    @Resource
    private YuApiClient yuApiClient;
    @Test
    void contextLoads() {

        String byGet = yuApiClient.getNameByGet("yupi");
        User u=new User();
        System.out.println(byGet);
        u.setUsername("hhhccc");
        String userNameByPost = yuApiClient.getUsernameByPost(u);

        System.out.println(userNameByPost);
    }

}
