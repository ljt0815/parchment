package shop.jitlee.parchment.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import shop.jitlee.parchment.config.auth.PrincipalDetails;
import shop.jitlee.parchment.dto.AddBookDto;
import shop.jitlee.parchment.dto.ResponseDto;
import shop.jitlee.parchment.entity.Book;
import shop.jitlee.parchment.entity.Pdf;
import shop.jitlee.parchment.service.BookService;
import shop.jitlee.parchment.service.PageService;
import shop.jitlee.parchment.service.PdfService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookApiController {

    private final PdfService pdfService;
    private final BookService bookService;
    private final PageService pageService;

    @PostMapping("/book/upload")
    public ResponseDto<Map> bookUpload(@RequestParam("file") MultipartFile multipartFile) {
        Map<String, String> map = new HashMap<>();
        pdfService.uploadBook(multipartFile, map);

        return new ResponseDto<>(HttpStatus.OK.value(), map);
    }

    @PostMapping("/book/add")
    public ResponseDto<Integer> bookAdd(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @RequestBody AddBookDto addBookDto) {
        Book book = addBookDto.toEntity();
        Pdf pdf = pdfService.find(addBookDto.getId());
        bookService.addBook(book, principalDetails.getUsername(), pdf);
        pdfService.convertPdf(pdf);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/book/getConvertProgress")
    public ResponseDto<Map<String, Object>> getConvertProgress(@RequestBody Map<String, Object> map) {
        Map<String, Object> rtn = new HashMap<>();
        String uuid = (String)map.get("uuid");
        rtn.put("pageTotal", bookService.getPageTotal(uuid));
        rtn.put("convertedPage", bookService.getConvertedPage(uuid));

        return new ResponseDto<>(HttpStatus.OK.value(), rtn);
    }

    @PostMapping("/book/getPagePath")
    public ResponseDto<Map<String, Object>> getPagePath(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody Map<String, Object> map) {
        String requestUsername = principalDetails.getUsername();
        Map<String, Object> rtn = new HashMap<>();
        String uuid = (String)map.get("uuid");
        String flag = (String)map.get("flag");
        String type = (String)map.get("type");
        Book book = bookService.find(bookService.findByUuidGetBookId(uuid));
        if (!book.getMember().getUsername().equals(requestUsername)) {
            return new ResponseDto<>(HttpStatus.NOT_FOUND.value(), rtn);
        }
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
            bookService.save(book);
        } else if (flag.equals("previous")) {
            if (type.equals("pc"))
                currentPage -= 2;
            else if (type.equals("mobile"))
                currentPage -= 1;
            if (currentPage < 0)
                currentPage = 0;
            book.setCurrentPage(currentPage);
            bookService.save(book);
        }
        //페이지에서 이미지 경로 2개 가져오기
        String imgPath1 = pageService.findByPageNoUuidGetImgPath(currentPage, uuid);
        String imgPath2 = pageService.findByPageNoUuidGetImgPath(currentPage + 1, uuid);

        rtn.put("page1Path", imgPath1);
        rtn.put("page2Path", imgPath2);
        return new ResponseDto<>(HttpStatus.OK.value(), rtn);
    }
}
