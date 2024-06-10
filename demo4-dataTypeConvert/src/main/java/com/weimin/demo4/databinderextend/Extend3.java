package com.weimin.demo4.databinderextend;

import com.weimin.demo4.databinderextend.bean.Mycontroller;
import com.weimin.demo4.databinderextend.bean.User;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.Collections;

public class Extend3 {
    public static void main(String[] args) throws Exception {


        // 如果 @InitBinder 和 ConversionService 都提供给 ServletRequestDataBinderFactory，会怎样？

        // @InitBinder 优先级高，
        // 如果没有提供@InitBinder 方式的转换器，再考虑 FormattingConversionService


        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthday", "1998|06|22");
        request.setParameter("address.name", "shanghai");

        User user = new User();

        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(new Mycontroller(), Mycontroller.class.getMethod("aaa", WebDataBinder.class));

        FormattingConversionService formattingConversionService = new FormattingConversionService();
        formattingConversionService.addFormatter(new MyFormatter("使用 FormattingConversionService 这种方式"));
        ConfigurableWebBindingInitializer configurableWebBindingInitializer = new ConfigurableWebBindingInitializer();
        configurableWebBindingInitializer.setConversionService(formattingConversionService);

        ServletRequestDataBinderFactory servletRequestDataBinderFactory = new ServletRequestDataBinderFactory(Collections.singletonList(invocableHandlerMethod), configurableWebBindingInitializer);
        WebDataBinder webDataBinder = servletRequestDataBinderFactory.createBinder(new ServletWebRequest(request), user, "user");

        webDataBinder.bind(new ServletRequestParameterPropertyValues(request));

        System.out.println(user);
    }
}
