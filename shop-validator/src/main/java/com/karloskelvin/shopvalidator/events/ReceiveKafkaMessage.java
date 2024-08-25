package com.karloskelvin.shopvalidator.events;

import com.karloskelvin.shopvalidator.dto.ShopDTO;
import com.karloskelvin.shopvalidator.dto.ShopItemDTO;
import com.karloskelvin.shopvalidator.model.Product;
import com.karloskelvin.shopvalidator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiveKafkaMessage {

    private static final String SHOP_TOPIC_NAME = "SHOP_TOPIC";
    private static final String SHOP_EVENT_TOPIC_NAME = "SHOP_EVENT_TOPIC";

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ShopDTO> kafkaTemplate;

    @KafkaListener(topics = SHOP_TOPIC_NAME, groupId = "group")
    public void listenShopTopic(ShopDTO shopDTO) {
        try {
            log.info("Compra recebida no tópico: {}", shopDTO.getIdentifier());

            boolean success = true;
            for (ShopItemDTO item: shopDTO.getItems()) {
                Product product = productRepository.findByIdentifier(item.getProductIdentifier());

                if (!isValidShop(item, product)) {
                    shopError(shopDTO);
                    success = false;
                    break;
                }
            }

            if (success) {
                shopSuccess(shopDTO);
            }
        } catch (Exception e) {
            log.error("Erro no processamento da compra {}", shopDTO.getIdentifier());
        }
    }

    private boolean isValidShop(ShopItemDTO item, Product product) {
        return product != null && product.getAmount() >= item.getAmount();
    }

    private void shopError(ShopDTO shopDTO) {
        log.info("Erro no processamento da compra {}.", shopDTO.getIdentifier());
        shopDTO.setStatus("ERROR");
        kafkaTemplate.send(SHOP_EVENT_TOPIC_NAME, shopDTO);
    }

    private void shopSuccess(ShopDTO shopDTO) {
        log.info("Compra {} efetuada com sucesso.", shopDTO.getIdentifier());
        shopDTO.setStatus("SUCCESS");
        kafkaTemplate.send(SHOP_EVENT_TOPIC_NAME, shopDTO);
    }
}
