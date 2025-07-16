package ru.eugene.tgBot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.eugene.tgBot.entity.Client;
import ru.eugene.tgBot.repository.ClientRepository;

@SpringBootTest
public class ClientsTest {
    @Autowired
    ClientRepository clientRepository;

    @Test
    void createClients() {
        saveClient(1L, "Иванов Иван Иванович", "+797802989836", "ул Пушкина 1");
        saveClient(2L, "Денисов Денис Денисович", "+797853678478", "ул Пушкина 2");
        saveClient(3L, "Карлов Карл Карлович", "+797826785342", "ул Пушкина 3");
        saveClient(4L, "Семенов Семен Семенович", "+797814881312", "ул Пушкина 4");
        saveClient(5L, "Андреев Андрей Андреевич", "+797813376969", "ул Пушкина 5");

    }

    private void saveClient(Long ExternalId, String fullName, String phoneNumber, String address) {
        Client client2 = new Client();
        client2.setExternalId(ExternalId);
        client2.setFullName(fullName);
        client2.setPhoneNumber(phoneNumber);
        client2.setAddress(address);
        clientRepository.save(client2);
    }
}
