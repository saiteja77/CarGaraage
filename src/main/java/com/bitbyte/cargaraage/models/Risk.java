package com.bitbyte.cargaraage.models;

public enum Risk {
    LOW (1), MEDIUM (3), HIGH (5);

    private int risk;

    Risk(int i) {
        this.risk = i;
    }

    public int getRisk() {
        return risk;
    }
}
