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
        // Сохраняем джобу по inputTopic (можно и по job.getId() при необходимости)
        ctx.getBroadcastState(jobDescriptor).put(job.getInputTopic(), job);
        System.out.println("📡 Получена новая джоба: " + job.getId() + " -> топик: " + job.getInputTopic());
        if (job.getStatus().equals("STOPPED")) {
            if (ctx.getBroadcastState(jobDescriptor).contains(job.getInputTopic())) {
                ctx.getBroadcastState(jobDescriptor).remove(job.getInputTopic());
                System.out.println("🗑️ Джоба остановлена и удалена: " + job.getId());
            } else {
                System.out.println("⚠️ Попытка удалить несуществующую джобу: " + job.getId());
            }
        } else {
            ctx.getBroadcastState(jobDescriptor).put(job.getInputTopic(), job);
            System.out.println("📡 Джоба активна: " + job.getId() + " -> " + job.getInputTopic());
        }
    }

    @Override
    public void processElement(PersonalData data, ReadOnlyContext ctx, Collector<PersonalData> out) throws Exception {
        // Получаем джобу по sourceTopic (откуда пришли данные)
        JobResponseDto job = ctx.getBroadcastState(jobDescriptor).get(data.getSourceTopic());

        if (job != null) {
            System.out.println("📥 Flink получил данные из топика: " + data.getSourceTopic());
            System.out.println("➡️ Обрабатываем PersonalData: " + data);

            for (AnonymizationRuleDto rule : job.getRuleSet().getRules()) {
                data = AnonymizationUtil.apply(data, rule);
            }
            out.collect(data);
        } else {
            System.out.println("⛔ Джоба для топика " + data.getSourceTopic() + " не найдена. Данные пропущены.");
        }
    }



}
