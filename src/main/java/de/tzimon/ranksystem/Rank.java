package de.tzimon.ranksystem;

import java.util.HashSet;
import java.util.Set;

public class Rank {

    public static final Set<String> FORBIDDEN_NAMES = new HashSet<>();

    private final String name;

    private final Set<Permission> permissions;

    public Rank(final String name) {
        this.name = name;
        this.permissions = new HashSet<>();
    }

    public static void registerForbiddenNames() {
        Rank.FORBIDDEN_NAMES.add(".*\\..*");
        Rank.FORBIDDEN_NAMES.add("all");
    }

    public String getName() {
        return this.name;
    }

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

}
