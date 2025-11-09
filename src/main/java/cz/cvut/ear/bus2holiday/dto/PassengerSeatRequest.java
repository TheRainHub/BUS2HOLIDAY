package cz.cvut.ear.bus2holiday.dto;

public record PassengerSeatRequest(
        String firstName, String lastName, String seatNumber, int fromStopOrder, int toStopOrder) {}
