package com.esecondhand.esecondhand.mapper;
import com.esecondhand.esecondhand.domain.MainCategory;
import com.esecondhand.esecondhand.domain.Subcategory;
import com.esecondhand.esecondhand.dto.MainCategoryDto;
import com.esecondhand.esecondhand.dto.SubcategoryDto;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class CategoryMapper {
    public List<MainCategoryDto> mapToCategoryDto(List<MainCategory> entity){
        return entity.stream().map(e -> {
            MainCategoryDto dto = new MainCategoryDto();
            dto.setId(e.getId());
            dto.setDestinationSex(e.getDestinationSex());
            dto.setName(e.getName());
            dto.setSubcategories(new ArrayList<>());
            dto.setType(e.getType());
            return dto;
        }).collect(Collectors.toList());
    }
    public List<SubcategoryDto> mapToSubcategoryDto(List<Subcategory> entity){
        return entity.stream().map(e -> {
            SubcategoryDto dto = new SubcategoryDto();
            dto.setId(e.getId());
            dto.setDestinationSex(e.getDestinationSex());
            dto.setName(e.getName());
            dto.setMainCategoryId(e.getMainCategoryId());
            return dto;
        }).collect(Collectors.toList());
    }
}
