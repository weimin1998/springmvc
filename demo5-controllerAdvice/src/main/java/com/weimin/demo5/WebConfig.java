package com.weimin.demo5;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@Configuration
public class WebConfig {

    @ControllerAdvice
    static class MyControllerAdvice{

        @InitBinder
        public void binder4(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyFormatter("binder4"));
        }
    }

    @Controller
    static class Controller1{
        @InitBinder
        public void binder1(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyFormatter("binder1"));
        }


        public void m1(){

        }
    }

    @Controller
    static class Controller2{
        @InitBinder
        public void binder2(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyFormatter("binder2"));
        }

        @InitBinder
        public void binder3(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyFormatter("binder3"));
        }

        public void m2(){

        }
    }
}
