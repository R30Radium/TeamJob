package telegram.teamjob.controllerTest;


import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import telegram.teamjob.entity.Contact;
import telegram.teamjob.repositories.ContactRepository;
import telegram.teamjob.service.TelegramBotUpdatesListener;


import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@AutoConfigureMockMvc
public class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactRepository contactRepositoryTest;

    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    // @InjectMocks
    //  private ContactController contactController;
    Contact contact = new Contact(1, "89061876655", "Петров Семен Иванович");
    JSONObject jsonContact = new JSONObject();
    private final int id = 1;
    private final String name = "Петров Семен Иванович";
    private final String numberPhone = "89061876655";
    @Test
    public void saveContactTestPositive() {
        Mockito.when(contactRepositoryTest.save(Mockito.any())).thenReturn(contact);
        try {
            mockMvc.perform(post("/newContact")
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
                        .get("/{id}" + id)
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

