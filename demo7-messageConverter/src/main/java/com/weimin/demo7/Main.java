package com.weimin.demo7;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        test1();
        test2();
        test3();
    }

    // java bean 转 json
    private static void test1() throws IOException {
        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if (converter.canWrite(User.class, MediaType.APPLICATION_JSON)){
            converter.write(new User("tom",20), MediaType.APPLICATION_JSON, outputMessage);

            System.out.println(outputMessage.getBodyAsString());
        }

    }

    // java bean 转 xml
    private static void test2() throws IOException {
        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();

        MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter();
        if (converter.canWrite(User.class, MediaType.APPLICATION_XML)){
            converter.write(new User("tom",20), MediaType.APPLICATION_XML, outputMessage);

            System.out.println(outputMessage.getBodyAsString());
        }

    }

    private static void test3() throws IOException {
        MockHttpInputMessage inputMessage = new MockHttpInputMessage("{\"name\":\"tom\",\"age\":20}".getBytes(StandardCharsets.UTF_8));
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if (converter.canRead(User.class, MediaType.APPLICATION_JSON)){
            Object read = converter.read(User.class, inputMessage);

            System.out.println(read);
        }

    }
}
