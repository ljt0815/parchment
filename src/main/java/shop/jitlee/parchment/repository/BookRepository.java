package shop.jitlee.parchment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.jitlee.parchment.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
