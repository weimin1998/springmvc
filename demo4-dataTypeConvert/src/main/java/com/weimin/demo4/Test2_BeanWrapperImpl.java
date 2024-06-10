package com.weimin.demo4;

import org.springframework.beans.BeanWrapperImpl;

import java.util.Date;

public class Test2_BeanWrapperImpl {
    public static void main(String[] args) {

        MyBean myBean = new MyBean();

        // 利用反射原理，给bean的属性赋值，bean中必须有set方法，否则报错
        // 如果赋值的时候发现类型不一致，则会自动类型转换
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(myBean);

        // string->int
        beanWrapper.setPropertyValue("a","10");
        beanWrapper.setPropertyValue("b","hello");
        // string->date
        beanWrapper.setPropertyValue("c","2023/01/01");

        System.out.println(myBean); // MyBean{a=10, b='hello', c=Sun Jan 01 00:00:00 CST 2023}

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
