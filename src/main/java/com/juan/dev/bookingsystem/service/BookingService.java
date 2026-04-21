package com.juan.dev.bookingsystem.service;

import com.juan.dev.bookingsystem.dto.BookingRequest;
import com.juan.dev.bookingsystem.dto.BookingResponse;
import com.juan.dev.bookingsystem.model.Booking;
import com.juan.dev.bookingsystem.model.Room;
import com.juan.dev.bookingsystem.repository.BookingRepository;
import com.juan.dev.bookingsystem.repository.RoomRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    // ✅ CREATE con DTOs
    public BookingResponse createBooking(BookingRequest request) {

        // 🔥 1. Validar habitación
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 🔥 2. Validar fechas null
        if (request.getCheckIn() == null || request.getCheckOut() == null) {
            throw new RuntimeException("Dates are required");
        }

        // 🔥 3. Validar orden
        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new RuntimeException("Check-out must be after check-in");
        }

        // 🔥 4. Validar conflictos
        var conflicts = bookingRepository
                .findByRoomIdAndCheckOutAfterAndCheckInBefore(
                        request.getRoomId(),
                        request.getCheckIn(),
                        request.getCheckOut()
                );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room is not available for selected dates");
        }

        // 🔥 5. Crear entidad
        Booking booking = new Booking();
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setRoom(room);

        Booking saved = bookingRepository.save(booking);

        // 🔥 6. Responder DTO
        return new BookingResponse(
                saved.getId(),
                saved.getCheckIn(),
                saved.getCheckOut(),
                room.getId()
        );
    }

    // ✅ GET ALL con DTOs
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(booking -> new BookingResponse(
                        booking.getId(),
                        booking.getCheckIn(),
                        booking.getCheckOut(),
                        booking.getRoom().getId()
                ))
                .collect(Collectors.toList());
    }
}