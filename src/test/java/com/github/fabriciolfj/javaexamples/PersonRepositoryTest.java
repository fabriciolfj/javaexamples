package com.github.fabriciolfj.javaexamples;

import com.github.fabriciolfj.javaexamples.data.Person;
import com.github.fabriciolfj.javaexamples.repository.AddressRepository;
import com.github.fabriciolfj.javaexamples.repository.PersonRepository;
import fixture.AddressFixture;
import fixture.PersonFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.LineIterator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //nao tentar configurar um banco para teste
@DataJpaTest
public class PersonRepositoryTest {

    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.1")
            .withDatabaseName("test")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void setProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private PersonRepository repository;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setup() {
        init();
    }

    @Test
    void testPersonFindTwoAddresses() {
        var person = repository.findById(1L);
        var addresses = addressRepository.findAllAddress(person.get());
        System.out.println("Number of addresses: " + addresses.size());

        assertNotNull(person);
        assertNotNull(addresses);
        assertTrue(addresses.size() == 2);
    }

    void init() {
        var person = PersonFixture.toValid();
        var ad1 = AddressFixture.getValid(person);
        var ad2 = AddressFixture.getValid(person);

        addressRepository.save(ad1);
        addressRepository.save(ad2);
    }
}