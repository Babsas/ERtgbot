package ru.eugene.tgBot.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.eugene.tgBot.entity.ClientOrder;
import ru.eugene.tgBot.service.EntitiesService;

import java.util.List;

@RestController
@RequestMapping("/rest/clients")
public class ClientOrderController {

    private final EntitiesService entitiesService;

    public ClientOrderController(EntitiesService entitiesService) {
        this.entitiesService = entitiesService;
    }

    // /rest/clients/{id}/orders
    @GetMapping("/{id}/orders")
    public List<ClientOrder> getClientOrders(@PathVariable Long id) {
        return entitiesService.getClientOrders(id);
    }
}