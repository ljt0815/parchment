package shop.jitlee.parchment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.jitlee.parchment.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b.uuid FROM Book b where b.pdf.id = :pdfId")
    String getBookUuid(Long pdfId);

    @Query("select b.id from Book b where b.pdf.id = :pdfId")
    Long getBookId(Long pdfId);

    @Query("select m.id from Book b inner join Member m on b.member.id = m.id where b.uuid = :uuid")
    Long findByUuidGetMemberId(String uuid);
    @Query("select p.pageTotal from Book b inner join Pdf p on b.pdf.id = p.id where b.uuid = :uuid")
    Integer getPageTotal(String uuid);

    @Query("select count(p.id) from Book b inner join Page p on b.id = p.book.id where b.uuid = :uuid")
    Integer getConvertedPage(String uuid);
}
