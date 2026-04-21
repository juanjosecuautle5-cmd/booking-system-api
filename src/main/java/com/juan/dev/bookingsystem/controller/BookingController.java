package com.juan.dev.bookingsystem.controller;

import com.juan.dev.bookingsystem.dto.BookingRequest;
import com.juan.dev.bookingsystem.dto.BookingResponse;
import com.juan.dev.bookingsystem.service.BookingService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ✅ CREATE con DTO
    @PostMapping
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    // ✅ GET ALL con DTO
    @GetMapping
    public List<BookingResponse> getAll() {
        return bookingService.getAllBookings();
    }
}