package shop.jitlee.parchment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import shop.jitlee.parchment.entity.Page;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {
    @Query("select i.imgPath from Page p " +
            "inner join Image i on i.id = p.image.id " +
            "inner join Book b on b.id = p.book.id " +
            "where p.pageNo = :pageNo and " +
            "b.uuid = :uuid")
    String findByPageNoUuidGetImgPath(Integer pageNo, String uuid);

    @Query("select p from Page p " +
            "inner join Book b on b.id = p.book.id " +
            "where b.id = :bookId")
    List<Page> getBookPages(Long bookId);

    @Query("select i.id from Page p " +
            "inner join Book b on b.id = p.book.id " +
            "inner join Image i on i.id = p.image.id " +
            "where b.id = :bookId")
    List<Long> getPagesImageId(Long bookId);

    @Modifying
    @Query("delete from Page p " +
            "where p.book.id = :bookId")
    void deletePages(Long bookId);
}