package shop.jitlee.parchment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.jitlee.parchment.entity.Member;
import shop.jitlee.parchment.entity.Pdf;
import shop.jitlee.parchment.repository.PdfRepository;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final PdfRepository pdfRepository;


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
        File dest = new File("D:\\uploads\\" + map.get("uuid") + ".pdf");
        try {
            multipartFile.transferTo(dest);
        } catch (IOException e) {
            dest.delete();
            e.printStackTrace();
        }
        Pdf pdf = new Pdf(null, multipartFile.getOriginalFilename(), "D:\\uploads\\" + map.get("uuid") + ".pdf");
        pdfRepository.save(pdf);
        map.put("id", pdf.getId());
    }
}
