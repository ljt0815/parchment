package shop.jitlee.parchment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jitlee.parchment.entity.Page;
import shop.jitlee.parchment.repository.PageRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;

    @Transactional
    public void addPage(Page page) {
        pageRepository.save(page);
    }

    public String findByPageNoUuidGetImgPath(Integer pageNo, String uuid) {
        return pageRepository.findByPageNoUuidGetImgPath(pageNo, uuid);
    }

    @Transactional
    public void deletePages(Long bookId) {
        pageRepository.deletePages(bookId);
    }

    public List<Long> getPagesImageId(Long bookId) {
        return pageRepository.getPagesImageId(bookId);
    }
}
