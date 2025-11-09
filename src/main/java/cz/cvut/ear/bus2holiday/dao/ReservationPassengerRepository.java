package cz.cvut.ear.bus2holiday.dao;

import cz.cvut.ear.bus2holiday.model.ReservationPassenger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationPassengerRepository extends JpaRepository<ReservationPassenger, Long> {}
