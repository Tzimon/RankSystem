package de.tzimon.ranksystem;

import java.util.HashSet;
import java.util.Set;

public class Rank {

    private final String name;

    private final Set<Permission> permissions;

    public Rank(final String name) {
        this.name = name;
        this.permissions = new HashSet<>();
    }

    public String getName() {
        return this.name;
    }

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

}
