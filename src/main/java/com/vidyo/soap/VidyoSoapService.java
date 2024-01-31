package com.vidyo.soap;

import com.vidyo.model.request.DisconnectConferenceAllRequest;
import com.vidyo.model.request.CreateScheduledRoomRequest;
import com.vidyo.model.response.DisconnectConferenceAllResponse;
import com.vidyo.model.response.LogInResponse;
import com.vidyo.model.response.CreateScheduledRoomResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface VidyoSoapService {

    LogInResponse logIn(String basicAuth);

    CreateScheduledRoomResponse createScheduledRoom(CreateScheduledRoomRequest createScheduledRoomRequest, String basicAuth);

    DisconnectConferenceAllResponse disconnectConferenceAll(DisconnectConferenceAllRequest disconnectConferenceAllRequest, String request);
}
