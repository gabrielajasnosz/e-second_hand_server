package com.esecondhand.esecondhand.dto;

import java.util.Collection;


public class MainCategoryDto {
    private int id;
    private String type;
    private String destinationSex;
    private String name;
    private Collection<SubcategoryDto> subcategories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDestinationSex() {
        return destinationSex;
    }

    public void setDestinationSex(String destinationSex) {
        this.destinationSex = destinationSex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<SubcategoryDto> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Collection<SubcategoryDto> subcategories) {
        this.subcategories = subcategories;
    }

}
