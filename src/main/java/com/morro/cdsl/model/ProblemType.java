package com.morro.cdsl.model;

public enum ProblemType {
    CARDS("Карты", "CARDS"),
    WORDS("Слова", "WORDS"),
    NUMBERS("Числа", "NUMBERS"),
    EQUATIONS("Уравнения", "EQUATIONS"),
    BALLS_AND_URNS("Шары и урны", "BALLS"),
    DIVISIBILITY("Делимости", "DIVISIBILITY"),
    REMAINDERS("Остатки", "REMAINDERS"),
    CHESS("Шахматы", "CHESS");

    private final String displayName;
    private final String cdslName;

    ProblemType(String displayName, String cdslName) {
        this.displayName = displayName;
        this.cdslName = cdslName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCdslName() {
        return cdslName;
    }

    public static ProblemType fromCdslName(String cdslName) {
        for (ProblemType type : values()) {
            if (type.cdslName.equalsIgnoreCase(cdslName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown CDSL task type: " + cdslName);
    }
}