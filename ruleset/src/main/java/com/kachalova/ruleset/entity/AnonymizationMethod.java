package com.kachalova.ruleset.entity;


import lombok.Getter;

@Getter
public enum AnonymizationMethod {
    MASK,
    HASH,
    DELETE,
    GENERALIZE;

    public static AnonymizationMethod fromString(String method) {
        try {
            return AnonymizationMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown anonymization method: " + method);
        }
    }
}

