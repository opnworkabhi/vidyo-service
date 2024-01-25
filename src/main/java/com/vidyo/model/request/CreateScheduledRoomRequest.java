package com.vidyo.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateScheduledRoomRequest {
    private String userName;
    private String roomName;
}
