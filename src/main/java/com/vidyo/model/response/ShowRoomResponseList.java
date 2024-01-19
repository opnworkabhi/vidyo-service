package com.vidyo.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ShowRoomResponseList {
    private List<ShowScheduledRoomResponse> roomResponseList ;
}
