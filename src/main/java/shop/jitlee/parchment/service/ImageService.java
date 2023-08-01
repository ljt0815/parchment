package shop.jitlee.parchment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jitlee.parchment.entity.Image;
import shop.jitlee.parchment.repository.ImageRepository;

import java.awt.*;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void addImage(Image image) {
        imageRepository.save(image);
    }
}
