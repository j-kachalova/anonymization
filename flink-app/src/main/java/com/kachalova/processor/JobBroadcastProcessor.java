package com.kachalova.processor;

import com.kachalova.model.AnonymizationRuleDto;
import com.kachalova.model.JobResponseDto;
import com.kachalova.model.PersonalData;
import com.kachalova.util.AnonymizationUtil;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

public class JobBroadcastProcessor extends BroadcastProcessFunction<PersonalData, JobResponseDto, PersonalData> {

    private final MapStateDescriptor<String, JobResponseDto> jobDescriptor;

    public JobBroadcastProcessor(MapStateDescriptor<String, JobResponseDto> jobDescriptor) {
        this.jobDescriptor = jobDescriptor;
    }

    @Override
    public void processBroadcastElement(JobResponseDto job, Context ctx, Collector<PersonalData> out) throws Exception {
        ctx.getBroadcastState(jobDescriptor).put("current", job);
    }

    @Override
    public void processElement(PersonalData data, ReadOnlyContext ctx, Collector<PersonalData> out) throws Exception {
        JobResponseDto job = ctx.getBroadcastState(jobDescriptor).get("current");
        if (job != null) {
            for (AnonymizationRuleDto rule : job.getRuleSet().getRules()) {
                data = AnonymizationUtil.apply(data, rule);
            }
        }
        out.collect(data);
    }
}
