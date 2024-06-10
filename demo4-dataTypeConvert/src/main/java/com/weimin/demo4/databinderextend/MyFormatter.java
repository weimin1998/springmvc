package com.weimin.demo4.databinderextend;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyFormatter implements Formatter<Date> {

    private final String msg;

    public MyFormatter(String msg) {
        this.msg = msg;
    }

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        System.out.println(msg);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy|MM|dd");
        return simpleDateFormat.parse(text);
    }

    @Override
    public String print(Date object, Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy|MM|dd");
        return simpleDateFormat.format(object);
    }
}
