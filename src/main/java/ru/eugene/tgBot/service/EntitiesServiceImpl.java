package ru.eugene.tgBot.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.eugene.tgBot.entity.Client;
import ru.eugene.tgBot.entity.ClientOrder;
import ru.eugene.tgBot.entity.Product;
import ru.eugene.tgBot.repository.*;

import java.util.List;

@Service
@Transactional
public class EntitiesServiceImpl implements EntitiesService {
    private final ClientRepository clientRepository;
    private final ClientOrderRepository clientOrderRepository;
    private final ProductRepository productRepository;

    public EntitiesServiceImpl(ClientRepository clientRepository,
                               ClientOrderRepository clientOrderRepository,
                               ProductRepository productRepository) {
        this.clientRepository = clientRepository;
        this.clientOrderRepository = clientOrderRepository;
        this.productRepository = productRepository;

    }

    @Override
    public List<Product> getProductsByCategoryId(Long id) {
        return productRepository.findByCategoryId(id);
    }

    @Override
    public List<ClientOrder> getClientOrders(Long id) {
        return clientOrderRepository.findByClientId(id);
    }

    @Override
    public List<Product> getClientProducts(Long id) {
        return productRepository.findProductsByClientId(id);
    }

    @Override
    public List<Product> getTopPopularProducts(Integer limit) {
        return productRepository.findTopPopularProducts(limit);
    }

    @Override
    public List<Client> searchClientsByName(String name) {
        return clientRepository.findByFullNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}
