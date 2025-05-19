package com.kachalova.fileprocessing.mapper;

import com.kachalova.fileprocessing.dto.PersonalDataDTO;
import com.kachalova.fileprocessing.entity.PersonalDataEntity;
import com.kachalova.fileprocessing.kafka.KafkaData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalDataMapper {

    PersonalDataEntity toEntity(PersonalDataDTO dto);

    KafkaData toKafkaModel(PersonalDataEntity entity);
    PersonalDataDTO toDto(PersonalDataEntity entity);
}
