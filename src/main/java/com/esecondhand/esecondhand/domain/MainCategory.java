package com.esecondhand.esecondhand.domain;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "main_category", schema = "public", catalog = "e-second_hand")
public class MainCategory {
    private int id;
    private String type;
    private String destinationSex;
    private String name;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Column(name = "type", nullable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "destination_sex", nullable = false)
    public String getDestinationSex() {
        return destinationSex;
    }

    public void setDestinationSex(String destinationSex) {
        this.destinationSex = destinationSex;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MainCategory that = (MainCategory) o;

        if (id != that.id) return false;
        if (!Objects.equals(type, that.type)) return false;
        if (!Objects.equals(destinationSex, that.destinationSex))
            return false;
        if (!Objects.equals(name, that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (destinationSex != null ? destinationSex.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
