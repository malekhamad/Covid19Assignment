package com.example.covid19;

public enum CasesCount {
    LOW(70), MEDIUM(150), DANGEROUS(200);

    private final int value;

    private CasesCount(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
