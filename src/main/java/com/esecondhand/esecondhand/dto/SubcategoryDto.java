package com.esecondhand.esecondhand.dto;

public class SubcategoryDto {
    private int id;
    private String destinationSex;
    private String name;
    private int mainCategoryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(int mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

}
