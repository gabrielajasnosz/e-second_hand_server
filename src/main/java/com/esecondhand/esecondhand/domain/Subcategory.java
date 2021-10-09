package com.esecondhand.esecondhand.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "subcategory", schema = "public", catalog = "e-second_hand")
public class Subcategory {
    private int id;
    private String destinationSex;
    private String name;
    private int mainCategoryId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "destination_sex", nullable = false, length = -1)
    public String getDestinationSex() {
        return destinationSex;
    }

    public void setDestinationSex(String destinationSex) {
        this.destinationSex = destinationSex;
    }

    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "main_category_id", nullable = false)
    public int getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(int mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subcategory that = (Subcategory) o;

        if (id != that.id) return false;
        if (!Objects.equals(destinationSex, that.destinationSex))
            return false;
        if (!Objects.equals(name, that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (destinationSex != null ? destinationSex.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
