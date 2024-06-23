package com.weimin.demo9.diy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.Controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
public class WebConfigDIY {
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet) {
        DispatcherServletRegistrationBean registrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }

    @Component
    static class MyHandlerMapping implements HandlerMapping {
        Map<String, Controller> mapping = new HashMap<>();
        @Autowired
        private ApplicationContext applicationContext;

        @Override
        public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
            String requestURI = request.getRequestURI();
            Controller controller = mapping.get(requestURI);
            if (controller == null) {
                return null;
            }
            return new HandlerExecutionChain(controller);
        }

        // 初始化的时候，就找到所有的控制器方法
        @PostConstruct
        public void init() {

            Map<String, Controller> map = applicationContext.getBeansOfType(Controller.class);
            Set<String> strings = map.keySet();
            for (String string : strings) {
                if (string.startsWith("/")) {
                    mapping.put(string, map.get(string));
                }
            }

            System.out.println("mapping: " + mapping);
        }
    }

    @Component
    static class MyHandlerAdapter implements HandlerAdapter {

        @Override
        public boolean supports(Object handler) {
            return handler instanceof Controller;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            if (handler instanceof Controller) {
                ModelAndView modelAndView = ((Controller) handler).handleRequest(request, response);

            }
            return null;
        }

        // 废弃
        @Override
        public long getLastModified(HttpServletRequest request, Object handler) {
            return -1;
        }
    }

    @Component("/c4")
    public static class Controller4 implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().print("this is c4");
            return null;
        }
    }

    @Component("/c5")
    public static class Controller5 implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().print("this is c5");
            return null;
        }
    }

    @Bean("/c6")
    public Controller controller6() {
        return new Controller() {
            @Override
            public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.getWriter().print("this is c6");
                return null;
            }
        };
    }

}
