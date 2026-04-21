package com.juan.dev.bookingsystem.service;

import com.juan.dev.bookingsystem.model.Booking;
import com.juan.dev.bookingsystem.model.Room;
import com.juan.dev.bookingsystem.repository.BookingRepository;
import com.juan.dev.bookingsystem.repository.RoomRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public Booking createBooking(Booking booking) {

        // 🔥 0. Validar que venga la habitación
        if (booking.getRoom() == null || booking.getRoom().getId() == null) {
            throw new RuntimeException("Room is required");
        }

        // 🔥 1. Validar que exista
        Room room = roomRepository.findById(booking.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 🔥 2. Validar fechas NULL (esto te faltaba ⚠️)
        if (booking.getCheckIn() == null || booking.getCheckOut() == null) {
            throw new RuntimeException("Dates are required");
        }

        // 🔥 3. Validar orden de fechas (mejor con isAfter)
        if (!booking.getCheckOut().isAfter(booking.getCheckIn())) {
            throw new RuntimeException("Check-out must be after check-in");
        }

        // 🔥 4. Validar conflictos
        List<Booking> conflicts = bookingRepository
                .findByRoomIdAndCheckOutAfterAndCheckInBefore(
                        room.getId(),
                        booking.getCheckIn(),
                        booking.getCheckOut()
                );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room is not available for selected dates");
        }

        // 🔥 5. Asignar room validada
        booking.setRoom(room);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}