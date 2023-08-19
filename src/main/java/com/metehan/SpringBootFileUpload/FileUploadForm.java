package com.metehan.SpringBootFileUpload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class FileUploadForm {
    @NotNull(message = "Dosya seçiniz")
    private MultipartFile file;

    @Size(max = 100, message = "Açıklama en fazla 100 karakter olmalıdır")
    private String description;

}

