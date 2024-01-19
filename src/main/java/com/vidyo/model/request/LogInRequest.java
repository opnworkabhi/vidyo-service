package com.vidyo.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogInRequest {
    private String clientType;
    private String userRole;
    private String userId;
    private String password;

}
