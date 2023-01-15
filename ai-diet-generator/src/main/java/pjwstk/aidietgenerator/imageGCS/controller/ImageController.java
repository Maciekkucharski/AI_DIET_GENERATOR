package pjwstk.aidietgenerator.imageGCS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.imageGCS.request.ImageRequest;
import pjwstk.aidietgenerator.imageGCS.service.ImageService;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/upload/image/")
public class ImageController {

    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
    @PostMapping("/profile")
    public String uploadProfilePicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException {
        return imageService.uploadImage(imageRequest, "Profile", response);
    }

    @PostMapping("/background")
    public String uploadBackgroundPicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException {
        return imageService.uploadImage(imageRequest, "Background", response);
    }

    @PostMapping("/post")
    public String uploadPostPicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException {
        return imageService.uploadImage(imageRequest, "Post", response);
    }

    @PostMapping("/recipe")
    public String uploadRecipePicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException {
        return imageService.uploadImage(imageRequest, "Recipe", response);
    }
}
