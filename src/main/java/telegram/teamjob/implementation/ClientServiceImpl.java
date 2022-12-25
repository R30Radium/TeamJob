package telegram.teamjob.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.Service.ClientService;
import telegram.teamjob.entity.Client;
import telegram.teamjob.repositories.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void saveClient(Client client) {
        logger.info("метод saveClient - заносим пользователя");
        clientRepository.save(client);
    }

    public Client findClient(Long chatId) {
        return clientRepository.findByChatId(chatId);
    }

    public Client updateClient(Client client) {
        Client clientToUpdate = clientRepository.findByChatId(client.getChatId());
        clientToUpdate.setStatus(client.getStatus());
        clientToUpdate.setChatId(client.getChatId());
        return clientRepository.save(clientToUpdate);
    }


}
