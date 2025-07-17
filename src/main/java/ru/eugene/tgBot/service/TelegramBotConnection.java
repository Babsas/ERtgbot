package ru.eugene.tgBot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.eugene.tgBot.entity.*;
import ru.eugene.tgBot.repository.*;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class TelegramBotConnection {

    private final EntitiesService entitiesService;
    private final ClientOrderRepository clientOrderRepository;
    private final OrderProductRepository orderProductRepository;

    private TelegramBot bot;

    public TelegramBotConnection(EntitiesService entitiesService,
                                 ClientOrderRepository clientOrderRepository,
                                 OrderProductRepository orderProductRepository) {
        this.entitiesService = entitiesService;
        this.clientOrderRepository = clientOrderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @PostConstruct
    public void initBot() {
        bot = new TelegramBot("7601005396:AAGTY7iSJ1YG3_quYsiP0JTnowZ7t9_krbE");
        bot.setUpdatesListener(new TelegramUpdatesListener());
    }

    private class TelegramUpdatesListener implements UpdatesListener {
        @Override
        public int process(List<Update> updates) {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }

        private void processUpdate(Update update) {
            try {
                if (update.message() != null) {
                    Message message = update.message();
                    if (message.text() != null) {
                        handleMessage(message);
                    }
                } else if (update.callbackQuery() != null) {
                    handleCallback(update.callbackQuery());
                }
            } catch (Exception e) {
                bot.execute(new SendMessage(update.message().chat().id(),
                        "Произошла ошибка: " + e.getMessage()));
            }
        }

        private void handleMessage(Message message) {
            Long chatId = message.chat().id();
            String text = message.text();

            Client client = getOrCreateClient(message.from(), message.chat());
            getOrCreateActiveOrder(client);

            if (text != null && text.equalsIgnoreCase("/start")) {
                showMainMenuWithReplyKeyboard(chatId);
                return;
            }

            if (text != null && text.equalsIgnoreCase("START")) {
                showMainMenuWithReplyKeyboard(chatId);
                return;
            }

            switch (text) {
                case "🍔 Меню":
                    showMainMenuWithReplyKeyboard(chatId);
                    break;
                case "🛒 Корзина":
                    showCart(chatId);
                    break;
                case "📞 Контакты":
                    showContacts(chatId);
                    break;
                case "ℹ️ Помощь":
                    showHelp(chatId);
                    break;
                default:
                    if (text != null && !text.equalsIgnoreCase("/start")
                            && !text.equalsIgnoreCase("START")) {
                        bot.execute(new SendMessage(chatId,
                                "Используйте кнопки меню"));
                    }
            }
        }


        private void showContacts(Long chatId) {
            String contacts = """
        📍 Наш адрес: ул. Пушкина, 123
        📞 Телефон: +7 (123) 456-78-90
        🕒 Часы работы: 10:00 - 22:00""";

            bot.execute(new SendMessage(chatId, contacts));
        }

        private void showHelp(Long chatId) {
            String helpText = "Помощь по боту ℹ\uFE0F\n\n" +
                    "1. Нажмите \uD83C\uDF54 Меню для выбора блюд\n" +
                    "2. Добавляйте товары в 🛒 Корзину\n" +
                    "3. Оформляйте заказ когда готовы\n\n" +
                    "Для начала заново - /start";

            bot.execute(new SendMessage(chatId, helpText));
        }

        private void handleCallback(CallbackQuery callback) {
            Long chatId = callback.message().chat().id();
            String data = callback.data();

            try {
                if (data.startsWith("category_")) {
                    showCategoryMenu(chatId, Long.parseLong(data.substring(9)));
                } else if (data.startsWith("product_")) {
                    addProductToOrder(chatId, Long.parseLong(data.substring(8)));
                } else if (data.equals("main_menu")) {
                    showMainMenu(chatId, "Главное меню:");
                } else if (data.equals("place_order")) {
                    placeOrder(chatId);
                } else if (data.equals("cart")) {
                    showCart(chatId);
                }
            } catch (Exception e) {

                bot.execute(new SendMessage(chatId, "Произошла ошибка: " + e.getMessage()));
            }
        }


        private Client getOrCreateClient(User user, Chat chat) {
            return entitiesService.findClientByExternalId(user.id())
                    .orElseGet(() -> {
                        Client newClient = new Client();
                        newClient.setExternalId(user.id());
                        newClient.setFullName((user.firstName() + " " +
                                (user.lastName() != null ? user.lastName() : "")).trim());
                        newClient.setPhoneNumber(chat.username() != null ? "@" + chat.username() : "не указан");
                        newClient.setAddress("не указан");
                        return entitiesService.saveClient(newClient);
                    });
        }

        private ClientOrder getOrCreateActiveOrder(Client client) {
            return entitiesService.getClientOrders(client.getId()).stream()
                    .filter(o -> o.getStatus() == 1)
                    .findFirst()
                    .orElseGet(() -> {
                        ClientOrder order = new ClientOrder();
                        order.setClient(client);
                        order.setStatus(1);
                        order.setTotal(0.0);
                        return entitiesService.saveOrder(order);
                    });
        }

        private void updateOrderTotal(ClientOrder order) {
            Double total = orderProductRepository.sumProductsInOrder(order);
            order.setTotal(total != null ? total : 0.0);
            clientOrderRepository.save(order);
        }

        private void showMainMenu(Long chatId, String message) {
            List<Category> rootCategories = entitiesService.getCategoriesByParentId(null);

            List<List<InlineKeyboardButton>> keyboard = rootCategories.stream()
                    .map(c -> List.of(
                            new InlineKeyboardButton(c.getName())
                                    .callbackData("category_" + c.getId())
                    ))
                    .collect(Collectors.toList());

            keyboard.add(List.of(
                    new InlineKeyboardButton("🛒 Корзина").callbackData("cart")
            ));

            sendMenu(chatId, message, keyboard);
        }

        private void showMainMenuWithReplyKeyboard(Long chatId) {
            List<Category> rootCategories = entitiesService.getCategoriesByParentId(null);

            List<List<InlineKeyboardButton>> keyboard = rootCategories.stream()
                    .map(c -> List.of(
                            new InlineKeyboardButton(c.getName())
                                    .callbackData("category_" + c.getId())
                    ))
                    .collect(Collectors.toList());

            keyboard.add(List.of(
                    new InlineKeyboardButton("🛒 Корзина").callbackData("cart")
            ));

            sendMenu(chatId, "Выберите категорию:", keyboard);

            ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup(
                    new KeyboardButton("\uD83C\uDF54 Меню"),
                    new KeyboardButton("\uD83D\uDED2 Корзина")
            )
                    .addRow(
                            new KeyboardButton("\uD83D\uDCDE Контакты"),
                            new KeyboardButton("ℹ\uFE0F Помощь")
                    )
                    .resizeKeyboard(true)
                    .oneTimeKeyboard(false);

            bot.execute(new SendMessage(chatId, "Меню:")
                    .replyMarkup(replyKeyboard));
        }

        private void showCategoryMenu(Long chatId, Long categoryId) {
            Category category = entitiesService.getCategoryById(categoryId);
            List<Category> sub = entitiesService.getCategoriesByParentId(categoryId);
            List<Product> products = entitiesService.getProductsByCategoryId(categoryId);

            StringBuilder text = new StringBuilder("Категория: ").append(category.getName()).append("\n");

            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

            sub.forEach(sc ->
                    keyboard.add(List.of(new InlineKeyboardButton(sc.getName()).callbackData("category_" + sc.getId())))
            );

            products.forEach(p ->
                    keyboard.add(List.of(new InlineKeyboardButton(p.getName() + " - " + p.getPrice() + "₽").callbackData("product_" + p.getId())))
            );

            keyboard.add(List.of(
                    new InlineKeyboardButton("⬅\uFE0F Назад").callbackData("main_menu"),
                    new InlineKeyboardButton("✅ Оформить заказ").callbackData("place_order")
            ));

            sendMenu(chatId, text.toString(), keyboard);
        }

        private void showCart(Long chatId) {
            Client client = entitiesService.findClientByExternalId(chatId)
                    .orElseThrow(() -> new RuntimeException("Клиент не найден"));

            ClientOrder order = getOrCreateActiveOrder(client);
            List<OrderProduct> items = orderProductRepository.findByClientOrder(order);

            if (items.isEmpty()) {
                bot.execute(new SendMessage(chatId, "Ваша корзина пуста!"));
                return;
            }

            StringBuilder text = new StringBuilder("\uD83D\uDED2 Ваша корзина:\n\n");
            for (OrderProduct item : items) {
                Product p = item.getProduct();
                text.append(String.format("• %s ×%d = %.2f₽\n", p.getName(), item.getCountProduct(), p.getPrice() * item.getCountProduct()));
            }
            text.append(String.format("\nИтого: %.2f₽", order.getTotal()));

            List<List<InlineKeyboardButton>> keyboard = List.of(
                    List.of(new InlineKeyboardButton("⬅\uFE0F Продолжить").callbackData("main_menu")),
                    List.of(new InlineKeyboardButton("✅ Оформить заказ").callbackData("place_order"))
            );

            sendMenu(chatId, text.toString(), keyboard);
        }

        private void placeOrder(Long chatId) {
            Client client = entitiesService.findClientByExternalId(chatId)
                    .orElseThrow(() -> new RuntimeException("Клиент не найден"));

            ClientOrder activeOrder = getOrCreateActiveOrder(client);
            List<OrderProduct> items = orderProductRepository.findByClientOrder(activeOrder);

            if (items.isEmpty()) {
                bot.execute(new SendMessage(chatId, "Ваша корзина пуста!"));
                return;
            }

            activeOrder.setStatus(2);
            clientOrderRepository.save(activeOrder);

            ClientOrder newOrder = new ClientOrder();
            newOrder.setClient(client);
            newOrder.setStatus(1);
            newOrder.setTotal(0.0);
            clientOrderRepository.save(newOrder);

            String text = String.format("""
                    ✅ Заказ #%d оформлен!
                    Сумма: %.2f₽
                    Спасибо за покупку!
                    """, activeOrder.getId(), activeOrder.getTotal());

            bot.execute(new SendMessage(chatId, text));
            showMainMenu(chatId, "Хотите что-то ещё?");
        }

        private void addProductToOrder(Long chatId, Long productId) {
            Product product = entitiesService.getProductById(productId);
            Client client = entitiesService.findClientByExternalId(chatId)
                    .orElseThrow(() -> new RuntimeException("Клиент не найден"));
            ClientOrder order = getOrCreateActiveOrder(client);

            OrderProduct orderProduct = orderProductRepository.findByClientOrderAndProduct(order, product)
                    .map(op -> {
                        op.setCountProduct(op.getCountProduct() + 1);
                        return op;
                    })
                    .orElseGet(() -> {
                        OrderProduct newOp = new OrderProduct();
                        newOp.setClientOrder(order);
                        newOp.setProduct(product);
                        newOp.setCountProduct(1L);
                        return newOp;
                    });

            orderProductRepository.save(orderProduct);
            updateOrderTotal(order);

            bot.execute(new SendMessage(chatId, "Добавлен: " + product.getName()));
        }


        private void sendMenu(Long chatId, String text, List<List<InlineKeyboardButton>> keyboard) {
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                    keyboard.stream()
                            .map(row -> row.toArray(new InlineKeyboardButton[0]))
                            .toArray(InlineKeyboardButton[][]::new)
            );
            bot.execute(new SendMessage(chatId, text).replyMarkup(markup));
        }
    }
}
