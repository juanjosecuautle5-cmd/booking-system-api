package com.juan.dev.bookingsystem.dto;

import java.time.LocalDate;

public class BookingResponse {

    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Long roomId;

    public BookingResponse(Long id, LocalDate checkIn, LocalDate checkOut, Long roomId) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomId = roomId;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public Long getRoomId() {
        return roomId;
    }
}