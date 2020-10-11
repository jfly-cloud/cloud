package com.jfly.api.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 返回成功
     */
    public ResponseEntity<String> success() {
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * 返回失败消息
     */
    public ResponseEntity<String>  error() {
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 返回成功消息
     */
    public ResponseEntity<String> success(String message) {
       return new ResponseEntity<String>(message,HttpStatus.OK);
    }

    /**
     * 返回失败消息
     */
    public ResponseEntity<String>  error(String message) {
        return new ResponseEntity<String>(message,HttpStatus.BAD_REQUEST);
    }

    /**
     * 返回错误码消息
     */
    public ResponseEntity<String> error(Integer code, String message) {
        return ResponseEntity.status(code).body(message);
    }


}
