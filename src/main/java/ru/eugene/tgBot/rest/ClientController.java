package ru.eugene.tgBot.rest;

import org.springframework.web.bind.annotation.*;
import ru.eugene.tgBot.entity.Client;
import ru.eugene.tgBot.entity.Product;
import ru.eugene.tgBot.service.EntitiesService;

import java.util.List;

@RestController
@RequestMapping("/rest/clients")
public class ClientController {

    private final EntitiesService entitiesService;

    public ClientController(EntitiesService entitiesService) {
        this.entitiesService = entitiesService;
    }

    @GetMapping("/search")
    public List<Client> searchClients(@RequestParam String name) {
        return entitiesService.searchClientsByName(name);
    }

    @GetMapping("/{id}/products")
    public List<Product> getClientProducts(@PathVariable Long id) {
        return entitiesService.getClientProducts(id);
    }
}
