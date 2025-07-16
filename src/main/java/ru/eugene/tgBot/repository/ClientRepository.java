package ru.eugene.tgBot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.eugene.tgBot.entity.Client;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByFullNameContainingIgnoreCase(String name);
}

