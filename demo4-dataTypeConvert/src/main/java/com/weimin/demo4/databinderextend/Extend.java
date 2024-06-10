package com.weimin.demo4.databinderextend;

import com.weimin.demo4.databinderextend.bean.User;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

public class Extend {
    public static void main(String[] args) throws Exception {

        // 从本例开始，学习自定义类型转换
        // 比如，我想把这种格式的时间字符串，转为date 并 绑定的bean的date属性：1998|06|22
        // 但是，这种格式默认是不支持转换的，因此需要自定义类型转换器

        // 而自定义类型转换，有两种方式，也就是前面提到的两套底层接口：
        // ConversionService
        // PropertyEditorRegistry
        // 详见oneNote



        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthday", "1998|06|22");
        request.setParameter("address.name", "shanghai");

        User user = new User();

        // 同时，这里的DataBinder，就不手动new，而是通过工厂创建
        ServletRequestDataBinder binder = new ServletRequestDataBinder(user);

        binder.bind(new ServletRequestParameterPropertyValues(request));

        System.out.println(user);
    }
}
