package com.vidyo.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ShowScheduledRoomRequest {
    private LocalDate date;
}
