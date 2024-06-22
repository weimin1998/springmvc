package com.weimin.demo8;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main4_args_resolve {
    public static void main(String[] args) throws Exception {

        /**
         * 本案例，演示springmvc中的异常处理，异常处理方法中的，参数解析
         * 类似目标方法的参数解析
         */

        // 异常处理器
        ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();

        // 给异常处理器 设置消息转换器
        exceptionHandlerExceptionResolver.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));//消息转换
        // 给异常处理器 设置参数解析器和异常处理器
        exceptionHandlerExceptionResolver.afterPropertiesSet();// 参数解析器，返回值处理器，重用了目标方法的参数解析器


        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerMethod handlerMethod = new HandlerMethod(new Controller04(), Controller04.class.getMethod("foo"));

        Exception exception = new ArithmeticException("e1");

        // 模拟在controller1中的foo方法执行时，发生异常
        exceptionHandlerExceptionResolver.resolveException(request, response, handlerMethod, exception);

        System.out.println("==========================");
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
        System.out.println("==========================");

    }

    static class Controller04 {
        public void foo() {
        }

        @ExceptionHandler
        @ResponseBody
        public Map<String, String> handle(Exception e, HttpServletRequest request) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.getMessage());
            System.out.println("异常处理方法的参数："+request);
            return map;
        }
    }
}
