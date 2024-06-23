package com.weimin.demo5.exceptionHandler;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.nio.charset.StandardCharsets;

public class AdviceMain {
    public static void main(String[] args) throws NoSuchMethodException {
        // 本案例演示，@ControllerAdvice 和 @ExceptionHandler 全局异常处理


        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = applicationContext.getBean(ExceptionHandlerExceptionResolver.class);

        HandlerMethod handlerMethod = new HandlerMethod(new Controller05(), Controller05.class.getMethod("foo"));

        Exception exception = new Exception("e1");

        ModelAndView modelAndView = exceptionHandlerExceptionResolver.resolveException(mockHttpServletRequest, mockHttpServletResponse, handlerMethod, exception);

        System.out.println("==========================");
        System.out.println(new String(mockHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8));
        System.out.println("==========================");
    }

    static class Controller05 {
        public void foo() {
        }
    }
}
