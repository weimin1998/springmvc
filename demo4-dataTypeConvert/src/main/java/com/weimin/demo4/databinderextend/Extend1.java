package com.weimin.demo4.databinderextend;

import com.weimin.demo4.databinderextend.bean.Mycontroller;
import com.weimin.demo4.databinderextend.bean.User;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.Collections;

public class Extend1 {
    public static void main(String[] args) throws Exception {

        // 绑定器工厂，用@InitBinder 方式拓展，也就是jdk提供的那套底层接口

        // 1.首先，提供一个Controller类，里面再提供一个@InitBinder注解标注的方法；
        // 方法没有返回值。方法名无所谓，方法参数必须是 WebDataBinder，在这个方法中，提供自定义的类型转换器 Formatter
        // 虽然提供的是Formatter类型，但是底层会适配成 PropertyEditor

        // 2.然后，我们要将 第一步准备好的方法传到 绑定器工厂 中，需要先封装成 InvocableHandlerMethod
        // 因为将来要反射调用方法，所以需要准备 控制器对象，以及方法对象
        // 然后传给 绑定器工厂

        // 3.然后通过 绑定器工厂 来创建绑定器，将请求中的参数 绑定到 bean 中


        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthday", "1998|06|22");
        request.setParameter("address.name", "shanghai");

        User user = new User();

        // 第一步，参考 Mycontroller
        // 第二步
        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(new Mycontroller(), Mycontroller.class.getMethod("aaa", WebDataBinder.class));


        ServletRequestDataBinderFactory servletRequestDataBinderFactory = new ServletRequestDataBinderFactory(Collections.singletonList(invocableHandlerMethod), null);

        // 第三步
        WebDataBinder webDataBinder = servletRequestDataBinderFactory.createBinder(new ServletWebRequest(request), user, "user");

        webDataBinder.bind(new ServletRequestParameterPropertyValues(request));

        System.out.println(user); // User{birthday=Mon Jun 22 00:00:00 CST 1998, address=Address{name='shanghai'}}
    }
}
