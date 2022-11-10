package com.akhan.nomadsocialnetworkservice.service;

import org.springframework.stereotype.Service;

import com.akhan.nomadsocialnetworkservice.model.ResponseObject;

@Service
public class ResponseObjectService {
    
    public ResponseObject getResponseObject(String status, String message, Object payload){
        return new ResponseObject(status, message, payload);
    }

}
