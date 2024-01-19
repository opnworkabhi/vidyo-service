package com.vidyo.repository;

import com.vidyo.model.dto.VidyoScheduleDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomVidyoScheduleRepository extends MongoRepository<VidyoScheduleDto, String> {
    List<VidyoScheduleDto> findByDate(LocalDate date);
}
