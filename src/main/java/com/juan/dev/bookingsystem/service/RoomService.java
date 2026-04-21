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

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public Room updateRoom(Long id, Room updatedRoom) {
        return roomRepository.findById(id)
                .map(room -> {
                    room.setName(updatedRoom.getName());
                    room.setDescription(updatedRoom.getDescription());
                    room.setPrice(updatedRoom.getPrice());
                    room.setAvailable(updatedRoom.isAvailable());
                    return roomRepository.save(room);
                })
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}