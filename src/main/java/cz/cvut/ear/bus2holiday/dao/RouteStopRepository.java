package cz.cvut.ear.bus2holiday.dao;

import cz.cvut.ear.bus2holiday.model.RouteStop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {

    Optional<RouteStop> findByRouteIdAndSequenceOrder(Long routeId, int sequenceOrder);
}
