package com.weimin.demo4;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import java.util.Date;

public class Test4_DataBinder {
    public static void main(String[] args) {
        MyBean myBean = new MyBean();

        // 数据绑定
        // 给bean的属性赋值
        DataBinder dataBinder = new DataBinder(myBean);
        // 至于，到底是通过set方法，还是直接field赋值，取决于下面这个布尔变量
        // directFieldAccess，如果为true，则通过field赋值，不需要set方法
        // 如果为false，则通过set赋值，需要提供set方法，否则，虽然不会报错，但是不会给属性赋值

        dataBinder.initDirectFieldAccess();// 成员变量赋值

        // 数据来源
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.add("a","10");
        pvs.add("b","hello");
        pvs.add("c","1999/10/10");
        dataBinder.bind(pvs);
        System.out.println(myBean);

    }
    static class MyBean{
        private int a;
        private String b;
        private Date c;

//        public int getA() {
//            return a;
//        }
//
//        public void setA(int a) {
//            this.a = a;
//        }
//
//        public String getB() {
//            return b;
//        }
//
//        public void setB(String b) {
//            this.b = b;
//        }
//
//        public Date getC() {
//            return c;
//        }
//
//        public void setC(Date c) {
//            this.c = c;
//        }

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
