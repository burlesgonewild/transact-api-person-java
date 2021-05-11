package com.ts.person;

import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonRepository personRepository;
    private final CityService cityService;
    private final Sort LAST_NAME_ASC;

    public PersonController(PersonRepository personRepository, CityService cityService) {
        this.personRepository = personRepository;
        this.cityService = cityService;
        // id is used as a tie breaker in order to have consistent sort
        LAST_NAME_ASC = Sort.by(Sort.Direction.ASC, "lastName", "id");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> getPerson() {
        return personRepository.findAll(LAST_NAME_ASC).stream()
                .map(person -> {
                    City city = cityService.getCityByZipCode(person.getZipCode());
                    person.setCity(city.getCity());
                    return person;
                }).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Person createPerson(@Valid @RequestBody Person person) {
        if (this.personRepository.existsPersonByEmail(person.getEmail())) {
            throw new UniqueEmailException(person.getEmail());
        }

        return personRepository.save(person);
    }
}
