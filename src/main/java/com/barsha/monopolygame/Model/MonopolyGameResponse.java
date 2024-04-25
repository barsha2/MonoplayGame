package com.barsha.monopolygame.Model;

import java.util.List;

public class MonopolyGameResponse {
    private List<String>    errorList;
    private Object          apiResponse;
    
    public MonopolyGameResponse() {
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    public Object getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(Object apiResponse) {
        this.apiResponse = apiResponse;
    }

    
}
