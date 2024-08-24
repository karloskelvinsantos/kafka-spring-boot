package com.karloskelvin.shopapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class ShopDTO {

    private String identifier;
    private String status;
    private LocalDate dateShop;
    private List<ShopItemDTO> items;

    public static ShopDTO convert(Shop shop) {
        var shopDTO = new ShopDTO();
        shopDTO.setIdentifier(shop.getIdentifier());
        shopDTO.setStatus(shop.getStatus());
        shopDTO.setDateShop(shop.getDateShop());
        shopDTO.setItems(shop.getItems()
                        .stream()
                        .map(ShopItemDTO::convert)
                        .collect(Collectors.toList()));

        return shopDTO;
    }
}
