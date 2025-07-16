package ru.eugene.tgBot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.eugene.tgBot.entity.Client;
import ru.eugene.tgBot.entity.ClientOrder;
import ru.eugene.tgBot.entity.OrderProduct;
import ru.eugene.tgBot.entity.Product;
import ru.eugene.tgBot.repository.ClientOrderRepository;
import ru.eugene.tgBot.repository.ClientRepository;
import ru.eugene.tgBot.repository.OrderProductRepository;
import ru.eugene.tgBot.repository.ProductRepository;

@SpringBootTest
public class OrderFillingTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientOrderRepository clientOrderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    /**
     * Заполнение таблиц ClientOrder и OrderProduct
     */
    @Test
    public void createOrdersWithProducts() {
        // Получаем клиентов и продукты из базы
        Client client1 = clientRepository.findById(1L).orElseThrow();
        Client client2 = clientRepository.findById(2L).orElseThrow();
        Client client3 = clientRepository.findById(3L).orElseThrow();

        Product p1 = productRepository.findByName("Филадельфия").getFirst();
        Product p2 = productRepository.findByName("Токио").getFirst();
        Product p3 = productRepository.findByName("Кока-Кола").getFirst();
        Product p4 = productRepository.findByName("Чизбургер").getFirst();
        Product p5 = productRepository.findByName("Сет семейный").getFirst();

        // Заказ клиента 1
        ClientOrder order1 = saveOrder(client1, 1, 1000.0);
        saveOrderProduct(order1, p1, 2L);
        saveOrderProduct(order1, p3, 1L);

        // Заказ клиента 2
        ClientOrder order2 = saveOrder(client2, 1, 1200.0);
        saveOrderProduct(order2, p2, 2L);
        saveOrderProduct(order2, p4, 1L);

        // Заказ клиента 3
        ClientOrder order3 = saveOrder(client3, 1, 1500.0);
        saveOrderProduct(order3, p5, 1L);
        saveOrderProduct(order3, p1, 1L);
    }

    private ClientOrder saveOrder(Client client, int status, double total) {
        ClientOrder order = new ClientOrder();
        order.setClient(client);
        order.setStatus(status);
        order.setTotal(total);
        return clientOrderRepository.save(order);
    }

    private void saveOrderProduct(ClientOrder order, Product product, Long count) {
        OrderProduct op = new OrderProduct();
        op.setClientOrder(order);
        op.setProduct(product);
        op.setCountProduct(count);
        orderProductRepository.save(op);
    }
}

