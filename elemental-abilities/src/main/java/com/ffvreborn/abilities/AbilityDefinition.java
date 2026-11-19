package com.ffvreborn.abilities;
public record AbilityDefinition(String id, String jobId, int requiredLevel, int abpCost, int manaCost, boolean passive, int cooldownTicks, String effect) {
    public AbilityDefinition {
        if (id == null || !id.matches("[a-z0-9_]+") || jobId == null || requiredLevel < 1 || requiredLevel > 50 || abpCost < 0 || manaCost < 0 || cooldownTicks < 0 || effect == null || effect.isBlank()) throw new IllegalArgumentException("Invalid ability definition");
    }
}
