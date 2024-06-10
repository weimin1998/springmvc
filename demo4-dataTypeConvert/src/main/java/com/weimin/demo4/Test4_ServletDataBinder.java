package com.weimin.demo4;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import java.util.Date;

// 上一个例子，是在非web环境下使用dataBinder，这里看在web环境下如何使用dataBinder
// 这里需要使用DataBinder的子类 ServletRequestDataBinder
// MutablePropertyValues 数据来源，也需要换成web环境下的子类 ServletRequestParameterPropertyValues
public class Test4_ServletDataBinder {
    public static void main(String[] args) {
        MyBean myBean = new MyBean();

        // 将请求参数封装到javabean对象中
        ServletRequestDataBinder servletRequestDataBinder = new ServletRequestDataBinder(myBean);
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("a","10");
        request.setParameter("b","hello");
        request.setParameter("c","1998/06/21");

        // 这里就把request中的参数，绑定到bean的属性中
        servletRequestDataBinder.bind(new ServletRequestParameterPropertyValues(request));
        System.out.println(myBean);// MyBean{a=10, b='hello', c=Sun Jun 21 00:00:00 CST 1998}
    }

    static class MyBean{
        private int a;
        private String b;
        private Date c;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public Date getC() {
            return c;
        }

        public void setC(Date c) {
            this.c = c;
        }

        @Override
        public String toString() {
            return "MyBean{" +
                    "a=" + a +
                    ", b='" + b + '\'' +
                    ", c=" + c +
                    '}';
        }
    }
}
