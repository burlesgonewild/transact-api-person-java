package com.ts.person;

import com.ts.person.models.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface PersonRepository extends CrudRepository<Person, Long> {

    Optional<Person> findByEmail(String email);
}
