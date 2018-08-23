package com.sundy.share.dto;

import com.sundy.share.enums.ResultCode;

import java.io.Serializable;

/**
 * 响应对象
 *
 * @author plus
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 9131899105712058126L;

    private String message;

    private String code;

    private T data;

    private Result() {

    }

    public static <T> Result<T> success(T data) {

        Result<T> tResult = new Result<>();

        tResult.setCode(ResultCode.SUCCESS.getCode());

        tResult.setMessage(ResultCode.SUCCESS.getMessage());

        tResult.setData(data);

        return tResult;
    }

    public static <T> Result<T> success(String code, String message) {

        Result<T> tResult = new Result<>();

        tResult.setCode(code);

        tResult.setMessage(message);

        return tResult;
    }

    public static <T> Result<T> failure() {

        Result<T> tResult = new Result<>();

        tResult.setCode(ResultCode.SERVER_ERROR.getCode());

        tResult.setMessage(ResultCode.SERVER_ERROR.getMessage());

        return tResult;
    }

    public static <T> Result<T> failure(String code, String message) {

        Result<T> tResult = new Result<>();

        tResult.setCode(code);

        tResult.setMessage(message);

        return tResult;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
