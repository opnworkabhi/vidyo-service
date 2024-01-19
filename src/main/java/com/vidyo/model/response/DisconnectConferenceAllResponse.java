package com.vidyo.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DisconnectConferenceAllResponse {
    private String notificationMsg;
    private String status;
    private String error;
    private String message;
}
