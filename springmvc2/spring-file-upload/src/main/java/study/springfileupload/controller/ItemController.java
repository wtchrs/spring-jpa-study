package study.springfileupload.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import study.springfileupload.domain.Item;
import study.springfileupload.domain.ItemRepository;
import study.springfileupload.domain.UploadFile;
import study.springfileupload.file.FileStore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItemForm(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    @PostMapping("/items/new")
    public String newItemProcess(@ModelAttribute ItemForm form, RedirectAttributes redirect) throws IOException {
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> imageFiles = fileStore.storeFiles(form.getImageFiles());
        Item savedItem = itemRepository.save(new Item(form.getName(), attachFile, imageFiles));
        redirect.addAttribute("id", savedItem.getId());
        return "redirect:/items/{id}";
    }

    @GetMapping("/items/{id}")
    public String itemDetail(Model model, @PathVariable Long id) {
        Optional<Item> findItem = itemRepository.findById(id);
        if (findItem.isEmpty()) throw new IllegalStateException("Not exists item");
        Item item = findItem.get();
        model.addAttribute("item", item);
        return "item-detail";
    }

    @GetMapping("/images/{filename}")
    @ResponseBody
    public Resource image(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> attachFile(@PathVariable Long itemId) throws MalformedURLException {
        Optional<Item> findItem = itemRepository.findById(itemId);

        if (findItem.isEmpty()) throw new IllegalStateException("Not exists item");
        Item item = findItem.get();

        String storeFileName = item.getAttachFile().getStoreFileName();
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        String uploadFileName = item.getAttachFile().getUploadFileName();
        uploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + uploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
