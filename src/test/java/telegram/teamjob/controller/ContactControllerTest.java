package telegram.teamjob.controller; // поменял название пакета, чтобы работало покрытие


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pengrad.telegrambot.TelegramBot;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import telegram.teamjob.entity.Contact;
import telegram.teamjob.repositories.ContactRepository;
import telegram.teamjob.repositories.InformationForOwnerRepository;
import telegram.teamjob.repositories.ShelterRepository;
import telegram.teamjob.service.PetPhotoServiceImpl;
import telegram.teamjob.service.RecordServiceImpl;
import telegram.teamjob.service.TelegramBotUpdatesListener;
import telegram.teamjob.service.UserServiceImpl;


@WebMvcTest(controllers = ContactController.class) // надо указать, контекст какого контроллера спринг должен замокать
@ExtendWith(MockitoExtension.class) // т.к. используется мокито, то надо указать расширение его
public class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean // т.к. ContactController замокан, то все его зависимости должны быть либо тоже замоканы, либо заспаены
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    // В свою очередь TelegramBotUpdatesListener тоже имеет зависимости и т.к. он Spy, то его зависимости тоже надо либо замокать, либо заспаить

    @MockBean
    private ContactRepository contactRepositoryTest;

    @MockBean
    private ShelterRepository shelterRepository;

    @MockBean
    private InformationForOwnerRepository informationForOwnerRepository;

    @MockBean
    private  UserServiceImpl userService;

    @MockBean
    private  RecordServiceImpl recordService;

    @MockBean
    private  PetPhotoServiceImpl petPhotoService;

    @MockBean
    private  TelegramBot telegramBot;

    Contact contact = new Contact(1, "89061876655", "Петров Семен Иванович");
    JSONObject jsonContact = new JSONObject();
    private final int id = 1;
    private final String name = "Петров Семен Иванович";
    private final String numberPhone = "89061876655";
    @Test
    public void saveContactTestPositive() {
        Mockito.when(contactRepositoryTest.save(Mockito.any())).thenReturn(contact);
        try {
            mockMvc.perform(post("/contact/newContact") // Был неправильный путь, из-зз чего 404
                            .content(jsonContact.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.numberPhone").value(numberPhone))
                    .andExpect(jsonPath("$.name").value(name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getContactByIdTestPositive() throws Exception {

        Mockito.when(contactRepositoryTest.save(Mockito.any())).thenReturn(contact);
        Mockito.when(contactRepositoryTest.findById(Mockito.any())).thenReturn(Optional.of(contact));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/contact/{id}", id) // id надо не прибавлять к строке, а подставлять вместо {id}, также путь был неправильный
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.numberPhone").value(numberPhone))
                .andExpect(jsonPath("$.name").value(name));

    }
    @Test
    public void getAllContactTestPositive() throws Exception {

        Mockito.when(contactRepositoryTest.save(Mockito.any())).thenReturn(contact);
        Mockito.when(contactRepositoryTest.findAll()).thenReturn(List.of(contact));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/contact/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}

