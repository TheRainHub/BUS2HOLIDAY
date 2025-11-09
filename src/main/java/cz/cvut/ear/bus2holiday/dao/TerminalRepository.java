package cz.cvut.ear.bus2holiday.dao;

import cz.cvut.ear.bus2holiday.model.Terminal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminalRepository extends JpaRepository<Terminal, Long> {}
