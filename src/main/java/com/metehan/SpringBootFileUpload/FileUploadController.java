package com.metehan.SpringBootFileUpload;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private Environment env;

    @GetMapping("/upload-form")
    public String showUploadForm(Model model) {
        model.addAttribute("fileUploadForm", new FileUploadForm());
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute FileUploadForm fileUploadForm, RedirectAttributes redirectAttributes) {
        MultipartFile file = fileUploadForm.getFile();

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Lütfen bir dosya seçin.");
            return "redirect:/file/upload-form";
        }

        String uploadDirectory = env.getProperty("upload.directory");

        try {
            Path directoryPath = Paths.get(uploadDirectory);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            //String fileName = StringUtils.cleanPath(file.getOriginalFilename()); Dosya adı temizleme
            Path filePath = Paths.get(uploadDirectory, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            redirectAttributes.addFlashAttribute("message", "Dosya başarıyla yüklendi!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Dosya yükleme sırasında bir hata oluştu.");
        }

        return "redirect:/file/upload-form";
    }

    // <<<----AJAX KISMI---->>>
    @GetMapping("/upload-form-ajax")
    public String uploadFormAjax() {
        return "ajax_upload";
    }

    @PostMapping("/upload-ajax")
    @ResponseBody
    public String uploadFileAjax(@RequestParam("file") MultipartFile file,
                                 @RequestParam("description") String description) {
        if (file.isEmpty()) {
            return "Dosya seçilmedi!";
        }

        try {
            //Dosya dizin temizleme StringUtils.cleanPath()
            /*
            Örneğin, kullanıcının yüklediği dosyanın adı ../../gizli_veriler.txt olsaydı,
            bu kod dosya adını güvenli bir şekilde gizli_veriler.txt olarak temizler.
            */
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            // Dosya yüklenecek dizin
            String uploadDir = "C:/Users/metehan/Desktop/uploads/";

            Path directoryPath = Paths.get(uploadDir);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            // Dosya yolu oluşturma
            String filePath = uploadDir + fileName;

            // Dosyayı kaydet
            file.transferTo(new File(filePath));

            return "Dosya başarıyla yüklendi: " + fileName + ", Açıklama: " + description;
        } catch (IOException e) {
            return "Dosya yükleme hatası: " + e.getMessage();
        }
    }

}