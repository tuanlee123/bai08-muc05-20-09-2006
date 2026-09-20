package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.iotstar.util.Constant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
public class ImageController {

    @Value("${app.upload.dir:" + Constant.DIR + "}")
    private String uploadDir;

    @GetMapping("/image")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@RequestParam(name = "fname", required = false) String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = "default_avatar.png";
        }

        File file = new File(uploadDir, fileName);

        // 1. Nếu file tồn tại trong thư mục upload bên ngoài
        if (file.exists() && file.isFile()) {
            Resource resource = new FileSystemResource(file);
            String mimeType = getMimeType(file);
            MediaType mediaType = (mimeType != null) ? MediaType.parseMediaType(mimeType) : MediaType.IMAGE_JPEG;

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .contentType(mediaType)
                    .body(resource);
        }

        // 2. Fallback: Nếu không tìm thấy file ngoài ổ đĩa, thử lấy ảnh mặc định trong static/images/
        Resource defaultResource = new ClassPathResource("static/images/avatar.jpg");
        if (defaultResource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"avatar.jpg\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(defaultResource);
        }

        return ResponseEntity.notFound().build();
    }

    private String getMimeType(File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            return null;
        }
    }
}