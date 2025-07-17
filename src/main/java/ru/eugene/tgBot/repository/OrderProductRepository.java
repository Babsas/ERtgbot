package ru.eugene.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.eugene.tgBot.entity.ClientOrder;
import ru.eugene.tgBot.entity.OrderProduct;
import ru.eugene.tgBot.entity.Product;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "order-products", path = "order-products")
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByClientOrder(ClientOrder clientOrder);

    Optional<OrderProduct> findByClientOrderAndProduct(ClientOrder clientOrder, Product product);

    @Query("SELECT SUM(op.countProduct * op.product.price) FROM OrderProduct op WHERE op.clientOrder = :order")
    Double sumProductsInOrder(@Param("order") ClientOrder order);
}
