package com.juan.dev.bookingsystem.service;

import com.juan.dev.bookingsystem.model.Room;
import com.juan.dev.bookingsystem.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
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

                    return roomRepository.save(room);
                })
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    // 🔥 SOFT DELETE (YA NO BORRA)
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setActive(false);
        roomRepository.save(room);
    }
}