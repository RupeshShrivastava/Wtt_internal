package com.halodoc.omstests;

/**
 * @author praveenkumardn
 * This Enum class contains all the error codes and messages
 */
public enum ErrorCodesAndMessages {

    NOT_FOUND_GENERIC_EXCEPTION("404", "No products found");

    private String errCode;
    private String errMsg;
    private ErrorCodesAndMessages(String errCode, String errMsg){
        this.errCode = errCode ;
        this.errMsg = errMsg ;
    }
    public String errCode(){
        return this.errCode;
    }
    public String errMsg(){
        return this.errMsg ;
    }
}