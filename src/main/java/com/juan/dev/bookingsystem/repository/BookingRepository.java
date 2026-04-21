package com.juan.dev.bookingsystem.repository;

import com.juan.dev.bookingsystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomId(Long roomId);

    // 🔥 clave para evitar conflictos
    List<Booking> findByRoomIdAndCheckOutAfterAndCheckInBefore(
            Long roomId,
            LocalDate checkIn,
            LocalDate checkOut
    );
}