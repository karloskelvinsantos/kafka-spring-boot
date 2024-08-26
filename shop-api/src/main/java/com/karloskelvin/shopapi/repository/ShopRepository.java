package com.karloskelvin.shopapi.repository;

import com.karloskelvin.shopapi.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    Shop findByIdentifier(String identifier);
}
