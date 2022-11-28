package telegram.teamjob;

import liquibase.pro.packaged.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Equals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.KotlinBodySpec;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.teamjob.configuration.AnimalShelterBotConfiguration;
import telegram.teamjob.controller.ContactController;
import telegram.teamjob.model.Contact;
import telegram.teamjob.model.Shelter;
import telegram.teamjob.repositories.ContactRepository;
import telegram.teamjob.repositories.ShelterRepository;
import telegram.teamjob.service.AnimalShelterBotService;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TeamJobApplicationTests {

    /*
    @Mock
    @InjectMocks
    private String token;

    @Mock
    @InjectMocks
    private AnimalShelterBotService animalShelterBotService;

    @Mock
    @InjectMocks
    private Logger logger;

    @Mock
    @InjectMocks
    public Shelter shelter;

    @Mock
    @InjectMocks
    public ContactRepository contactRepository;

    @Mock
    @InjectMocks
    public ShelterRepository shelterRepository;

    @Test
    public static Collection<Contact> ContactCollectionTest() {
        return (Collection<Contact>) Stream.of(Arguments.of());
    }

    @Test
    public void findAllContactsTest() {

    }
    */
}
