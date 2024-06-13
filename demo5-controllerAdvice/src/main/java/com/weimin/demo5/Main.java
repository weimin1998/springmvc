package com.weimin.demo5;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        /**
         * @ControllerAdvice 中定义的 initBinder，在RequestMappingHandlerAdapter 初始化的时候就会被解析，放到集合中；
         * 而 @controller 中的initBinder，在controller中任一目标方法被执行之前才会解析，然后放到集合中
         *
         *
         * 初始化，可以通过调用 afterPropertiesSet
         *
         * 执行某个目标方法，流程比较多，而和解析 initBinder相关的方法就是 getDataBinderFactory 方法
         *
         */
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(WebConfig.class);

        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();

        requestMappingHandlerAdapter.setApplicationContext(applicationContext);
        requestMappingHandlerAdapter.afterPropertiesSet();
        System.out.println("requestMappingHandlerAdapter 初始化完成");

        showBindMethod(requestMappingHandlerAdapter);

        Method getDataBinderFactory = RequestMappingHandlerAdapter.class.getDeclaredMethod("getDataBinderFactory", HandlerMethod.class);
        getDataBinderFactory.setAccessible(true);

        System.out.println("模拟调用controller1中的方法");
        getDataBinderFactory.invoke(requestMappingHandlerAdapter, new HandlerMethod(new WebConfig.Controller1(), WebConfig.Controller1.class.getMethod("m1")));

        showBindMethod(requestMappingHandlerAdapter);


        System.out.println("模拟调用controller2中的方法");
        getDataBinderFactory.invoke(requestMappingHandlerAdapter, new HandlerMethod(new WebConfig.Controller2(), WebConfig.Controller2.class.getMethod("m2")));

        showBindMethod(requestMappingHandlerAdapter);
    }

    @SuppressWarnings("all")
    private static void showBindMethod(RequestMappingHandlerAdapter requestMappingHandlerAdapter) throws NoSuchFieldException, IllegalAccessException {
        Field initBinderAdviceCache = RequestMappingHandlerAdapter.class.getDeclaredField("initBinderAdviceCache");
        initBinderAdviceCache.setAccessible(true);

        Map<ControllerAdviceBean, Set<Method>> globalMap = (Map<ControllerAdviceBean, Set<Method>>) initBinderAdviceCache.get(requestMappingHandlerAdapter);

        System.out.println("ControllerAdvice中的 InitBinder");
        System.out.println(globalMap);



        Field initBinderCache = RequestMappingHandlerAdapter.class.getDeclaredField("initBinderCache");
        initBinderCache.setAccessible(true);

        Map<Class<?>, Set<Method>> controllerMap = (Map<Class<?>, Set<Method>>) initBinderCache.get(requestMappingHandlerAdapter);

        System.out.println("Controller中的 InitBinder");
        System.out.println(controllerMap);

    }
}
