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
@RequestMapping("/upload/image/")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
    @PostMapping("/profile")
    public String uploadProfilePicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.uploadImage(imageRequest, "Profile", response);
    }

    @PostMapping("/background")
    public String uploadBackgroundPicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.uploadImage(imageRequest, "Background", response);
    }

    @PostMapping("/post/")
    public String uploadPostPicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.uploadImage(imageRequest, "Post", response);
    }

    @PostMapping("/recipe/")
    public String uploadRecipePicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.uploadImage(imageRequest, "Recipe", response);
    }

    @PostMapping("/profile")
    public String updateProfilePicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.updateImage(imageRequest, "Profile", null, response);
    }

    @PostMapping("/background")
    public String updateBackgroundPicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.updateImage(imageRequest, "Background", null, response);
    }

    @PostMapping("/post/{ID}")
    public String updatePostPicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.updateImage(imageRequest, "Post", @PathVariable(name = "ID") long id, response);
    }

    @PostMapping("/recipe/{ID}")
    public String updateRecipePicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.updateImage(imageRequest, "Recipe", response);
    }

}
