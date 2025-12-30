package cz.cvut.ear.bus2holiday.dto.request;

import cz.cvut.ear.bus2holiday.dto.response.PassengerSeatRequest;

import java.util.List;

public record ReservationRequest(Long userId, Long tripId, List<PassengerSeatRequest> passengers) {}
