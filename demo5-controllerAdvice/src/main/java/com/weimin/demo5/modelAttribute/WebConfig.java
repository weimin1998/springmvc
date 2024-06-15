package com.weimin.demo5.modelAttribute;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class WebConfig {

    @ControllerAdvice
    static class MyControllerAdvice{
        @ModelAttribute("a")
        public String a(){
            return "aa";
        }
    }

    @Controller
    static class Controller1{
        @ResponseStatus(HttpStatus.OK)
        public ModelAndView foo(@ModelAttribute("u") User user){
            System.out.println("foo");
            System.out.println(user);
            return null;
        }

        @ModelAttribute("b")
        public String b(){
            return "bb";
        }
    }
    @Controller
    static class Controller2{
        @ModelAttribute("c")
        public String c(){
            return "cc";
        }
    }

    static class User{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

}
