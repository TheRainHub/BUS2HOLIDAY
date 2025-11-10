package cz.cvut.ear.bus2holiday.service;

import cz.cvut.ear.bus2holiday.dao.*;
import cz.cvut.ear.bus2holiday.dto.PassengerSeatRequest;
import cz.cvut.ear.bus2holiday.dto.ReservationRequest;
import cz.cvut.ear.bus2holiday.exception.SeatUnavailableException;
import cz.cvut.ear.bus2holiday.model.*;
import cz.cvut.ear.bus2holiday.model.enums.ReservationStatus;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final ReservationPassengerRepository passengerRepo;
    private final BookedSegmentRepository segmentRepo;
    private final TripRepository tripRepo;
    private final UserRepository userRepo;

    public ReservationService(
            ReservationRepository reservationRepo,
            ReservationPassengerRepository passengerRepo,
            BookedSegmentRepository segmentRepo,
            TripRepository tripRepo,
            UserRepository userRepo) {
        this.reservationRepo = reservationRepo;
        this.passengerRepo = passengerRepo;
        this.segmentRepo = segmentRepo;
        this.tripRepo = tripRepo;
        this.userRepo = userRepo;
    }

    @Autowired private RouteStopRepository routeStopRepo;

    @Transactional
    public Reservation createReservation(ReservationRequest request) {

        User user =
                userRepo.findById(request.userId())
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "User not found: " + request.userId()));
        Trip trip =
                tripRepo.findById(request.tripId())
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Trip not found: " + request.tripId()));

        for (PassengerSeatRequest psr : request.passengers()) {
            if (psr.fromStopOrder() >= psr.toStopOrder()) {
                throw new IllegalArgumentException("fromStopOrder must be less than toStopOrder");
            }

            if (!isSeatAvailable(
                    trip.getId(), psr.seatNumber(), psr.fromStopOrder(), psr.toStopOrder())) {
                throw new SeatUnavailableException(
                        "Seat " + psr.seatNumber() + " is not available for the selected segment.");
            }
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setTrip(trip);
        reservation.setStatus(ReservationStatus.PENDING);

        BigDecimal totalAmount =
                trip.getPrice().multiply(BigDecimal.valueOf(request.passengers().size()));
        reservation.setTotalAmount(totalAmount);
        reservation.setBookingReference(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        Reservation savedReservation = reservationRepo.save(reservation);
        Set<ReservationPassenger> passengers = new HashSet<>();

        for (PassengerSeatRequest psr : request.passengers()) {
            RouteStop fromStop =
                    routeStopRepo
                            .findByRouteIdAndSequenceOrder(
                                    trip.getRoute().getId(), psr.fromStopOrder())
                            .orElseThrow(() -> new EntityNotFoundException("RouteStop not found"));

            ReservationPassenger passenger = new ReservationPassenger();
            passenger.setReservation(savedReservation);
            passenger.setFirstName(psr.firstName());
            passenger.setLastName(psr.lastName());
            ReservationPassenger savedPassenger = passengerRepo.save(passenger);

            BookedSegment segment = new BookedSegment();
            segment.setTrip(trip);
            segment.setPassenger(savedPassenger);
            segment.setSeatNumber(psr.seatNumber());
            segment.setFromStopOrder(psr.fromStopOrder());
            segment.setToStopOrder(psr.toStopOrder());
            segment.setFromStop(fromStop);
            segmentRepo.save(segment);

            passengers.add(savedPassenger);
        }

        savedReservation.setPassengers(passengers);
        return savedReservation;
    }

    private boolean isSeatAvailable(
            Long tripId, String seatNumber, int requestedFrom, int requestedTo) {

        List<BookedSegment> existingSegments =
                segmentRepo.findByTripIdAndSeatNumber(tripId, seatNumber);

        if (existingSegments.isEmpty()) {
            return true;
        }

        for (BookedSegment existing : existingSegments) {
            int existingFrom = existing.getFromStopOrder();
            int existingTo = existing.getToStopOrder();

            boolean overlaps = (requestedFrom < existingTo) && (existingFrom < requestedTo);

            if (overlaps) {
                return false;
            }
        }

        return true;
    }
}
