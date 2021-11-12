package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.dto.ItemListFiltersDto;
import com.esecondhand.esecondhand.domain.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ItemRepositoryImpl implements ItemRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private String ISO_DATE_FORMAT_ZERO_OFFSET = "yyyy-MM-dd'T'HH:mm:ss.SS";
    private String UTC_TIMEZONE_NAME = "UTC";

    @Override
    public List<Item> findItems(ItemListFiltersDto itemListFiltersDto) throws ParseException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = builder.createQuery(Item.class);

        Root<Item> itemRoot = query.from(Item.class);
        Join<Item, Category> category = itemRoot.join("category");
        Join<Item, Brand> brand = itemRoot.join("brand");
        Join<Item, Color> color = itemRoot.join("color");
        Join<Item, Size> size = itemRoot.join("size");

        query.where(createWhereClause(itemRoot, category, brand, color, size, builder, itemListFiltersDto));

        if (itemListFiltersDto.getSortingOrder().equals("DESC")) {
            query.orderBy(builder.desc(itemRoot.get(itemListFiltersDto.getSortingColumn())));
        } else if (itemListFiltersDto.getSortingOrder().equals("ASC")) {
            query.orderBy(builder.asc(itemRoot.get(itemListFiltersDto.getSortingColumn())));
        }

        final TypedQuery<Item> finalQuery = entityManager.createQuery(query);
        finalQuery.setMaxResults(itemListFiltersDto.getPageSize() + 1);
        List<Item> result = finalQuery.getResultList();
        return result;
    }

    private Predicate createWhereClause(Root<Item> root, Join<Item, Category> category, Join<Item, Brand> brand, Join<Item, Color> color, Join<Item, Size> size, CriteriaBuilder builder, ItemListFiltersDto itemListFiltersDto) throws ParseException {
        List<Predicate> predicates = new ArrayList<>();

        if (itemListFiltersDto.getCategoryId() != null) {
            predicates.add(builder.equal(category.get("id"), itemListFiltersDto.getCategoryId()));
        }
        if (itemListFiltersDto.getNextItemId() != null) {
            Expression<Boolean> e1;
            Expression<Boolean> e2;

            if (itemListFiltersDto.getSortingOrder().equals("DESC")) {
                if (itemListFiltersDto.getSortingColumn().equals("creationDate")) {
                    predicates.add(builder.lessThanOrEqualTo(root.get("id"), itemListFiltersDto.getNextItemId()));
                } else {
                    e1 = builder.lessThan(root.get(itemListFiltersDto.getSortingColumn()), itemListFiltersDto.getNextItemValue());
                    e2 = builder.and(builder.equal(root.get(itemListFiltersDto.getSortingColumn()), itemListFiltersDto.getNextItemValue()), builder.le(root.get("id"), itemListFiltersDto.getNextItemId()));
                    predicates.add(builder.or(e1, e2));
                }
            } else {
                if (itemListFiltersDto.getSortingColumn().equals("creationDate")) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("id"), itemListFiltersDto.getNextItemId()));
                } else {
                    e1 = builder.greaterThan(root.get(itemListFiltersDto.getSortingColumn()), itemListFiltersDto.getNextItemValue());
                    e2 = builder.and(builder.equal(root.get(itemListFiltersDto.getSortingColumn()), itemListFiltersDto.getNextItemValue()), builder.ge(root.get("id"), itemListFiltersDto.getNextItemId()));
                    predicates.add(builder.or(e1, e2));
                }
            }


        }

        if (itemListFiltersDto.getBrandId() != null) {
            predicates.add(builder.equal(brand.get("id"), itemListFiltersDto.getBrandId()));
        }
        if (itemListFiltersDto.getColorId() != null) {
            predicates.add(builder.equal(color.get("id"), itemListFiltersDto.getColorId()));
        }
        if (itemListFiltersDto.getSizeId() != null) {
            predicates.add(builder.equal(size.get("id"), itemListFiltersDto.getSizeId()));
        }

        Predicate[] predicatesArray = new Predicate[predicates.size()];
        return builder.and(predicates.toArray(predicatesArray));
    }
}

