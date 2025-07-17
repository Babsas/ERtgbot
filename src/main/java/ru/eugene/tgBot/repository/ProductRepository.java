package ru.eugene.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.eugene.tgBot.entity.Product;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    @Query("""
        SELECT op.product FROM OrderProduct op
        WHERE op.clientOrder.client.id = :clientId
    """)
    List<Product> findProductsByClientId(@Param("clientId") Long clientId);

    @Query(value = """
        SELECT p.* FROM order_product op
        JOIN product p ON op.product_id = p.id
        GROUP BY p.id
        ORDER BY SUM(op.count_product) DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Product> findTopPopularProducts(@Param("limit") int limit);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByName(String name);


}

