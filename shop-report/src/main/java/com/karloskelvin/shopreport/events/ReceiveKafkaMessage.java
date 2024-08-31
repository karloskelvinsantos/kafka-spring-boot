package com.karloskelvin.shopreport.events;

import com.karloskelvin.shopreport.dto.ShopDTO;
import com.karloskelvin.shopreport.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiveKafkaMessage {

    private static final String SHOP_EVENT_TOPIC_NAME = "SHOP_EVENT_TOPIC";

    private final ReportRepository reportRepository;

    @Transactional
    @KafkaListener(topics = SHOP_EVENT_TOPIC_NAME, groupId = "group_report")
    public void listenShopTopic(ShopDTO shopDTO) {
        try {
            log.info("Compra recebida no tópico: {}", shopDTO.getIdentifier());
            reportRepository.incrementShopStatus(shopDTO.getStatus());
        } catch (Exception e){
            log.error("Erro no processamento da mensagem", e);
        }
    }
}
