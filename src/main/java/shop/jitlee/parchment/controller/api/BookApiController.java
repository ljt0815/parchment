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
import shop.jitlee.parchment.service.PdfService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookApiController {

    private final PdfService pdfService;
    private final BookService bookService;

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
        String uuid = (String)map.get("uuid");
        Integer pageTotal = bookService.getPageTotal(uuid);
        Integer convertedPage = bookService.getConvertedPage(uuid);
        Map<String, Object> rtn = new HashMap<>();
        rtn.put("pageTotal", pageTotal);
        rtn.put("convertedPage", convertedPage);

        return new ResponseDto<>(HttpStatus.OK.value(), rtn);
    }
}
