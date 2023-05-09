package com.yupi.yuapigateway;

import com.yupi.yuapiclientsdk.utils.SignUtils;
import com.yupi.yuapicommon.model.entity.InterfaceInfo;
import com.yupi.yuapicommon.model.entity.User;
import com.yupi.yuapicommon.service.InnerInterfaceInfoService;
import com.yupi.yuapicommon.service.InnerUserInterfaceInfoService;
import com.yupi.yuapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;
    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;
    @DubboReference
    private InnerUserService innerUserService;




    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String path = "http://localhost:8123"+request.getPath().value();
        String method = request.getMethod().toString();

        String addr = request.getLocalAddress().getHostString();
        log.info(addr);
        ServerHttpResponse response = exchange.getResponse();
//        if(!white.contains(addr)){
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
//        }
        log.info(request.getId());
        log.info("custom global filter");
        //todo 查数据库
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String onnce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        //查表得到 accesskey 和secretkey
        User invokeUser =null;
 
        try {
            invokeUser  = innerUserService.getInvokeUser(accessKey);
        }catch (Exception e){
            log.info("no user",e);
        }
        if(invokeUser==null){
            return handleNoAuth(response);
        }
       
        if(Long.parseLong(onnce) >1000000){
            return handleNoAuth(response);
        }
        Long five_min=60*5L;
        if(System.currentTimeMillis()/1000-Long.parseLong(timestamp)>five_min){
            return handleNoAuth(response);
        }
        String getsign = SignUtils.genSign(body, invokeUser.getSecretKey());
        if(sign==null||!sign.equals(getsign)){
            throw  new RuntimeException("无权限");
        }
        InterfaceInfo interfaceInfo =null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        }catch (Exception e){
            log.info("no user",e);
        }
        if(interfaceInfo==null){
            return handleNoAuth(response);
        }

        if(!innerUserInterfaceInfoService.invoke(interfaceInfo.getId(),invokeUser.getId())){
            return handleNoAuth(response);
        }
        return handleResponse(exchange,chain,interfaceInfo.getId(),invokeUser.getId());
    }

    public Mono handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = (HttpStatus) originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
//                                        // todo 7. 调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            innerUserInterfaceInfoService.invoke(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }



    @Override
    public int getOrder() {

        return -1;
    }


}