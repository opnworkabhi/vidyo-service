package com.vidyo.model.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("vidyo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VidyoScheduleDto {
    @Id
    private String roomName;
    private String userName;
    private String roomURL;
    private String roomStatus;
    private LocalDate date;
}
