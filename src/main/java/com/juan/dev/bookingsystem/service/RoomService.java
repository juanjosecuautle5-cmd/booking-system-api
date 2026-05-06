package com.juan.dev.bookingsystem.service;

import com.juan.dev.bookingsystem.model.Room;
import com.juan.dev.bookingsystem.repository.RoomRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final AuditService auditService;

    public RoomService(RoomRepository roomRepository,
                       AuditService auditService) {
        this.roomRepository = roomRepository;
        this.auditService = auditService;
    }

    // 🔥 CREATE ROOM + AUDIT
    public Room createRoom(Room room) {

        Room savedRoom = roomRepository.save(room);

        // 🔥 OBTENER USUARIO ACTUAL
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = auth.getName();

        // 🔥 AUDITORÍA
        auditService.log(
                email,
                "Created room: " + savedRoom.getName()
        );

        return savedRoom;
    }

    // 🔥 SOLO activas
    public List<Room> getAllRooms() {
        return roomRepository.findByActiveTrue();
    }

    public Room getRoomById(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.isActive()) {
            throw new RuntimeException("Room not available");
        }

        return room;
    }

    // 🔥 UPDATE ROOM + AUDIT
    public Room updateRoom(Long id, Room updatedRoom) {

        return roomRepository.findById(id)
                .map(room -> {

                    if (!room.isActive()) {
                        throw new RuntimeException("Cannot update inactive room");
                    }

                    room.setName(updatedRoom.getName());
                    room.setDescription(updatedRoom.getDescription());
                    room.setPrice(updatedRoom.getPrice());
                    room.setAvailable(updatedRoom.isAvailable());

                    Room savedRoom = roomRepository.save(room);

                    // 🔥 OBTENER USUARIO ACTUAL
                    Authentication auth = SecurityContextHolder
                            .getContext()
                            .getAuthentication();

                    String email = auth.getName();

                    // 🔥 AUDITORÍA
                    auditService.log(
                            email,
                            "Updated room: " + savedRoom.getName()
                    );

                    return savedRoom;
                })
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    // 🔥 SOFT DELETE + AUDIT
    public void deleteRoom(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setActive(false);

        roomRepository.save(room);

        // 🔥 OBTENER USUARIO ACTUAL
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = auth.getName();

        // 🔥 AUDITORÍA
        auditService.log(
                email,
                "Deleted room: " + room.getName()
        );
    }
}