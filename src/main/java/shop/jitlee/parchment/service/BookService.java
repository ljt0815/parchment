package shop.jitlee.parchment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jitlee.parchment.entity.Book;
import shop.jitlee.parchment.entity.Member;
import shop.jitlee.parchment.entity.Pdf;
import shop.jitlee.parchment.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MemberService memberService;

    @Transactional
    public Book find(Long id) {
        Book book = bookRepository.findById(id).orElseGet(()->{
            return null;
        });
        return book;
    }

    @Transactional
    public void addBook(Book book, String username, Pdf pdf) {
        Member member = memberService.find(username);
        book.setMember(member);
        book.setPdf(pdf);
        bookRepository.save(book);
    }


}