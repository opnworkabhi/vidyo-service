package com.vidyo.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShowScheduledRoomResponse {
    private String roomName;
    private String userName;
    private String roomURL;
    private String roomStatus;
    private String status;
    private String error;
    private String message;
}
