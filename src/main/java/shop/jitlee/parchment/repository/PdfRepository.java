package shop.jitlee.parchment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.jitlee.parchment.entity.Pdf;

import java.util.Optional;

public interface PdfRepository extends JpaRepository<Pdf, Long> {
    Optional<Pdf> findById(Long id);
}