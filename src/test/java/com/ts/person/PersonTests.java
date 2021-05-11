package com.ts.person;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonTests {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Person person1;
    private Person person2;

    @Autowired
    private MockMvc mvc;

    @Resource
    private PersonRepository personRepository;

    @BeforeEach
    public void setUp() {
        person1 = new Person();
        person1.setFirstName("Bob");
        person1.setLastName("Smith");
        person1.setEmail("bsmith@gmail.com");
        person1.setZipCode("85286");

        personRepository.save(person1);

        person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setEmail("jadoe@yahoo.com");
        person2.setZipCode("85202"); // maps to the city Mesa

        personRepository.save(person2);
    }

    @AfterEach
    public void tearDown() {
        personRepository.deleteAll();
    }

    @Test
    void getAllPersons() throws Exception {
        mvc.perform(get("/persons")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    void getAllPersons_shouldReturnPersonSortedByLastNameAscending() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andReturn();
        String mvcResultContent = mvcResult.getResponse().getContentAsString();
        List<Person> result = objectMapper.readValue(mvcResultContent, new TypeReference<>() {
        });

        assertThat(result).isSortedAccordingTo(Comparator.comparing(Person::getLastName));
    }

    @Test
    void getAllPersons_shouldReturnPersonWithCity() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andReturn();
        String mvcResultContent = mvcResult.getResponse().getContentAsString();
        List<Person> result = objectMapper.readValue(mvcResultContent, new TypeReference<>() {
        });

        assertThat(result.get(0).getCity().toUpperCase()).isEqualTo("MESA");
    }

    @Test
    void createPerson_shouldCreatePerson() throws Exception {
        Person person = new Person();
        person.setFirstName("Test");
        person.setLastName("Test");
        person.setZipCode("00000");
        person.setEmail("tst@test.com");

        mvc.perform(post("/persons")
                .content(objectMapper.writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createPerson_shouldThrowErrorOnDuplicateEmail() throws Exception {
        mvc.perform(post("/persons")
                .content(objectMapper.writeValueAsString(person1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
