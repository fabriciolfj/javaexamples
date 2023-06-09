package com.github.fabriciolfj.javaexamples.entrypoint;

import com.github.fabriciolfj.javaexamples.data.Person;
import com.github.fabriciolfj.javaexamples.service.PersonService;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService service;

    @PostMapping
    @Observed(name = "post.persons")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPerson(@Valid @RequestBody final Person person) {
        log.info("person create {}", person);
        service.save(person);
    }

    @GetMapping("/{id}")
    @Observed(name = "get.persons.id")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Person findTwoAdresses(@PathVariable("id") final Long id) {
        return service.getPersonTwoAdresses(id);
    }
}
