package com.clockbone.model;

import java.io.Serializable;

/**
 * Created by qinjun on 2016/3/16.
 */
public class Response<T> implements Serializable {

    protected T result;
    protected Boolean success;
    protected String errorCode;
    protected String errorMsg;

    public Response(String errorCode){
        this.success=false;
        this.errorCode=errorCode;

    }

    public Response(String errorCode,String errorMsg){
        this.success=false;
        this.errorCode=errorCode;
        this.errorMsg=errorMsg;
    }

    public Response(T result){
        this.success=true;
        this.result=result;
    }

    public Boolean isSuccess(){
        return this.success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                ", success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
