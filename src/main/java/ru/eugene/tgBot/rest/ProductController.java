package ru.eugene.tgBot.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.eugene.tgBot.entity.Product;
import ru.eugene.tgBot.service.EntitiesService;

import java.util.List;

@RestController
@RequestMapping("/rest/products")
public class ProductController {

    private final EntitiesService entitiesService;

    public ProductController(EntitiesService entitiesService) {
        this.entitiesService = entitiesService;
    }

    // /rest/products/search?categoryId=...
    // /rest/products/search?name=...
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam(required = false) Long categoryId,
                                        @RequestParam(required = false) String name) {
        if (categoryId != null) {
            return entitiesService.getProductsByCategoryId(categoryId);
        } else if (name != null) {
            return entitiesService.searchProductsByName(name);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Укажите categoryId или name");
    }

    // /rest/products/popular?limit=5
    @GetMapping("/popular")
    public List<Product> getTopPopularProducts(@RequestParam(defaultValue = "5") Integer limit) {
        return entitiesService.getTopPopularProducts(limit);
    }
}

