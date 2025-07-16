package ru.eugene.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.eugene.tgBot.entity.ClientOrder;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "client-orders", path = "client-orders")
public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {
    List<ClientOrder> findByClientId(Long clientId);
}

