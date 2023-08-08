package shop.jitlee.parchment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.jitlee.parchment.entity.Page;

public interface PageRepository extends JpaRepository<Page, Long> {
    @Query("select i.imgPath from Page p " +
            "inner join Image i on i.id = p.image.id " +
            "inner join Book b on b.id = p.book.id " +
            "where p.pageNo = :pageNo and " +
            "b.uuid = :uuid")
    String findByPageNoUuidGetImgPath(Integer pageNo, String uuid);
}