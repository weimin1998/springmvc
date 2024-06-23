package com.weimin.demo9.diy;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class MainDiy {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext applicationContext = new AnnotationConfigServletWebServerApplicationContext(WebConfigDIY.class);

        // http://localhost:8080/c4
        // http://localhost:8080/c5
        // http://localhost:8080/c6
    }
}
