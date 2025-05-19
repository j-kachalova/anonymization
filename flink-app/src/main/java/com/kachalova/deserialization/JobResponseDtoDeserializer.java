package com.kachalova.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.model.JobResponseDto;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class JobResponseDtoDeserializer implements DeserializationSchema<JobResponseDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JobResponseDto deserialize(byte[] message) throws IOException {
        return objectMapper.readValue(message, JobResponseDto.class);
    }

    @Override
    public boolean isEndOfStream(JobResponseDto nextElement) {
        return false;
    }

    @Override
    public TypeInformation<JobResponseDto> getProducedType() {
        return TypeInformation.of(JobResponseDto.class);
    }
}
