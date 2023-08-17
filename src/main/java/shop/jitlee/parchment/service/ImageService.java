package shop.jitlee.parchment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jitlee.parchment.entity.Image;
import shop.jitlee.parchment.repository.ImageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void addImage(Image image) {
        imageRepository.save(image);
    }

    @Transactional
    public void deleteImages(List<Long> ids) {
        imageRepository.deleteImages(ids);
    }
}
