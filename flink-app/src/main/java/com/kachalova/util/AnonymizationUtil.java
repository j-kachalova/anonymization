package com.kachalova.util;

import com.kachalova.model.AnonymizationRuleDto;
import com.kachalova.model.PersonalData;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class AnonymizationUtil {

    public static PersonalData apply(PersonalData data, AnonymizationRuleDto rule) {
        switch (rule.getMethod()) {
            case "MASK" -> applyMask(data, rule.getFieldName(), rule.getParameters());
            case "DELETE" -> applyDelete(data, rule.getFieldName());
            case "HASH" -> applyHash(data, rule.getFieldName());
            case "GENERALIZE" -> applyGeneralization(data, rule.getFieldName(), rule.getParameters());
            default -> throw new IllegalArgumentException("Неизвестный метод: " + rule.getMethod());
        }
        return data;
    }

    private static void applyMask(PersonalData data, String field, String mask) {
        switch (field) {
            case "phone" -> data.setPhone(maskValue(data.getPhone(), mask));
            case "email" -> data.setEmail(maskValue(data.getEmail(), mask));
            case "id" -> data.setId(maskValue(data.getId(), mask));
            case "passport" -> data.setPassport(maskValue(data.getPassport(), mask));
            case "address" -> data.setAddress(maskValue(data.getAddress(), mask));
            case "inn" -> data.setInn(maskValue(data.getInn(), mask));
            case "snils" -> data.setSnils(maskValue(data.getSnils(), mask));
            case "card" -> data.setCard(maskValue(data.getCard(), mask));
            case "birthPlace" -> data.setBirthPlace(maskValue(data.getBirthPlace(), mask));
            case "birthDate" -> data.setBirthDate(maskValue(data.getBirthDate(), mask));
        }
    }

    private static void applyDelete(PersonalData data, String field) {
        switch (field) {
            case "phone" -> data.setPhone(null);
            case "email" -> data.setEmail(null);
            case "id" -> data.setId(null);
            case "passport" -> data.setPassport(null);
            case "address" -> data.setAddress(null);
            case "inn" -> data.setInn(null);
            case "snils" -> data.setSnils(null);
            case "card" -> data.setCard(null);
            case "birthPlace" -> data.setBirthPlace(null);
            case "birthDate" -> data.setBirthDate(null);
        }
    }

    private static void applyHash(PersonalData data, String field) {
        switch (field) {
            case "phone" -> data.setPhone(hash(data.getPhone()));
            case "email" -> data.setEmail(hash(data.getEmail()));
            case "id" -> data.setId(hash(data.getId()));
            case "passport" -> data.setPassport(hash(data.getPassport()));
            case "address" -> data.setAddress(hash(data.getAddress()));
            case "inn" -> data.setInn(hash(data.getInn()));
            case "snils" -> data.setSnils(hash(data.getSnils()));
            case "card" -> data.setCard(hash(data.getCard()));
            case "birthPlace" -> data.setBirthPlace(hash(data.getBirthPlace()));
            case "birthDate" -> data.setBirthDate(hash(data.getBirthDate()));
        }
    }

    private static void applyGeneralization(PersonalData data, String field, String parameters) {
        switch (field) {
            case "birthDate" -> {
                if (data.getBirthDate() != null) {
                    try {
                        LocalDate date = LocalDate.parse(data.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        int year = date.getYear();
                        data.setBirthDate(year + "-01-01"); // обобщаем до года рождения
                    } catch (Exception e) {
                        // некорректный формат даты — оставим без изменений или можно логировать
                    }
                }
            }
            case "address" -> {
                if (data.getAddress() != null) {
                    data.setAddress("Регион: " + data.getAddress().split(",")[0]); // оставим только регион
                }
            }
        }
    }

    private static String maskValue(String value, String mask) {
        if (value == null || value.length() < 4) return mask;
        return value.substring(0, 2) + mask + value.substring(value.length() - 2);
    }

    private static String hash(String input) {
        if (input == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка хэширования", e);
        }
    }
}
