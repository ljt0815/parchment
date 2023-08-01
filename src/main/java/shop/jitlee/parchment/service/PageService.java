package shop.jitlee.parchment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jitlee.parchment.entity.Page;
import shop.jitlee.parchment.repository.PageRepository;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;

    @Transactional
    public void addPage(Page page) {
        pageRepository.save(page);
    }

}
