package shop.jitlee.parchment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.jitlee.parchment.entity.Page;

public interface PageRepository extends JpaRepository<Page, Long> {
}