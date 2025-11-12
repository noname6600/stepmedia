package com.thanhvh.shopmanagement.modules.shop.repository;

import com.thanhvh.shopmanagement.modules.shop.entity.ShopEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The interface Shop repository.
 */
@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, UUID> {

    /**
     * Find by filter page.
     *
     * @param name     the name
     * @param location the location
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = """
            SELECT a FROM ShopEntity a
                WHERE (:name IS NULL OR LOWER(UNACCENT(a.name)) LIKE LOWER(UNACCENT(CONCAT('%',:name,'%'))))
                AND (:location IS NULL OR LOWER(UNACCENT(a.location)) LIKE LOWER(UNACCENT(CONCAT('%',:location,'%'))))
                ORDER BY a.createdDate DESC
                """)
    Page<ShopEntity> findByFilter(String name, String location, Pageable pageable);

    /**
     * Search page.
     *
     * @param searchText the search text
     * @param pageable   the pageable
     * @return the page
     */
    @Query(value = """
            SELECT a FROM ShopEntity a
                WHERE (LOWER(UNACCENT(a.name)) LIKE LOWER(UNACCENT(CONCAT('%',:searchText,'%')))
                    OR LOWER(UNACCENT(a.location)) LIKE LOWER(UNACCENT(CONCAT('%',:searchText,'%')))
                    OR :searchText IS NULL)
                ORDER BY a.createdDate DESC
                """)
    Page<ShopEntity> search(String searchText, Pageable pageable);
}
