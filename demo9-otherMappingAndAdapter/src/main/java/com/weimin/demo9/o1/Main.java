package com.weimin.demo9.o1;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext applicationContext = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

        // http://localhost:8080/c1
        // http://localhost:8080/c2
        // http://localhost:8080/c3
    }
}
