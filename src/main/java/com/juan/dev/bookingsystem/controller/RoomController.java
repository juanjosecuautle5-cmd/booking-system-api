package com.juan.dev.bookingsystem.controller;

import com.juan.dev.bookingsystem.model.Room;
import com.juan.dev.bookingsystem.service.RoomService;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // 🔥 CREATE ROOM
    @PostMapping
    @PreAuthorize("hasAuthority('ROOM_CREATE')")
    public Room createRoom(@Valid @RequestBody Room room) {
        return roomService.createRoom(room);
    }

    // 🔥 GET ALL ROOMS
    @GetMapping
    @PreAuthorize("hasAuthority('ROOM_READ')")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    // 🔥 GET ROOM BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROOM_READ')")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    // 🔥 UPDATE ROOM
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROOM_UPDATE')")
    public Room updateRoom(@PathVariable Long id,
                           @Valid @RequestBody Room room) {
        return roomService.updateRoom(id, room);
    }

    // 🔥 DELETE ROOM
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROOM_DELETE')")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
}