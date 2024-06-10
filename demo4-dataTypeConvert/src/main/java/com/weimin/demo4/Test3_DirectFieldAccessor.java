package com.weimin.demo4;

import org.springframework.beans.DirectFieldAccessor;

import java.util.Date;

public class Test3_DirectFieldAccessor {
    public static void main(String[] args) {

        MyBean myBean = new MyBean();

        // 和BeanWrapperImpl类似
        // 区别是不需要set方法，直接给field赋值
        DirectFieldAccessor directFieldAccessor = new DirectFieldAccessor(myBean);

        directFieldAccessor.setPropertyValue("a","10");
        directFieldAccessor.setPropertyValue("b","hello");
        directFieldAccessor.setPropertyValue("c","2023/01/01");

        System.out.println(myBean);

    }

    static class MyBean{
        private int a;
        private String b;
        private Date c;

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
