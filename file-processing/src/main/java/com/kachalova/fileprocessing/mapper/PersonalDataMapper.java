package com.kachalova.fileprocessing.mapper;

import com.kachalova.fileprocessing.dto.PersonalDataDTO;
import com.kachalova.fileprocessing.entity.PersonalDataEntity;
import com.kachalova.fileprocessing.kafka.PersonalData;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface PersonalDataMapper {

    PersonalDataEntity toEntity(PersonalDataDTO dto);

    PersonalData toKafkaModel(PersonalDataEntity entity);
}
