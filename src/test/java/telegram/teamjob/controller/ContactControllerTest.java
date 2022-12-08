package telegram.teamjob.controller;

import com.pengrad.telegrambot.TelegramBot;
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
import telegram.teamjob.repositories.*;
import telegram.teamjob.implementation.PetPhotoServiceImpl;
import telegram.teamjob.implementation.RecordServiceImpl;
import telegram.teamjob.implementation.TelegramBotUpdatesListener;
import telegram.teamjob.implementation.UserServiceImpl;


import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ContactController.class)
@ExtendWith(MockitoExtension.class)
public class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @MockBean
    private ContactRepository contactRepositoryTest;
    @MockBean
    private ShelterRepository shelterRepository;
    @MockBean
    private  InformationForOwnerRepository informationForOwnerRepository;
    @MockBean
    private  UserServiceImpl userService;
    @MockBean
    private  RecordServiceImpl recordService;
    @MockBean
    private  PetPhotoServiceImpl petPhotoService;
    @MockBean
    private ReportRepository reportRepository;
    @MockBean
    private PetPhotoRepository petPhotoRepository;
    @MockBean
    private  TelegramBot telegramBot;



    Contact contact = new Contact(2, "89061876655", "Петров Семен Иванович");

    JSONObject jsonContact = new JSONObject();
    private final int id = 2;
    private final String name = "Петров Семен Иванович";
    private final String numberPhone = "89061876655";

    @Test
    public void getContactByIdTestPositive() throws Exception {

        Mockito.when(contactRepositoryTest.save(Mockito.any())).thenReturn(contact);
        Mockito.when(contactRepositoryTest.findById(Mockito.any())).thenReturn(Optional.of(contact));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/contact/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.numberPhone").value(numberPhone));

    }
    @Test
    public void getContactByIdTestNegative() throws Exception {
        Mockito.when(contactRepositoryTest.findById(Mockito.any())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/contact/{id}", 100)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getAllContactTestPositive() throws Exception {

        Mockito.when(contactRepositoryTest.save(Mockito.any())).thenReturn(contact);
        Mockito.when(contactRepositoryTest.findAll()).thenReturn(List.of(contact));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/contact")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void saveContactTestPositive() {
        Mockito.when(contactRepositoryTest.save(Mockito.any())).thenReturn(contact);
        try {
            mockMvc.perform(post("/contact/newContact")
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

}
