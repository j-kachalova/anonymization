package com.kachalova.processor;

import com.kachalova.model.AnonymizationRuleDto;
import com.kachalova.model.JobResponseDto;
import com.kachalova.model.PersonalData;
import com.kachalova.util.AnonymizationUtil;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

public class JobKeyedBroadcastProcessor extends KeyedBroadcastProcessFunction<String, PersonalData, JobResponseDto, PersonalData> {

    private final MapStateDescriptor<String, JobResponseDto> jobDescriptor;

    public JobKeyedBroadcastProcessor(MapStateDescriptor<String, JobResponseDto> jobDescriptor) {
        this.jobDescriptor = jobDescriptor;
    }

    @Override
    public void processBroadcastElement(JobResponseDto job, Context ctx, Collector<PersonalData> out) throws Exception {
        ctx.getBroadcastState(jobDescriptor).put("current", job);
        System.out.println("📡 Получена новая джоба: " + job.getId() + " -> топик: " + job.getInputTopic());
    }

    @Override
    public void processElement(PersonalData data, ReadOnlyContext ctx, Collector<PersonalData> out) throws Exception {
        JobResponseDto job = ctx.getBroadcastState(jobDescriptor).get("current");
        if (job != null && job.getInputTopic().equals(data.getSourceTopic())) {
            System.out.println("📥 Flink получил данные из топика: " + data.getSourceTopic());
            System.out.println("➡️ Обрабатываем PersonalData: " + data);

            for (AnonymizationRuleDto rule : job.getRuleSet().getRules()) {
                data = AnonymizationUtil.apply(data, rule);
            }
            out.collect(data);
        } else {
            System.out.println("⛔ Пропущены данные из другого топика: " + data.getSourceTopic());
        }
    }
}
