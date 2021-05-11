package com.ts.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsPersonByEmail(String email);
}
