package telegram.teamjob.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import telegram.teamjob.model.Contact;
import telegram.teamjob.service.AnimalShelterBotService;
import java.util.Collection;


@RestController
@RequestMapping("/contact")
public class ContactController {

    private final AnimalShelterBotService animalShelterBotService;

    private final Logger logger = LoggerFactory.getLogger(AnimalShelterBotService.class);

    public ContactController(AnimalShelterBotService animalShelterBotService){
        this.animalShelterBotService = animalShelterBotService;
    }
    @Operation(summary = "Поиск контакта по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденный по id контакт",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Contact.class)
                            ))}, tags = "Contact" )

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@Parameter(description = "id контакта, для корректного поиска нужно указать верный id", required = true, example = "1")
                                              @PathVariable int id){
        Contact contact = animalShelterBotService.findContactById(id);
        if(contact == null ){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contact);
    }
    @Operation(summary = "Поиск всех контактов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Все найденные в базе контакты",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array=@ArraySchema(schema = @Schema(implementation = Contact.class))
                            ))}, tags = "Contact")
    @GetMapping
    public ResponseEntity<Collection<Contact>> findAllContacts(){
        Collection<Contact> contacts = animalShelterBotService.getAllContacts();
        if(contacts == null ){
            logger.warn("В базе данных нет контактов");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contacts);
    }

    @Operation(summary = "Добавление нового контакта в базу данных",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новый контакт, который нужно добавить в базу данных. " +
                            "При заполнении данных контакта значение параметра id указывать не нужно," +
                            "потому что оно генерируется программой автоматически",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Contact.class)
                    )), tags = "Contact")

    @PostMapping("/newContact")
    public Contact addContact(@RequestBody Contact contact){
        return animalShelterBotService.addContact(contact);
    }
}

