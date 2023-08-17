package shop.jitlee.parchment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.jitlee.parchment.entity.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying
    @Query("delete from Image i where i.id in :ids")
    void deleteImages(@Param("ids") List<Long> ids);
}