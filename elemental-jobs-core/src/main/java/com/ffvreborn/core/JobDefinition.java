package com.ffvreborn.core;
import java.util.Set;
public record JobDefinition(String id, String displayName, String role, Set<String> learnableAbilities) {
    public JobDefinition {
        if (id == null || !id.matches("[a-z0-9_]+")) throw new IllegalArgumentException("Invalid job id");
        if (displayName == null || displayName.isBlank()) throw new IllegalArgumentException("Missing display name");
        if (role == null || role.isBlank()) throw new IllegalArgumentException("Missing role");
        learnableAbilities = Set.copyOf(learnableAbilities);
    }
}
