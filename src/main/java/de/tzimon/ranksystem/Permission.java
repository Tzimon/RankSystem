package de.tzimon.ranksystem;

import java.util.HashSet;
import java.util.Set;

public class Permission {

    public static final Permission ROOT = new Permission("");

    private static final Set<Permission> PERMISSIONS = new HashSet<>();

    private final String name;
    private final Permission parent;
    private final Set<Permission> children;

    private String fullPath;

    public static Permission get(String name) throws IllegalArgumentException {
        if (name.startsWith(".") || name.endsWith(".") || name.contains("..") || name.isEmpty())
            throw new IllegalArgumentException("Invalid name");

        for (Permission permission : Permission.PERMISSIONS) {
            if (permission.getFullPath().equals(name))
                return permission;
        }

        return new Permission(name);
    }

    private Permission(String name) {
        final String[] parts = name.split("\\.");
        final int length = parts.length;

        this.name = parts[length - 1];
        this.children = new HashSet<>();

        if (name.equals("")) {
            this.parent = null;
            this.fullPath = "";
            return;
        }

        if (length > 1)
            this.parent = Permission.get(name.substring(0, name.length() - this.name.length() - 1));
        else
            this.parent = Permission.ROOT;

        if (!this.name.equals("*"))
            this.parent.children.add(this);

        this.generateFullPath();
        Permission.PERMISSIONS.add(this);
    }

    private void generateFullPath() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.insert(0, this.name);

        Permission current = this;

        while ((current = current.parent) != Permission.ROOT)
            stringBuilder.insert(0, current.name + ".");

        this.fullPath = stringBuilder.toString();
    }

    public Set<Permission> getCorresponding() {
        final Set<Permission> corresponding = new HashSet<>(this.getAllParents());

        if (this.name.equals("*"))
            corresponding.addAll(this.parent.getAllChildren());
        else
            corresponding.add(this);

        return corresponding;
    }

    private Set<Permission> getAllParents() {
        final Set<Permission> parents = new HashSet<>();

        if (this.parent == Permission.ROOT)
            return parents;

        parents.add(this.parent);
        parents.addAll(this.parent.getAllParents());
        return parents;
    }

    private Set<Permission> getAllChildren() {
        final Set<Permission> children = new HashSet<>(this.children);
        this.children.forEach(child -> children.addAll(child.getAllChildren()));

        return children;
    }

    public String getName() {
        return name;
    }

    public Permission getParent() {
        return parent;
    }

    public Set<Permission> getChildren() {
        return children;
    }

    public String getFullPath() {
        return fullPath;
    }

}
