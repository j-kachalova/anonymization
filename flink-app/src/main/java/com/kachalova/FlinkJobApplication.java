package com.kachalova;

import com.kachalova.model.JobResponseDto;
import com.kachalova.model.PersonalData;
import com.kachalova.processor.JobKeyedBroadcastProcessor;
import com.kachalova.sink.KafkaResultSinkProvider;
import com.kachalova.source.KafkaSourceProvider;
import com.kachalova.util.JsonUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkJobApplication {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        MapStateDescriptor<String, JobResponseDto> jobDescriptor =
                new MapStateDescriptor<>("JobBroadcastState", BasicTypeInfo.STRING_TYPE_INFO, TypeInformation.of(JobResponseDto.class));

        BroadcastStream<JobResponseDto> jobStream = env
                .fromSource(KafkaSourceProvider.createJobKafkaSource(), WatermarkStrategy.noWatermarks(), "Job Source")
                .broadcast(jobDescriptor);

        KeyedStream<PersonalData, String> dataStream = env
                .fromSource(KafkaSourceProvider.createDataKafkaSourceWildcard(), WatermarkStrategy.noWatermarks(), "Data Source")
                .map(json -> JsonUtil.fromJson(json, PersonalData.class))
                .keyBy(PersonalData::getUserId);

        dataStream
                .connect(jobStream)
                .process(new JobKeyedBroadcastProcessor(jobDescriptor))
                .map(JsonUtil::toJson)
                .print();
             //   .sinkTo(KafkaResultSinkProvider.createKafkaSink("job-commands-stop"));

        env.execute("Flink Dynamic Anonymization Job");
    }
}
