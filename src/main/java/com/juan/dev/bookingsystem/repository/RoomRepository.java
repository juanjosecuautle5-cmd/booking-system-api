package com.juan.dev.bookingsystem.repository;

import com.juan.dev.bookingsystem.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}