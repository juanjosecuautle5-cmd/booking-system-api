package com.juan.dev.bookingsystem.repository;

import com.juan.dev.bookingsystem.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // 🔥 SOLO rooms activas
    List<Room> findByActiveTrue();
}