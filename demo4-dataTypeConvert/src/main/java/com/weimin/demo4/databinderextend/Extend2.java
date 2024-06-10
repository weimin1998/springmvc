package com.weimin.demo4.databinderextend;

import com.weimin.demo4.databinderextend.bean.User;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

public class Extend2 {
    public static void main(String[] args) throws Exception {

        // 绑定器工厂，用FormattingConversionService 方式拓展，也就是spring 提供的那套底层接口

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthday", "1998|06|22");
        request.setParameter("address.name", "shanghai");

        User user = new User();

        // 第一步，创建一个 FormattingConversionService，加入自定义的 Formatter

        FormattingConversionService formattingConversionService = new FormattingConversionService();
        formattingConversionService.addFormatter(new MyFormatter("使用 FormattingConversionService 这种方式"));


        // 第二步，创建 ConfigurableWebBindingInitializer，设置 FormattingConversionService
        ConfigurableWebBindingInitializer configurableWebBindingInitializer = new ConfigurableWebBindingInitializer();
        configurableWebBindingInitializer.setConversionService(formattingConversionService);

        // 第三步。将 ConfigurableWebBindingInitializer 传给 ServletRequestDataBinderFactory
        ServletRequestDataBinderFactory servletRequestDataBinderFactory = new ServletRequestDataBinderFactory(null, configurableWebBindingInitializer);

        WebDataBinder webDataBinder = servletRequestDataBinderFactory.createBinder(new ServletWebRequest(request), user, "user");

        webDataBinder.bind(new ServletRequestParameterPropertyValues(request));

        System.out.println(user);
    }

}
