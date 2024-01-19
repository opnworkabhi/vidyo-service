package com.vidyo.service;

import com.vidyo.model.request.ShowScheduledRoomRequest;
import com.vidyo.model.response.ShowScheduledRoomResponse;

import java.util.List;

public interface VidyoService {

    List<ShowScheduledRoomResponse> getRoomRequest(ShowScheduledRoomRequest showScheduledRoomRequest);
}
