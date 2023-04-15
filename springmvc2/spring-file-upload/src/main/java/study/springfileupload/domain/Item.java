package study.springfileupload.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Item {

    private Long id;
    private String name;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;

    public Item(String name, UploadFile attachFile, List<UploadFile> imageFiles) {
        this.name = name;
        this.attachFile = attachFile;
        this.imageFiles = imageFiles;
    }
}
