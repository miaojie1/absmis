package com.absmis.exception;

public class ErrorInfo<T> {
    public  static  final Integer OK=0;
    public  static  final  Integer ERROR =100;
    private Integer code;//消息类型
    private String message;//消息内容
    private String url;//请求的url
    private T data;//请求返回的数据

    public ErrorInfo(Integer code, String message){
        this.code = code;
        this.message = message;

    }

    public ErrorInfo(){

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
