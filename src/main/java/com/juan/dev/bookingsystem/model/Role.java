package com.juan.dev.bookingsystem.model;

import java.util.Set;

public enum Role {

    USER(Set.of(
            Permission.ROOM_READ,
            Permission.BOOKING_CREATE,
            Permission.BOOKING_READ
    )),

    ADMIN(Set.of(
            Permission.ROOM_READ,
            Permission.ROOM_CREATE,
            Permission.ROOM_UPDATE,
            Permission.ROOM_DELETE,
            Permission.BOOKING_CREATE,
            Permission.BOOKING_READ
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}