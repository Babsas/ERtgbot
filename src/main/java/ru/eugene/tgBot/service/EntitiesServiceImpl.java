package ru.eugene.tgBot.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.eugene.tgBot.entity.*;
import ru.eugene.tgBot.repository.*;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EntitiesServiceImpl implements EntitiesService {
    private final ClientRepository clientRepository;
    private final ClientOrderRepository clientOrderRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public EntitiesServiceImpl(ClientRepository clientRepository,
                               ClientOrderRepository clientOrderRepository,
                               ProductRepository productRepository,
                               CategoryRepository categoryRepository) {
        this.clientRepository = clientRepository;
        this.clientOrderRepository = clientOrderRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;

    }

    // Методы для работы с клиентами
    @Override
    public Optional<Client> findClientByExternalId(Long externalId) {
        return clientRepository.findByExternalId(externalId);
    }

    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public List<Client> searchClientsByName(String name) {
        return clientRepository.findByFullNameContainingIgnoreCase(name);
    }

    @Override
    public List<ClientOrder> getClientOrders(Long clientId) {
        return clientOrderRepository.findByClientId(clientId);
    }

    @Override
    public ClientOrder saveOrder(ClientOrder order) {
        return clientOrderRepository.save(order);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> getClientProducts(Long clientId) {
        return productRepository.findProductsByClientId(clientId);
    }

    @Override
    public List<Product> getTopPopularProducts(Integer limit) {
        return productRepository.findTopPopularProducts(limit);
    }

    @Override
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Category> getCategoriesByParentId(Long parentId) {
        if (parentId == null) {
            return categoryRepository.findByParentIsNull();
        }
        return categoryRepository.findByParentId(parentId);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

}
