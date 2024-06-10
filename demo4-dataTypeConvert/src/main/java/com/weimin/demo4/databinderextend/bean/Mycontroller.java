package com.weimin.demo4.databinderextend.bean;

import com.weimin.demo4.databinderextend.MyFormatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public class Mycontroller {

    @InitBinder
    public void aaa(WebDataBinder webDataBinder) {
        // 在这里拓展 WebDataBinder的功能
        webDataBinder.addCustomFormatter(new MyFormatter("使用 @InitBinder 这种方式 "));
    }
}
