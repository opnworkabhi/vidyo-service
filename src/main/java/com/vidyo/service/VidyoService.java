package com.vidyo.service;

import com.vidyo.model.response.ShowScheduledRoomResponse;

import java.time.LocalDate;
import java.util.List;

public interface VidyoService {

    List<ShowScheduledRoomResponse> getRoomRequest(LocalDate date);
}
