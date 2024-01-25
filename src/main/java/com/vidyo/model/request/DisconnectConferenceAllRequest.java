package com.vidyo.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DisconnectConferenceAllRequest {
    private String roomKey;
    private String roomName;
}
