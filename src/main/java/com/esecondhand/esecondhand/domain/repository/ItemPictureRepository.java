package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.entity.ItemPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemPictureRepository extends JpaRepository<ItemPicture, Long> {

    @Query("SELECT ip FROM ItemPicture ip WHERE ip.item.id in ?1 and ip.isMainPicture = true")
    List<ItemPicture> findMainImagesByItemId(List<Long> itemIds);

    @Query("SELECT ip.id FROM ItemPicture ip WHERE ip.item.id = ?1 and ip.isMainPicture = true")
    Long findMainImageIdByItemId(Long itemId);

    @Query("SELECT ip FROM ItemPicture ip WHERE ip.item.id = ?1 and ip.isMainPicture = true")
    ItemPicture findMainImageUrlByItemId(long itemId);


}
