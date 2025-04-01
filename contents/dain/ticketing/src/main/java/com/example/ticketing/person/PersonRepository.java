package com.example.ticketing.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findById(Long id);
    Optional<Person> findByLoginId(String login_id);

    boolean existsByLoginId(String login_id);
    boolean existsByEmail(String email);
}
