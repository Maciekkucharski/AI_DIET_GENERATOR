package pjwstk.aidietgenerator.imageGCS.controller;

import com.google.cloud.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.UserExtras;
import pjwstk.aidietgenerator.imageGCS.request.ImageRequest;
import pjwstk.aidietgenerator.imageGCS.service.ImageService;
import pjwstk.aidietgenerator.repository.PostRepository;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.repository.UserExtrasRepository;
import pjwstk.aidietgenerator.service.UserDetailsService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
    @PostMapping("/upload/profile")
    public String uploadProfilePicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.uploadImage(imageRequest, "Profile", response);
    }

    @PostMapping("/upload/background")
    public String uploadBackgroundPicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.uploadImage(imageRequest, "Background", response);
    }

    @PostMapping("/upload/post")
    public String uploadPostPicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.uploadImage(imageRequest, "Post", response);
    }

    @PostMapping("/upload/recipe")
    public String uploadRecipePicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.uploadImage(imageRequest, "Recipe", response);
    }
}
