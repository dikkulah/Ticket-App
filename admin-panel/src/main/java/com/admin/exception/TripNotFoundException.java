package com.admin.exception;

public class TripNotFoundException extends RuntimeException {
    private static final String TRIP_NOT_FOUND="Böyle bir sefer mevcut değil.";
    public TripNotFoundException() {
        super(TRIP_NOT_FOUND);
    }
}
