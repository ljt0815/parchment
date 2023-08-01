package shop.jitlee.parchment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.jitlee.parchment.entity.Book;

import java.util.Collection;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b.uuid FROM Book b where b.pdf.id = :pdfId")
    String getBookUuid(Long pdfId);

    @Query("Select b.id FROM Book b where b.pdf.id = :pdfId")
    Long getBookId(Long pdfId);
}
