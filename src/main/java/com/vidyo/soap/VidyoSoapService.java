package com.vidyo.soap;

import com.vidyo.model.request.DisconnectConferenceAllRequest;
import com.vidyo.model.request.LogInRequest;
import com.vidyo.model.request.CreateScheduledRoomRequest;
import com.vidyo.model.response.DisconnectConferenceAllResponse;
import com.vidyo.model.response.LogInResponse;
import com.vidyo.model.response.CreateScheduledRoomResponse;

public interface VidyoSoapService {

    LogInResponse logIn();

    CreateScheduledRoomResponse createScheduledRoom(CreateScheduledRoomRequest createScheduledRoomRequest);

    DisconnectConferenceAllResponse disconnectConferenceAll(DisconnectConferenceAllRequest disconnectConferenceAllRequest);
}
