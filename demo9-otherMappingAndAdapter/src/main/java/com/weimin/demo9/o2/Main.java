package com.weimin.demo9.o2;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext applicationContext = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

        // http://localhost:8080/r1
        // http://localhost:8080/r2
    }
}
