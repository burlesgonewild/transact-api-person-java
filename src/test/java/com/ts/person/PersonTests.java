package com.ts.person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ts.person.models.Person;
import com.ts.person.models.PersonProcessObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PersonRepository personRepository;

    private ObjectMapper jsonToModelMapper = new ObjectMapper();

    @Test
    @Transactional
    public void successfulCreatePerson() throws Exception {

        PersonProcessObject toBeStoredPerson = new PersonProcessObject();
        toBeStoredPerson.setFirstName("firstName");
        toBeStoredPerson.setLastName("lastName");
        toBeStoredPerson.setEmail("firstName.lastName@email.com");
        toBeStoredPerson.setZipCode("85022");

        String personProcessObjectJsonString = jsonToModelMapper.writeValueAsString(toBeStoredPerson);

        MockHttpServletRequestBuilder createPersonRequest = post("/persons").contentType(MediaType.APPLICATION_JSON).content(personProcessObjectJsonString);
        MvcResult createPersonCall = mvc.perform(createPersonRequest).andExpect(status().isOk()).andReturn();
        PersonProcessObject personProccessObject = parsePersonJsonString(createPersonCall.getResponse().getContentAsString());

        assertTrue(personProccessObject.getId() != null);

        Optional<Person> optionalStoredPerson = personRepository.findById(personProccessObject.getId());

        assertTrue(optionalStoredPerson.isPresent());

        Person storedPerson = optionalStoredPerson.get();

        assertEquals("firstName", storedPerson.getFirstName());
        assertEquals("lastName", storedPerson.getLastName());
        assertEquals("firstName.lastName@email.com", storedPerson.getEmail());
        assertEquals("85022", storedPerson.getZipCode());
    }

    @Test
    public void createPersonWithExistingEmail() throws Exception {

        PersonProcessObject duplicatePeron = new PersonProcessObject();
        duplicatePeron.setFirstName("Beta");
        duplicatePeron.setLastName("Alpha");
        duplicatePeron.setEmail("balpha@yahoo.com");

        String personProcessObjectJsonString = jsonToModelMapper.writeValueAsString(duplicatePeron);

        MockHttpServletRequestBuilder createPersonRequest = post("/persons").contentType(MediaType.APPLICATION_JSON).content(personProcessObjectJsonString);
        mvc.perform(createPersonRequest).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void successfulGetAllPersons() throws Exception {

        final String[] PREDEFINED_LAST_NAMES_IN_ALPHABETICAL_ASCENDING_ORDER = new String[]{"Alpha", "Doe", "Doe", "Smith"};

        MvcResult result = mvc.perform(get("/persons")).andExpect(status().isOk()).andReturn();
        List<PersonProcessObject> foundPersons = parsePersonJsonArrayString(result.getResponse().getContentAsString());

        assertEquals(foundPersons.size(), 4);

        for (PersonProcessObject personProcessObject : foundPersons) {
            int personIndex = foundPersons.indexOf(personProcessObject);
            String expectedLastName = PREDEFINED_LAST_NAMES_IN_ALPHABETICAL_ASCENDING_ORDER[personIndex];
            String actualLastName = personProcessObject.getLastName();
            assertEquals(expectedLastName, actualLastName);
        }


    }

    @Test
    public void successfulGetPersonById() throws Exception {
        long personId = 2;

        MvcResult result = mvc.perform(get("/persons/" + personId)).andExpect(status().isOk()).andReturn();
        PersonProcessObject foundPersonById = parsePersonJsonString(result.getResponse().getContentAsString());

        assertEquals(foundPersonById.getId(), 2);
        assertEquals(foundPersonById.getFirstName(), "John");
        assertEquals(foundPersonById.getLastName(), "Doe");
        assertEquals(foundPersonById.getEmail(), "jdoe@yahoo.com");
        assertEquals(foundPersonById.getZipCode(), null);
    }

    @Test
    public void getPersonByUnknownId() throws Exception {
        long personId = 222;
        mvc.perform(get("/persons/" + personId)).andExpect(status().isNotFound());
    }


    private PersonProcessObject parsePersonJsonString(String personJsonString) throws JsonProcessingException {
        System.out.println(personJsonString);
        PersonProcessObject personProcessObject = jsonToModelMapper.readValue(personJsonString, new TypeReference<>() {
        });
        return personProcessObject;
    }

    private List<PersonProcessObject> parsePersonJsonArrayString(String jsonReponseBody) throws JsonProcessingException {
        System.out.println(jsonReponseBody);
        List<PersonProcessObject> persons = jsonToModelMapper.readValue(jsonReponseBody, new TypeReference<>() {
        });
        return persons;
    }


}
