package shop.jitlee.parchment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jitlee.parchment.dto.ResponseDto;
import shop.jitlee.parchment.entity.Book;
import shop.jitlee.parchment.entity.Member;
import shop.jitlee.parchment.entity.Pdf;
import shop.jitlee.parchment.repository.BookRepository;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MemberService memberService;
    private final PageService pageService;

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
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void addBook(Book book, String username, Pdf pdf) {
        Member member = memberService.find(username);
        book.setMember(member);
        book.setPdf(pdf);
        book.setCurrentPage(0);
        bookRepository.save(book);
    }

    public Long findByUuidGetMemberId(String uuid) {
        return bookRepository.findByUuidGetMemberId(uuid);
    }

    public Long findByUuidGetBookId(String uuid) {
        return bookRepository.findByUuidGetBookId(uuid);
    }

    public Integer getPageTotal(String uuid){
        return bookRepository.getPageTotal(uuid);
    }

    public Integer getConvertedPage(String uuid) {
        return bookRepository.getConvertedPage(uuid);
    }

    public void imgFilePathResponse(String username, Map<String, Object> map, Map<String, Object> rtn) {
        String uuid = (String)map.get("uuid");
        String flag = (String)map.get("flag");
        String type = (String)map.get("type");
        Book book = find(findByUuidGetBookId(uuid));
        if (!book.getMember().getUsername().equals(username))
            return ;
        Integer currentPage = book.getCurrentPage();
        Integer totalPage = book.getPdf().getPageTotal();

        if (flag.equals("next")) {
            if (type.equals("pc"))
                currentPage += 2;
            else if (type.equals("mobile"))
                currentPage += 1;
            if (currentPage >= totalPage)
                currentPage = totalPage - 1;
            book.setCurrentPage(currentPage);
            save(book);
        } else if (flag.equals("previous")) {
            if (type.equals("pc"))
                currentPage -= 2;
            else if (type.equals("mobile"))
                currentPage -= 1;
            if (currentPage < 0)
                currentPage = 0;
            book.setCurrentPage(currentPage);
            save(book);
        }
        //페이지에서 이미지 경로 2개 가져오기
        String imgPath1 = pageService.findByPageNoUuidGetImgPath(currentPage, uuid);
        String imgPath2 = pageService.findByPageNoUuidGetImgPath(currentPage + 1, uuid);

        rtn.put("page1Path", imgPath1);
        rtn.put("page2Path", imgPath2);
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

    public Boolean updateTitle(String username, Map<String, Object> map) {
        String uuid = (String)map.get("uuid");
        String beRenamedTitle = (String)map.get("title");
        Long bookId = findByUuidGetBookId(uuid);
        if (bookId == null)
            return false;
        Book book = find(bookId);
        if (!username.equals(book.getMember().getUsername()))
            return false;
        book.setTitle(beRenamedTitle);
        save(book);
        return true;
    }

    public boolean deleteBook(String username, Map<String, Object> map) {
        String uuid = (String)map.get("uuid");
        Long bookId = findByUuidGetBookId(uuid);
        if (bookId == null)
            return false;
        Book book = find(bookId);
        if (!username.equals(book.getMember().getUsername()))
            return false;
        bookRepository.deleteById(bookId);
        return true;
    }
}
