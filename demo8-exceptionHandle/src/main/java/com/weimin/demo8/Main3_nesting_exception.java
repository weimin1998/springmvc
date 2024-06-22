package com.weimin.demo8;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main3_nesting_exception {
    public static void main(String[] args) throws Exception {

        /**
         * 本案例，演示springmvc中的异常处理，处理嵌套异常
         *
         * 发生异常时，异常处理器会把异常完全展开，
         */

        // 异常处理器
        ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();

        // 给异常处理器 设置消息转换器
        exceptionHandlerExceptionResolver.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));//消息转换
        // 给异常处理器 设置参数解析器和异常处理器
        exceptionHandlerExceptionResolver.afterPropertiesSet();// 参数解析器，返回值处理器，重用了目标方法的参数解析器


        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerMethod handlerMethod = new HandlerMethod(new Controller03(), Controller03.class.getMethod("foo"));

        // 异常处理器会把异常完全展开，只要其中某个异常匹配上，就可以处理。
        // 比如下面，最内层的是IO异常，可以被Controller03中的 @ExceptionHandler 处理
        Exception exception = new Exception("e1", new RuntimeException("e2", new IOException("e3")));

        // 模拟在controller3中的foo方法执行时，发生异常
        ModelAndView modelAndView = exceptionHandlerExceptionResolver.resolveException(request, response, handlerMethod, exception);

        System.out.println("===========response===============");
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
        System.out.println("==========================");

    }
    static class Controller03 {
        public void foo() {
        }

        @ExceptionHandler
        @ResponseBody
        public Map<String, String> handle(IOException e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.getMessage());
            return map;
        }
    }
}
