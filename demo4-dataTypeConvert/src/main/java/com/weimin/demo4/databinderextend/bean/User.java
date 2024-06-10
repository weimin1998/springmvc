package com.weimin.demo4.databinderextend.bean;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class User {

    @DateTimeFormat(pattern = "yyyy|MM|dd")
    private Date birthday;
    private Address address;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "birthday=" + birthday +
                ", address=" + address +
                '}';
    }
}
