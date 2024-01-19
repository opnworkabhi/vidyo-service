package com.vidyo.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogInResponse {
    private String userId;
    private String status;
    private String error;
    private String message;
}
