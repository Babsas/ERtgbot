package ru.eugene.tgBot;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.eugene.tgBot.entity.Category;
import ru.eugene.tgBot.entity.Product;
import ru.eugene.tgBot.repository.CategoryRepository;
import ru.eugene.tgBot.repository.ProductRepository;

@SpringBootTest
public class FillingTests {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Category pizza;
    private Category rolls;
    private Category burgers;
    private Category drinks;

    private Category classicRolls;
    private Category bakedRolls;
    private Category sweetRolls;
    private Category sets;

    private Category classicBurgers;
    private Category spicyBurgers;

    private Category soda;
    private Category energy;
    private Category juice;
    private Category other;

    @Test
    public void fillDatabase() {
        createCategories();
        createProducts();
    }

    public void createCategories() {
        pizza = saveCategory("Pizza", null);
        rolls = saveCategory("Rolls", null);
        burgers = saveCategory("Burgers", null);
        drinks = saveCategory("Drinks", null);

        classicRolls = saveCategory("Классические роллы", rolls);
        bakedRolls = saveCategory("Запеченные роллы", rolls);
        sweetRolls = saveCategory("Сладкие роллы", rolls);
        sets = saveCategory("Наборы", rolls);

        classicBurgers = saveCategory("Классические бургеры", burgers);
        spicyBurgers = saveCategory("Острые бургеры", burgers);

        soda = saveCategory("Газированные напитки", drinks);
        energy = saveCategory("Энергетические напитки", drinks);
        juice = saveCategory("Соки", drinks);
        other = saveCategory("Другие", drinks);
    }

    public void createProducts() {
        saveProduct(pizza, "Маргарита", "Пицца с томатами и моцареллой", 450.0);
        saveProduct(pizza, "Пепперони", "Пицца с острым салями", 500.0);
        saveProduct(pizza, "Гавайская", "Пицца с ананасами и ветчиной", 480.0);

        saveProduct(rolls, "Калифорния", "Ролл с крабом и авокадо", 320.0);
        saveProduct(classicRolls, "Филадельфия", "Лосось, сыр и огурец", 350.0);
        saveProduct(classicRolls, "Токио", "Угорь, сыр, огурец", 340.0);

        saveProduct(bakedRolls, "Запечённый лосось", "Ролл с лососем и соусом", 370.0);
        saveProduct(bakedRolls, "Запечённый краб", "Краб, рис, сыр", 360.0);
        saveProduct(bakedRolls, "Запечённый угорь", "Угорь, соус, рис", 380.0);

        saveProduct(sweetRolls, "Фруктовый ролл", "Ролл с бананом и клубникой", 290.0);
        saveProduct(sweetRolls, "Шоколадный ролл", "С шоколадом и орехами", 310.0);
        saveProduct(sweetRolls, "Медовый ролл", "С мёдом и сыром", 300.0);

        saveProduct(sets, "Сет №1", "Набор из 4 видов роллов", 1200.0);
        saveProduct(sets, "Сет №2", "Сет с острыми и классическими роллами", 1350.0);
        saveProduct(sets, "Сет семейный", "Набор на 4 персоны", 1500.0);

        saveProduct(classicBurgers, "Чизбургер", "Классический бургер с сыром", 250.0);
        saveProduct(classicBurgers, "Гамбургер", "Бургер без сыра", 230.0);
        saveProduct(classicBurgers, "Двойной бургер", "С двумя котлетами", 310.0);

        saveProduct(spicyBurgers, "Острый бургер", "С перцем чили и острым соусом", 270.0);
        saveProduct(spicyBurgers, "Техасский бургер", "Острый бургер с луком", 290.0);
        saveProduct(spicyBurgers, "Жгучий бургер", "С халапеньо и соусом", 300.0);

        saveProduct(soda, "Кока-Кола", "Газированный напиток", 120.0);
        saveProduct(soda, "Спрайт", "Освежающий лимонад", 115.0);
        saveProduct(soda, "Фанта", "Апельсиновый напиток", 110.0);

        saveProduct(energy, "Red Bull", "Энергетик 250 мл", 150.0);
        saveProduct(energy, "Adrenaline Rush", "Энергетик 250 мл", 140.0);
        saveProduct(energy, "Burn", "Сильный энергетик", 145.0);

        saveProduct(juice, "Яблочный сок", "100% сок, 0.5л", 90.0);
        saveProduct(juice, "Апельсиновый сок", "С мякотью, 0.5л", 95.0);
        saveProduct(juice, "Томатный сок", "Соль и специи, 0.5л", 85.0);

        saveProduct(other, "Вода без газа", "0.5л, чистая вода", 60.0);
        saveProduct(other, "Вода с газом", "0.5л, минеральная", 65.0);
        saveProduct(other, "Кефир", "Напиток кисломолочный", 70.0);

    }

    private Category saveCategory(String name, Category parent) {
        Category category = new Category();
        category.setName(name);
        category.setParent(parent);
        return categoryRepository.save(category);
    }

    private void saveProduct(Category category, String name, String description, Double price) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        productRepository.save(product);
    }
}

