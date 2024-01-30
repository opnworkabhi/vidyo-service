package com.vidyo.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateScheduledRoomResponse {
    private String userName;
    private String roomName;
    private String roomURL;
    private String status;
    private String error;
    private String message;
}
