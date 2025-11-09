package cz.cvut.ear.bus2holiday.dao;

import cz.cvut.ear.bus2holiday.model.Route;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {}
