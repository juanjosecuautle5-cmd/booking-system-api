package com.juan.dev.bookingsystem.controller;

import com.juan.dev.bookingsystem.model.Room;
import com.juan.dev.bookingsystem.service.RoomService;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize; // 🔥 IMPORTANTE
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // 🔥 SOLO ADMIN puede crear
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Room createRoom(@Valid @RequestBody Room room) {
        return roomService.createRoom(room);
    }

    // 🔥 USER y ADMIN pueden ver todos
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    // 🔥 USER y ADMIN pueden ver por id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    // 🔥 SOLO ADMIN puede actualizar
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Room updateRoom(@PathVariable Long id, @Valid @RequestBody Room room) {
        return roomService.updateRoom(id, room);
    }

    // 🔥 SOLO ADMIN puede eliminar
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
}