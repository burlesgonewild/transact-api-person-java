package com.ts.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ts.person.models.Person;
import com.ts.person.models.PersonProcessObject;
import com.ts.person.models.ZipCodeMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.TEXT_HTML;


@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    public PersonRepository personRepository;

//    private final WebClient ZIP_STATIC_CLIENT = WebClient.create("http://ZiptasticAPI.com/");
    private final WebClient ZIP_STATIC_CLIENT = WebClient.builder()
        .exchangeStrategies(ExchangeStrategies.builder().codecs(this::acceptedCodecs).build())
        .baseUrl("http://ZiptasticAPI.com/")
        .build();


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createPerson(@RequestBody PersonProcessObject personProcessObject) throws Exception {

        Optional<Person> optionalPerson = personRepository.findByEmail(personProcessObject.getEmail());
        if (optionalPerson.isPresent()) {
            return ResponseEntity.badRequest().body("person email already exist");
        }

        Person personToSave = new Person(personProcessObject);

        Person savedPerson = personRepository.save(personToSave);

        return ResponseEntity.ok(savedPerson);
    }

    @GetMapping(value = "/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonProcessObject> getPerson(@PathVariable Long personId){

        Optional<Person> optionalPerson = personRepository.findById(personId);

        if (!optionalPerson.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Person storedPerson = optionalPerson.get();

        PersonProcessObject personProcessObject = new PersonProcessObject(storedPerson);
        String zipCode = storedPerson.getZipCode();
        if (zipCode != null && !zipCode.isEmpty()) {
            setPersonCityFromZipcode(zipCode, personProcessObject);
        }

        return ResponseEntity.ok(personProcessObject);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonProcessObject>> getAllPersons() {

        List<PersonProcessObject> allPersonsForResponse = new ArrayList<>();
        Iterable<Person> allPersonsStored = personRepository.findAll();

        for (Person person : allPersonsStored) {
            PersonProcessObject personProcessObject = new PersonProcessObject(person);
            String zipCode = person.getZipCode();
            if (zipCode != null && !zipCode.isEmpty()) {
                setPersonCityFromZipcode(zipCode, personProcessObject);
            }
            allPersonsForResponse.add(personProcessObject);
        }

        allPersonsForResponse.sort(new PersonProcessObject.SortByLastName());

        return ResponseEntity.ok(allPersonsForResponse);
    }

    private void setPersonCityFromZipcode(String zipcode, PersonProcessObject personProcessObject) {
        ZipCodeMetaData zipCodeMetaData = ZIP_STATIC_CLIENT.get()
                .uri(zipcode)
                .retrieve()
                .bodyToMono(ZipCodeMetaData.class)
                .block();

        personProcessObject.setCity(zipCodeMetaData.getCity());

    }


    private void acceptedCodecs(ClientCodecConfigurer clientCodecConfigurer) {
        clientCodecConfigurer.customCodecs().encoder(new Jackson2JsonEncoder(new ObjectMapper(), TEXT_HTML));
        clientCodecConfigurer.customCodecs().decoder(new Jackson2JsonDecoder(new ObjectMapper(), TEXT_HTML));
    }

}
