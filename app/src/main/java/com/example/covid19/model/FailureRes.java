package com.example.covid19.model;

import kotlin.Result;

public class FailureRes {

    private boolean hasError ;
    private String errorMsg ;

    public FailureRes() {
    }

    public FailureRes(boolean hasError, String errorMsg){
        this.hasError = hasError ;
        this.errorMsg = errorMsg ;
    }

    public boolean isHasError() {
        return hasError;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
