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

    @Override
    public List<Item> findItems(ItemListFiltersDto itemListFiltersDto, List<Long> categoryIds, List<Long> followedIds) throws ParseException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = builder.createQuery(Item.class);

        Root<Item> itemRoot = query.from(Item.class);
        Join<Item, Category> category = itemRoot.join("category");
        Join<Item, User> user = itemRoot.join("user");
        Join<Item, Brand> brand = itemRoot.join("brand");
        Join<Item, Color> color = itemRoot.join("color");
        Join<Item, Size> size = itemRoot.join("size");

        query.where(createWhereClause(itemRoot, category, brand, color, size, user, builder, itemListFiltersDto, categoryIds, followedIds));

        List<Order> orderList = new ArrayList();
        if (itemListFiltersDto.getSortingOrder().equals("DESC")) {
            orderList.add(builder.desc(itemRoot.get(itemListFiltersDto.getSortingColumn())));
            orderList.add(builder.desc(itemRoot.get("id")));
        } else if (itemListFiltersDto.getSortingOrder().equals("ASC")) {
            orderList.add(builder.asc(itemRoot.get(itemListFiltersDto.getSortingColumn())));
            orderList.add(builder.asc(itemRoot.get("id")));
        }

        query.orderBy(orderList);

        final TypedQuery<Item> finalQuery = entityManager.createQuery(query);
        finalQuery.setMaxResults(itemListFiltersDto.getPageSize() + 1);
        List<Item> result = finalQuery.getResultList();
        return result;
    }

    private Predicate createWhereClause(Root<Item> root, Join<Item, Category> category, Join<Item, Brand> brand, Join<Item, Color> color, Join<Item, Size> size, Join<Item, User> user, CriteriaBuilder builder, ItemListFiltersDto itemListFiltersDto, List<Long> categoryIds, List<Long> followedIds) throws ParseException {
        List<Predicate> predicates = new ArrayList<>();

        if (itemListFiltersDto.getCategoryId() != null) {
            predicates.add(category.get("id").in(categoryIds));
        }

        if(itemListFiltersDto.getUserId() != null){
            predicates.add(builder.equal(user.get("id"), itemListFiltersDto.getUserId()));
        }

        if(itemListFiltersDto.isOnlyFollowedUsers()){
            predicates.add(user.get("id").in(followedIds));
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
        if (itemListFiltersDto.getGender() != null) {
            predicates.add(builder.or(builder.equal(root.get("gender"), Gender.valueOf(itemListFiltersDto.getGender())), builder.equal(root.get("gender"), Gender.valueOf("UNDEFINED"))));
        }

        if (itemListFiltersDto.getMinPrice() != null && itemListFiltersDto.getMaxPrice() != null) {
            predicates.add(builder.and(builder.le(root.get("price"), itemListFiltersDto.getMaxPrice()), builder.ge(root.get("price"), itemListFiltersDto.getMinPrice())));
        }
        if (itemListFiltersDto.getColorId() != null) {
            predicates.add(builder.equal(color.get("id"), itemListFiltersDto.getColorId()));
        }
        if (itemListFiltersDto.getSizeId() != null) {
            predicates.add(builder.equal(size.get("id"), itemListFiltersDto.getSizeId()));
        }

        predicates.add(builder.equal(user.get("enabled"), true));

        predicates.add(builder.equal(root.get("isHidden"), false));
        predicates.add(builder.equal(root.get("isActive"), true));

        Predicate[] predicatesArray = new Predicate[predicates.size()];
        return builder.and(predicates.toArray(predicatesArray));
    }
}

