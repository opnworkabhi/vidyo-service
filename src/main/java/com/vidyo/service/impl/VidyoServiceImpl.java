package com.vidyo.service.impl;

import com.vidyo.model.dto.VidyoScheduleDto;
import com.vidyo.model.request.ShowScheduledRoomRequest;
import com.vidyo.model.response.ShowScheduledRoomResponse;
import com.vidyo.repository.CustomVidyoScheduleRepository;
import com.vidyo.service.VidyoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@Service
public class VidyoServiceImpl implements VidyoService{
    //@Autowired
    //VidyoScheduleRepository vidyoScheduleRepository;

    @Autowired(required=true)
    CustomVidyoScheduleRepository customVidyoScheduleRepository;

    @Override
    public List<ShowScheduledRoomResponse> getRoomRequest(ShowScheduledRoomRequest showScheduledRoomRequest) {
        List<VidyoScheduleDto> roomList;
        // added option to fetch room for specific day else default current date

        //VidyoScheduleDto vidyoScheduleDto = showRoomMapper.mapScheduleRoom(showScheduledRoomRequest);

        //roomList  = vidyoScheduleRepository.findAll(); // find all rooms
        //System.out.println("room list ## "+roomList); // update with log

        roomList = customVidyoScheduleRepository.findByDate(Optional.of(showScheduledRoomRequest.getDate()).orElse(LocalDate.now())); // specific date or current date
        //roomList = customVidyoScheduleRepository.findByDate(showScheduledRoomRequest.getDate()); // specific date or current date

        List<ShowScheduledRoomResponse> roomResponseList = new ArrayList<>();
        for(VidyoScheduleDto rooms : roomList){
            ShowScheduledRoomResponse roomResponse = new ShowScheduledRoomResponse();
            roomResponse.setUserName(rooms.getUserName());
            roomResponse.setRoomName(rooms.getRoomName());
            roomResponse.setRoomURL(rooms.getRoomURL());
            roomResponse.setStatus(String.valueOf(HttpStatus.OK));
            roomResponse.setMessage(String.valueOf(HttpStatus.FOUND));
            roomResponseList.add(roomResponse);
        }
        System.out.println("room list after map in response ## "+roomResponseList);
        return roomResponseList;
    }
}
