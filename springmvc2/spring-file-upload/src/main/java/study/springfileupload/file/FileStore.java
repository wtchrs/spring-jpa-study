package study.springfileupload.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import study.springfileupload.domain.UploadFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> result = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            UploadFile uploadFile = storeFile(multipartFile);
            if (uploadFile != null) result.add(uploadFile);
        }
        return result;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) return null;

        String originalFilename = multipartFile.getOriginalFilename();
        String internalFilename = createRandomUUIDFilename(originalFilename);
        multipartFile.transferTo(new File(getFullPath(internalFilename)));
        return new UploadFile(originalFilename, internalFilename);
    }

    private static String createRandomUUIDFilename(String originalFilename) {
        String ext = getExt(originalFilename);

        String uuidFilename = UUID.randomUUID().toString();
        if (ext != null) uuidFilename += "." + ext;
        log.info("uuidFilename = {}", uuidFilename);

        return uuidFilename;
    }

    private static String getExt(String filename) {
        int pos = filename.lastIndexOf('.');
        if (pos == -1) return null;
        return filename.substring(pos + 1);
    }
}
