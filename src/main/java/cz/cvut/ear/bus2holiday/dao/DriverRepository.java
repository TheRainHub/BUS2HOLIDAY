package cz.cvut.ear.bus2holiday.dao;

import cz.cvut.ear.bus2holiday.model.Driver;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {}
