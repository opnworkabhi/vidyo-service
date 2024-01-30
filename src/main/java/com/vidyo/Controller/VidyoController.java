package com.vidyo.Controller;

import com.vidyo.model.request.DisconnectConferenceAllRequest;
import com.vidyo.model.request.CreateScheduledRoomRequest;
import com.vidyo.model.response.DisconnectConferenceAllResponse;
import com.vidyo.model.response.LogInResponse;
import com.vidyo.model.response.CreateScheduledRoomResponse;
import com.vidyo.model.response.ShowScheduledRoomResponse;
import com.vidyo.service.VidyoService;
import com.vidyo.soap.VidyoSoapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class VidyoController {

    @Autowired
    private VidyoSoapService vidyoSoapService;

    @Autowired
    private VidyoService vidyoService;

    @RequestMapping("/")
    String hello() {
        return "Welcome to Vidyo Application";
    }

    @PostMapping("/login")
    public LogInResponse logIn() {
        return vidyoSoapService.logIn();
    }

    // call db and check if pre decided room name has been already created then get it from db without calling portal API
    // else call portal API(CreateScheduledRoomRequest) to get meeting url
    // final persist room name entered in UI along with meeting url received from portal API
    @PostMapping("/createScheduledRoom")
    public CreateScheduledRoomResponse createScheduledRoom(@RequestBody CreateScheduledRoomRequest createScheduledRoomRequest){
        return vidyoSoapService.createScheduledRoom(createScheduledRoomRequest);
    }

    // db call and to get scheduled rooms name list created for day and show
    @GetMapping("/showScheduledRoomList")
    //public List<ShowScheduledRoomResponse> showScheduledRoomList(@RequestBody ShowScheduledRoomRequest showScheduledRoomRequest){
    public List<ShowScheduledRoomResponse> showScheduledRoomList(@RequestParam("date") LocalDate date){
        return vidyoService.getRoomRequest(date);
    }

    // UI will send room key(received in CreateScheduledRoomRequest URL) and room name
    // basis of room key will call portal API (GetEntityByRoomKeyRequest) to get entityID
    // here entityID is confrenceID for portal API (disconnectConferenceAllRequest)
    // so pass that entityID in portal API (disconnectConferenceAllRequest) call to disconnect
    @PostMapping("/disconnectConferenceAll")
    public DisconnectConferenceAllResponse disconnectConferenceAll(@RequestBody DisconnectConferenceAllRequest disconnectConferenceAllRequest){
        return vidyoSoapService.disconnectConferenceAll(disconnectConferenceAllRequest); // call GetEntityByRoomKeyRequest internally
    }
}