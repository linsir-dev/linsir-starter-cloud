package com.linsir.cloud.config;

import com.linsir.base.core.constant.CommonConstant;
import feign.Feign;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @author ：linsir
 * @date ：Created in 2022/6/1 16:07
 * @description：FeignConfig
 * @modified By：
 * @version: 0.0.1
 */
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
@Slf4j
@Configuration
public class FeignConfig {

    /***
    * @Description: 设置feign head 参数
    * @Param:
    * @return:
    * @Author: Linsir
    * @Date: 2022/6/1 16:07
    */


    @Bean
    public RequestInterceptor requestInterceptor()
    {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                log.debug("Feign request: {}", request.getRequestURI());
                // 将token信息放入header中
                String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
                if(token==null || "".equals(token)){
                    token = request.getParameter("token");
                }
                log.info("Feign Login Request token: {}", token);
                requestTemplate.header(CommonConstant.X_ACCESS_TOKEN, token);
            }else{
//                String token = UserTokenContext.getToken();
//                log.info("Feign no Login token: {}", token);
//                requestTemplate.header(CommonConstant.X_ACCESS_TOKEN, token);
            }

            //================================================================================================================
            //针对特殊接口，进行加签验证 ——根据URL地址过滤请求 【字典表参数签名验证】
//            if (PathMatcherUtil.matches(Arrays.asList(SignAuthConfiguration.SIGN_URL_LIST),requestTemplate.path())) {
//                try {
//                    log.info("============================ [begin] fegin starter url ============================");
//                    log.info(requestTemplate.path());
//                    log.info(requestTemplate.method());
//                    String queryLine = requestTemplate.queryLine();
//                    if(queryLine!=null && queryLine.startsWith("?")){
//                        queryLine = queryLine.substring(1);
//                    }
//                    log.info(queryLine);
//                    if(requestTemplate.body()!=null){
//                        log.info(new String(requestTemplate.body()));
//                    }
//                    SortedMap<String, String> allParams = HttpUtils.getAllParams(requestTemplate.path(),queryLine,requestTemplate.body(),requestTemplate.method());
//                    String sign = SignUtil.getParamsSign(allParams);
//                    log.info(" Feign request params sign: {}",sign);
//                    log.info("============================ [end] fegin starter url ============================");
//                    requestTemplate.header(CommonConstant.X_SIGN, sign);
//                    requestTemplate.header(CommonConstant.X_TIMESTAMP, DateUtils.getCurrentTimestamp().toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            //================================================================================================================
        };
    }


}
