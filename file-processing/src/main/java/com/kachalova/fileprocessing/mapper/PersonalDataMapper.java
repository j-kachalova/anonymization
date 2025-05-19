package com.kachalova.fileprocessing.mapper;

import com.kachalova.fileprocessing.dto.PersonalDataDTO;
import com.kachalova.fileprocessing.entity.PersonalDataEntity;
import com.kachalova.fileprocessing.kafka.PersonalData;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface PersonalDataMapper {

    public PersonalDataEntity toEntity(PersonalDataDTO dto);

    public PersonalData toKafkaModel(PersonalDataEntity entity);
}
