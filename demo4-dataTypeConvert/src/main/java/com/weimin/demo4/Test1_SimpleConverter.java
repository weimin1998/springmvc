package com.weimin.demo4;

import org.springframework.beans.SimpleTypeConverter;

import java.util.Date;

public class Test1_SimpleConverter {
    public static void main(String[] args) {
        // 仅有类型转换的功能
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();

        // 比如，将string转为int
        Integer integer = simpleTypeConverter.convertIfNecessary("13", int.class);

        // 比如，将时间string转为date
        Date date = simpleTypeConverter.convertIfNecessary("1998/08/21", Date.class);
        System.out.println(integer); // 13
        System.out.println(date);// Fri Aug 21 00:00:00 CST 1998
    }
}
