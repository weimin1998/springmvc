package com.weimin.demo5.responsebodyadvice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class R {

    private int code;
    private String msg;
    private Object data;

    @JsonCreator
    private R(@JsonProperty("code") int code, @JsonProperty("data")Object data) {
        this.code = code;
        this.data = data;
    }

    private R(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static R ok(Object data) {
        return new R(200, data);
    }
}
