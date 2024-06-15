package com.weimin.demo5.modelAttribute;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.lang.reflect.Method;
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws Exception {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(WebConfig.class);

        // RequestMappingHandlerAdapter 会 解析加在方法上的@ModelAttribute
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        requestMappingHandlerAdapter.setApplicationContext(applicationContext);
        requestMappingHandlerAdapter.afterPropertiesSet();

        Method getModelFactory = RequestMappingHandlerAdapter.class.getDeclaredMethod("getModelFactory", HandlerMethod.class, WebDataBinderFactory.class);
        getModelFactory.setAccessible(true);




        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name","tom");

        ServletInvocableHandlerMethod servletInvocableHandlerMethod = new ServletInvocableHandlerMethod(new WebConfig.Controller1(), WebConfig.Controller1.class.getMethod("foo", WebConfig.User.class));

        ServletRequestDataBinderFactory servletRequestDataBinderFactory = new ServletRequestDataBinderFactory(null, null);

        servletInvocableHandlerMethod.setDataBinderFactory(servletRequestDataBinderFactory);
        servletInvocableHandlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
        servletInvocableHandlerMethod.setHandlerMethodArgumentResolvers(getArgumentResolverComposite(applicationContext));


        // 控制器方法运行过程中产生的模型数据，会存放到 这个ModelAndViewContainer
        // 控制器方法参数，参数要想加入ModelAndViewContainer中，只有加@ModelAttribute注解，或者不加其他任何注解
        // 对应的参数解析器 ServletModelAttributeMethodProcessor
        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();

        ModelFactory modelFactory = (ModelFactory) getModelFactory.invoke(requestMappingHandlerAdapter, servletInvocableHandlerMethod, servletRequestDataBinderFactory);

        modelFactory.initModel(new ServletWebRequest(request), modelAndViewContainer, servletInvocableHandlerMethod);

        servletInvocableHandlerMethod.invokeAndHandle(new ServletWebRequest(request), modelAndViewContainer);

        System.out.println(modelAndViewContainer.getModel());

        applicationContext.close();

    }


    private static HandlerMethodArgumentResolverComposite getArgumentResolverComposite(AnnotationConfigApplicationContext applicationContext){
        RequestParamMethodArgumentResolver requestParamMethodArgumentResolver1 = new RequestParamMethodArgumentResolver(applicationContext.getBeanFactory(), false);// 必须有@RequestParam
        PathVariableMethodArgumentResolver pathVariableMethodArgumentResolver = new PathVariableMethodArgumentResolver();
        RequestHeaderMethodArgumentResolver requestHeaderMethodArgumentResolver = new RequestHeaderMethodArgumentResolver(applicationContext.getBeanFactory());
        ServletCookieValueMethodArgumentResolver servletCookieValueMethodArgumentResolver = new ServletCookieValueMethodArgumentResolver(applicationContext.getBeanFactory());
        ExpressionValueMethodArgumentResolver expressionValueMethodArgumentResolver = new ExpressionValueMethodArgumentResolver(applicationContext.getBeanFactory());
        ServletRequestMethodArgumentResolver servletRequestMethodArgumentResolver = new ServletRequestMethodArgumentResolver();
        ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor1 = new ServletModelAttributeMethodProcessor(false);// 必须有@ModelAttribute
        RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = new RequestResponseBodyMethodProcessor(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor2 = new ServletModelAttributeMethodProcessor(true);
        RequestParamMethodArgumentResolver requestParamMethodArgumentResolver2 = new RequestParamMethodArgumentResolver(applicationContext.getBeanFactory(), true);


        // 组合
        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolvers(
                requestParamMethodArgumentResolver1,
                pathVariableMethodArgumentResolver,
                requestHeaderMethodArgumentResolver,
                servletCookieValueMethodArgumentResolver,
                expressionValueMethodArgumentResolver,
                servletRequestMethodArgumentResolver,
                servletModelAttributeMethodProcessor1,
                requestResponseBodyMethodProcessor,
                servletModelAttributeMethodProcessor2,
                requestParamMethodArgumentResolver2
        );

        return composite;
    }
}
