package group2.webapp.FinalProject4.services.implement;

import group2.webapp.FinalProject4.services.UploadFileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class UploadFileServiceImplement implements UploadFileService {
    private final Path storageFolder = Paths.get("uploads");

    public UploadFileServiceImplement() {
        try {
            Files.createDirectories(storageFolder);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store empty file", e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("Checking file...");

            //Check file is empty?
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file");
            }

            //Check file is image?
            if (!isImageFile(file)) {
                throw new RuntimeException("You can only upload image file");
            }

            //Check file size less than 5MB?
            float fileSize = file.getSize() / 1_000_000.0f;
            if (fileSize > 5.0f) {
                throw new RuntimeException("File size must be less than 5MB");
            }

            //File must be renamed before storage
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }
            //Copy file to the destination file path
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return generatedFileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store empty file", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            // list all file in storage folder
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> {
                        // filter file name do not contain "." and "_"
                        return !path.equals(this.storageFolder) && !path.toString().contains("._");
                    })
                    .map(this.storageFolder::relativize);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to load stored files", exception);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return StreamUtils.copyToByteArray(resource.getInputStream());
            } else {
                throw new RuntimeException("Could not read file: " + fileName);
            }

        } catch (IOException ioException) {
            throw new RuntimeException("Could no read file: " + fileName, ioException);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            Path path = Paths.get("uploads/" + fileName);
            Files.delete(path);
            System.out.println("Deleted file name: " + fileName);
        } catch (Exception e) {
            System.out.println("File not found");
            System.out.println("Details");
            e.printStackTrace();
        }
    }


    @Override
    public Boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        assert fileExtension != null;
        return Arrays.asList(new String[]{"png", "jpg", "jpeg", "bmp"}).contains(fileExtension.trim().toLowerCase());
    }
}

