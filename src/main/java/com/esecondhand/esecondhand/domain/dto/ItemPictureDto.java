package com.esecondhand.esecondhand.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPictureDto {

   private Long id;
   private String fileUrl;
   private boolean isMainPicture;
}
