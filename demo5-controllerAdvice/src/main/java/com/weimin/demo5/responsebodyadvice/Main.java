package com.weimin.demo5.responsebodyadvice;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(WebConfig.class);


        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ServletInvocableHandlerMethod servletInvocableHandlerMethod = new ServletInvocableHandlerMethod(applicationContext.getBean(WebConfig.MyController.class), WebConfig.MyController.class.getMethod("user"));

        ServletRequestDataBinderFactory servletRequestDataBinderFactory = new ServletRequestDataBinderFactory(Collections.emptyList(), null);


        servletInvocableHandlerMethod.setDataBinderFactory(servletRequestDataBinderFactory);
        servletInvocableHandlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
        servletInvocableHandlerMethod.setHandlerMethodArgumentResolvers(getArgumentResolverComposite(applicationContext));
        servletInvocableHandlerMethod.setHandlerMethodReturnValueHandlers(getReturnValHandler(applicationContext));

        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        servletInvocableHandlerMethod.invokeAndHandle(new ServletWebRequest(request, response), modelAndViewContainer);

        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));

        applicationContext.close();

    }


    private static HandlerMethodArgumentResolverComposite getArgumentResolverComposite(AnnotationConfigApplicationContext applicationContext) {
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

    private static HandlerMethodReturnValueHandlerComposite getReturnValHandler(AnnotationConfigApplicationContext applicationContext) {
        // 添加 advice
        List<ControllerAdviceBean> annotatedBeans = ControllerAdviceBean.findAnnotatedBeans(applicationContext);
        List<Object> collect = annotatedBeans.stream().filter(b -> ResponseBodyAdvice.class.isAssignableFrom(b.getBeanType()))
                .collect(Collectors.toList());

        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        composite.addHandler(new ModelAndViewMethodReturnValueHandler());
        composite.addHandler(new ViewNameMethodReturnValueHandler());
        composite.addHandler(new ServletModelAttributeMethodProcessor(false));
        composite.addHandler(new HttpEntityMethodProcessor(Collections.singletonList(new MappingJackson2HttpMessageConverter())));
        composite.addHandler(new HttpHeadersReturnValueHandler());
        composite.addHandler(new RequestResponseBodyMethodProcessor(Collections.singletonList(new MappingJackson2HttpMessageConverter()), collect));
        composite.addHandler(new ServletModelAttributeMethodProcessor(true));
        return composite;
    }
}
