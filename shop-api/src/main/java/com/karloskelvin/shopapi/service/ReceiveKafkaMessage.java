package com.karloskelvin.shopapi.service;

import com.karloskelvin.shopapi.model.Shop;
import com.karloskelvin.shopapi.model.ShopDTO;
import com.karloskelvin.shopapi.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiveKafkaMessage {

    private final ShopRepository shopRepository;
    private static final String SHOP_EVENT_TOPIC_NAME = "SHOP_EVENT_TOPIC";

    @KafkaListener(topics = SHOP_EVENT_TOPIC_NAME, groupId = "group")
    public void listenShopEvents(ShopDTO shopDTO) {
        try {
            log.info("Status da compra recebida no tópico: {}.", shopDTO.getIdentifier());

            Shop shop = shopRepository.findByIdentifier(shopDTO.getIdentifier());
            shop.setStatus(shopDTO.getStatus());
            shopRepository.save(shop);
        } catch (Exception e) {
            log.error("Erro no processamento da compra {}.", shopDTO.getIdentifier());
        }

    }
}
