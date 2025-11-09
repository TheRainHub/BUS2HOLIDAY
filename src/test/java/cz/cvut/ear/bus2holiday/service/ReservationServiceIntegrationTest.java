package cz.cvut.ear.bus2holiday.service;

import static org.junit.jupiter.api.Assertions.*;

import cz.cvut.ear.bus2holiday.TestContainerConfig;
import cz.cvut.ear.bus2holiday.dao.*;
import cz.cvut.ear.bus2holiday.dto.PassengerSeatRequest;
import cz.cvut.ear.bus2holiday.dto.ReservationRequest;
import cz.cvut.ear.bus2holiday.exception.SeatUnavailableException;
import cz.cvut.ear.bus2holiday.model.*;
import cz.cvut.ear.bus2holiday.model.enums.UserRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@SpringBootTest
@Transactional
public class ReservationServiceIntegrationTest extends TestContainerConfig {

    @Autowired private ReservationService reservationService;

    @Autowired private UserRepository userRepo;
    @Autowired private TripRepository tripRepo;
    @Autowired private RouteRepository routeRepo;
    @Autowired private BusRepository busRepo;
    @Autowired private BookedSegmentRepository segmentRepo;

    private User testUser;
    private Trip testTrip;

    @BeforeEach
    void setUp() {
        User u = new User();
        u.setEmail("test@user.com");
        u.setPasswordHash("hash");
        u.setFirstName("Test");
        u.setLastName("User");
        u.setRole(UserRole.user);
        testUser = userRepo.save(u);

        Bus bus = new Bus();
        bus.setTotalSeats(50);
        bus.setModel("TestBus");
        bus.setRegistrationNumber("TEST-123");
        bus.setSeatLayout("{}");
        busRepo.save(bus);

        Route route = new Route();
        route.setName("Test Route");
        routeRepo.save(route);

        Trip t = new Trip();
        t.setRoute(route);
        t.setBus(bus);
        t.setPrice(BigDecimal.valueOf(100));
        t.setDepartureDatetime(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));
        t.setArrivalDatetime(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1).plusHours(5));
        testTrip = tripRepo.save(t);
    }

    @Test
    void testCreateReservation_HappyPath() {
        PassengerSeatRequest p1 = new PassengerSeatRequest("Alice", "Smith", "1A", 1, 5);
        PassengerSeatRequest p2 = new PassengerSeatRequest("Bob", "Jones", "1B", 1, 5);
        ReservationRequest request =
                new ReservationRequest(testUser.getId(), testTrip.getId(), List.of(p1, p2));

        Reservation reservation = reservationService.createReservation(request);

        assertNotNull(reservation);
        assertNotNull(reservation.getId());
        assertEquals(2, reservation.getPassengers().size());
        assertEquals(2, segmentRepo.count());
    }

    @Test
    void testCreateReservation_FailOnOverlappingSegment() {
        PassengerSeatRequest initialPassenger =
                new PassengerSeatRequest("Initial", "Booker", "2A", 2, 5);
        ReservationRequest initialRequest =
                new ReservationRequest(
                        testUser.getId(), testTrip.getId(), List.of(initialPassenger));
        reservationService.createReservation(initialRequest);

        PassengerSeatRequest overlappingPassenger =
                new PassengerSeatRequest("Late", "Booker", "2A", 4, 6);
        ReservationRequest overlappingRequest =
                new ReservationRequest(
                        testUser.getId(), testTrip.getId(), List.of(overlappingPassenger));

        assertThrows(
                SeatUnavailableException.class,
                () -> {
                    reservationService.createReservation(overlappingRequest);
                });

        assertEquals(1, segmentRepo.count());
    }

    @Test
    void testCreateReservation_SuccessOnNonOverlappingSegment() {

        PassengerSeatRequest p1 = new PassengerSeatRequest("Anna", "First", "3B", 1, 3);
        ReservationRequest r1 =
                new ReservationRequest(testUser.getId(), testTrip.getId(), List.of(p1));
        reservationService.createReservation(r1);

        PassengerSeatRequest p2 = new PassengerSeatRequest("Boris", "Second", "3B", 3, 5);
        ReservationRequest r2 =
                new ReservationRequest(testUser.getId(), testTrip.getId(), List.of(p2));

        Reservation reservation2 = null;
        assertDoesNotThrow(
                () -> {
                    reservationService.createReservation(r2);
                });

        assertEquals(2, segmentRepo.count());
    }
}
