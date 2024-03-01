package group2.webapp.FinalProject4.controllers;

import group2.webapp.FinalProject4.services.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/uploadfile")
public class UploadFileAPI {
    //Sử dụng @Autowired để tiêm UploadFileService vào trong class.
//UploadFileService chịu trách nhiệm cho việc đọc nội dung của file.
    @Autowired
    UploadFileService uploadFileService;

    //Phương thức readDetailFile nhận một tham số từ đường dẫn URL và trả về một mảng byte.
    @GetMapping(value = "/upload/{file}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String file) {
        try {
            byte[] bytes = uploadFileService.readFileContent(file);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
}
