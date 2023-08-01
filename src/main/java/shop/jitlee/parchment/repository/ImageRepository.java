package shop.jitlee.parchment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.jitlee.parchment.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}