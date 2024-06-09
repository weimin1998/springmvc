package com.weimin.demo2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);


        // 模拟一个request
        MultipartHttpServletRequest request = mockHttpServletRequest();
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);

        // 准备一个HandlerMethod
        HandlerMethod handlerMethod = new HandlerMethod(new ArgController(), ArgController.class.getMethod("args", String.class, String.class, int.class, String.class, MultipartFile.class, int.class, String.class, String.class, String.class, HttpServletRequest.class, User.class, User.class, User.class));

        // 数据绑定, 类型转换
        // @RequestParam int age,  String to int
        //DefaultDataBinderFactory defaultDataBinderFactory = new DefaultDataBinderFactory(null);
        ServletRequestDataBinderFactory defaultDataBinderFactory = new ServletRequestDataBinderFactory(null, null);

        // mavContainer，存储model中间结果
        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();


        // 测试参数解析
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        for (MethodParameter methodParameter : methodParameters) {
            String annotations = Arrays.stream(methodParameter.getParameterAnnotations()).map(a -> a.annotationType().getSimpleName()).collect(Collectors.joining());
            String s = annotations.length() > 0 ? " @" + annotations + " " : " ";
            methodParameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());

            // useDefaultResolution： 表示必须有@RequestParam
            RequestParamMethodArgumentResolver requestParamMethodArgumentResolver1 = new RequestParamMethodArgumentResolver(applicationContext.getBeanFactory(), false);// 必须有@RequestParam
            RequestParamMethodArgumentResolver requestParamMethodArgumentResolver2 = new RequestParamMethodArgumentResolver(applicationContext.getBeanFactory(), true);
            PathVariableMethodArgumentResolver pathVariableMethodArgumentResolver = new PathVariableMethodArgumentResolver();
            RequestHeaderMethodArgumentResolver requestHeaderMethodArgumentResolver = new RequestHeaderMethodArgumentResolver(applicationContext.getBeanFactory());
            ServletCookieValueMethodArgumentResolver servletCookieValueMethodArgumentResolver = new ServletCookieValueMethodArgumentResolver(applicationContext.getBeanFactory());
            ExpressionValueMethodArgumentResolver expressionValueMethodArgumentResolver = new ExpressionValueMethodArgumentResolver(applicationContext.getBeanFactory());
            ServletRequestMethodArgumentResolver servletRequestMethodArgumentResolver = new ServletRequestMethodArgumentResolver();
            ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor1 = new ServletModelAttributeMethodProcessor(false);// 必须有@ModelAttribute
            ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor2 = new ServletModelAttributeMethodProcessor(true);
            RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = new RequestResponseBodyMethodProcessor(Arrays.asList(new MappingJackson2HttpMessageConverter()));


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


            if (composite.supportsParameter(methodParameter)) {
                Object o = composite.resolveArgument(methodParameter, modelAndViewContainer, servletWebRequest, defaultDataBinderFactory);
                // System.out.println(o.getClass());
                System.out.println("[" + methodParameter.getParameterIndex() + "]" + s + methodParameter.getParameterType().getSimpleName() + " " + methodParameter.getParameterName() + "-->" + o);

                System.out.println("model data: " + modelAndViewContainer.getModel());
            } else {
                System.out.println("[" + methodParameter.getParameterIndex() + "]" + s + methodParameter.getParameterType().getSimpleName() + " " + methodParameter.getParameterName());
            }
        }
    }

    public static MultipartHttpServletRequest mockHttpServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("username", "zhangsan");
        request.setParameter("name", "zhangsan");
        request.setParameter("age", "22");
        request.setParameter("str", "test str");

        request.addPart(new MockPart("file", "abc", "hello".getBytes(StandardCharsets.UTF_8)));
        Map<String, String> map = new AntPathMatcher().extractUriTemplateVariables("/test/{id}", "/test/123");
        System.out.println(map);
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, map);

        request.setContentType("application/json");

        request.setCookies(new Cookie("token", "123456"));

        request.setContent("{\"name\":\"lisi\",\"age\":20}".getBytes(StandardCharsets.UTF_8));

        return new StandardServletMultipartResolver().resolveMultipart(request);
    }

    static class ArgController {
        public void args(
                @RequestParam("username") String name,
                String str,
                @RequestParam int age,
                @RequestParam(value = "home", defaultValue = "${JAVA_HOME}") String home,
                @RequestParam("file") MultipartFile file,
                @PathVariable("id") int id,
                @RequestHeader("Content-Type") String header,
                @CookieValue("token") String token,
                @Value("${JAVA_HOME}") String homevalue,
                HttpServletRequest request,
                @ModelAttribute("abc") User user1, // 会将数据放到model中： abc=User{name='zhangsan', age=22}
                User user2, // 会将数据放到model中: user=User{name='zhangsan', age=22}
                @RequestBody User user3
        ) {}
    }
}
