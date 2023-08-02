package shop.jitlee.parchment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jitlee.parchment.entity.Book;
import shop.jitlee.parchment.entity.Member;
import shop.jitlee.parchment.entity.Pdf;
import shop.jitlee.parchment.repository.BookRepository;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MemberService memberService;

    @Value("${myPath.externalImgStorage}")
    private String imgPath;

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

    public Long findByUuidGetMemberId(String uuid) {
        return bookRepository.findByUuidGetMemberId(uuid);
    }

    public void imgFileResponse(String folderName, String username, String fileName, HttpServletResponse res) {
        Long bookMemberId = findByUuidGetMemberId(folderName);
        Long requestMemberId = memberService.findByUsernameGetId(username);
        File pngFile = null;
        if (bookMemberId == requestMemberId) {
            pngFile = new File(imgPath + folderName + "/" + fileName);
            if (pngFile.isFile()) {
                int fSize = (int)pngFile.length();
                res.setBufferSize(fSize);
                res.setContentType("image/png");
//                res.setHeader("Content-Disposition", "attachment; filename="+fileName+";"); // 경로 접근시 파일 다운로드
                res.setContentLength(fSize);
                ServletOutputStream out = null;
                FileInputStream in = null;
                try {
                    in  = new FileInputStream(pngFile);
                    out = res.getOutputStream();
                    byte[] buf = new byte[8192];  // 8Kbyte 로 쪼개서 보낸다.
                    int bytesread = 0, bytesBuffered = 0;
                    while ((bytesread = in.read(buf)) > -1) {
                        out.write(buf, 0, bytesread);
                        bytesBuffered += bytesread;
                        if (bytesBuffered > 1024 * 1024) { //아웃풋스트림이 1MB 가 넘어가면 flush 해준다.
                            bytesBuffered = 0;
                            out.flush();
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch(Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }
        } else {
            res.setStatus(HttpStatus.NOT_FOUND.value());
            //추후 에러페이지 출력
        }
    }
}
