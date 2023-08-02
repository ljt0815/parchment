package shop.jitlee.parchment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.jitlee.parchment.entity.Book;

import java.util.Collection;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b.uuid FROM Book b where b.pdf.id = :pdfId")
    String getBookUuid(Long pdfId);

    @Query("Select b.id FROM Book b where b.pdf.id = :pdfId")
    Long getBookId(Long pdfId);

    @Query("Select m.id From Book b inner join Member m on b.member.id = m.id where b.uuid = :uuid")
    Long findByUuidGetMemberId(String uuid);
    Optional<Book> findByUuid(String uuid);
}
