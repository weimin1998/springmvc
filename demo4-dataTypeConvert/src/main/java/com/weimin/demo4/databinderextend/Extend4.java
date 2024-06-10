package com.weimin.demo4.databinderextend;

import com.weimin.demo4.databinderextend.bean.User;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

public class Extend4 {
    public static void main(String[] args) throws Exception {

        // 使用默认的类型转换器，不用程序员提供自定义的类型转换器
        // 需要在 bean的属性上提供 @DateTimeFormat(pattern = "yyyy|MM|dd")，指定日期格式

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthday", "1998|06|22");
        request.setParameter("address.name", "shanghai");

        User user = new User();


        DefaultFormattingConversionService defaultFormattingConversionService = new DefaultFormattingConversionService();
        ConfigurableWebBindingInitializer configurableWebBindingInitializer = new ConfigurableWebBindingInitializer();

        configurableWebBindingInitializer.setConversionService(defaultFormattingConversionService);

        ServletRequestDataBinderFactory servletRequestDataBinderFactory = new ServletRequestDataBinderFactory(null, configurableWebBindingInitializer);
        WebDataBinder webDataBinder = servletRequestDataBinderFactory.createBinder(new ServletWebRequest(request), user, "user");

        webDataBinder.bind(new ServletRequestParameterPropertyValues(request));

        System.out.println(user);
    }
}
