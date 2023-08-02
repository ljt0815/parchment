package shop.jitlee.parchment.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.jitlee.parchment.entity.*;
import shop.jitlee.parchment.repository.BookRepository;
import shop.jitlee.parchment.repository.PdfRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final PdfRepository pdfRepository;
    private final BookRepository bookRepository;
    private final ImageService imageService;
    private final BookService bookService;
    private final PageService pageService;

    @Value("${myPath.externalPdfStorage}")
    private String pdfPath;
    @Value("${myPath.externalImgStorage}")
    private String imgPath;
    @Value("${myPath.imgConnectPath}")
    private String imgConnectPath;


    @Transactional
    public Pdf find(Long id) {
        Pdf pdf = pdfRepository.findById(id).orElseGet(()->{
            return null;
        });
        return pdf;
    }

    @Transactional
    public void uploadBook(MultipartFile multipartFile, Map map) {
        map.put("uuid", UUID.randomUUID().toString());
        File dest = new File(pdfPath + map.get("uuid") + ".pdf");
        try {
            multipartFile.transferTo(dest);
        } catch (IOException e) {
            dest.delete();
            e.printStackTrace();
        }
        Pdf pdf = new Pdf(null, multipartFile.getOriginalFilename(), pdfPath + map.get("uuid") + ".pdf", false,0);
        pdfRepository.save(pdf);
        map.put("id", pdf.getId());
    }

    @Async("pdfConvertExecutor")
    public void convertPdf(Pdf pdf) {
        String uuid = bookRepository.getBookUuid(pdf.getId());
        Long bookId = bookRepository.getBookId(pdf.getId());
        Book book = bookService.find(bookId);

        PDDocument document = null;
        try {
            File file = new File(pdf.getPath());
            document = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(document);
            List<BufferedImage> bufferedImages = new ArrayList<>();
            int pageCount = document.getNumberOfPages();
            pdf.setPageTotal(pageCount);
            pdfRepository.save(pdf);
            File folder = new File(imgPath + uuid);
            folder.mkdir();
            for (int page = 0; page < pageCount; page++) {
                BufferedImage bim = renderer.renderImage(page);
                File imgFile = new File(folder.getPath() + "\\" + page + ".png");
                ImageIO.write(bim, "png", imgFile);
                Image image = new Image(null, imgConnectPath + uuid + "/" + page + ".png", null);
                imageService.addImage(image);
                Page myPage = new Page(null, book, image, null);
                pageService.addPage(myPage);
                if (page == 0)
                    if (book.getThumbnail() == null) {
                        book.setThumbnail(image);
                        bookRepository.save(book);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                document.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        pdf.setConverted(true);
        pdfRepository.save(pdf);
    }
}
