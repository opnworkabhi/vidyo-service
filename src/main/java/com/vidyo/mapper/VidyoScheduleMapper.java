package com.vidyo.mapper;

import com.vidyo.model.dto.VidyoScheduleDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class VidyoScheduleMapper {

    // do the mapping
    public VidyoScheduleDto mapScheduleRoom(String roomName, String userName, String roomURL){
        VidyoScheduleDto vidyoScheduleDto = new VidyoScheduleDto();
        vidyoScheduleDto.setRoomName(Optional.ofNullable(roomName).orElse("default"));
        vidyoScheduleDto.setUserName(Optional.ofNullable(userName).orElse("default"));
        vidyoScheduleDto.setRoomURL(Optional.ofNullable(roomURL).orElse(""));
        vidyoScheduleDto.setRoomStatus("Active"); // while creation mark status as active
        vidyoScheduleDto.setDate(LocalDate.now());
        return vidyoScheduleDto;
    }
}
