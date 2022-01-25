package com.example.pojo;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
