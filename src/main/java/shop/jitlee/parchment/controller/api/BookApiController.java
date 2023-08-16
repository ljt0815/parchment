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
        Map<String, Object> rtn = new HashMap<>();
        String uuid = (String)map.get("uuid");
        rtn.put("pageTotal", bookService.getPageTotal(uuid));
        rtn.put("convertedPage", bookService.getConvertedPage(uuid));

        return new ResponseDto<>(HttpStatus.OK.value(), rtn);
    }

    @PostMapping("/book/getPagePath")
    public ResponseDto<Map<String, Object>> getPagePath(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody Map<String, Object> map) {
        Map<String, Object> rtn = new HashMap<>();
        bookService.imgFilePathResponse(principalDetails.getUsername(), map, rtn);
        if (rtn.isEmpty())
            return new ResponseDto<>(HttpStatus.NOT_FOUND.value(), rtn);
        return new ResponseDto<>(HttpStatus.OK.value(), rtn);
    }

    @PostMapping("/book/rename")
    public ResponseDto<Integer> renameBook(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody Map<String, Object> map) {
        if (!bookService.updateTitle(principalDetails.getUsername(), map))
            return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), -1);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/book/delete")
    public ResponseDto<Integer> deleteBook(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody Map<String, Object> map) {
        if (!bookService.deleteBook(principalDetails.getUsername(), map))
            return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), -1);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
