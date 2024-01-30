package com.vidyo.repository;

import com.vidyo.model.dto.VidyoScheduleDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VidyoScheduleRepository extends MongoRepository<VidyoScheduleDto, String> {
}
