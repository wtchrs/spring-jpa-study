package study.springfileupload.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemForm {

    private String name;
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles;
}
