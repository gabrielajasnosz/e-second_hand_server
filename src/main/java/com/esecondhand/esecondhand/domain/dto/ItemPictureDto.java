package com.esecondhand.esecondhand.domain.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemPictureDto {

   private Long id;
   private String fileUrl;
   private boolean isMainPicture;
}
