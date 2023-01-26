package pjwstk.aidietgenerator.imageGCS.controller;

import com.google.cloud.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.imageGCS.request.ImageRequest;
import pjwstk.aidietgenerator.imageGCS.service.ImageService;
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

    @PostMapping("/update/profile")
    public String updateProfilePicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.updateImage(imageRequest, "Profile", 0, response);
    }

    @PostMapping("/update/background")
    public String updateBackgroundPicture(@ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.updateImage(imageRequest, "Background", 0, response);
    }

    @PostMapping("/update/post/{ID}")
    public String updatePostPicture(@PathVariable(name = "ID") long ID, @ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.updateImage(imageRequest, "Post", ID, response);
    }

    @PostMapping("/update/recipe/{ID}")
    public String updateRecipePicture(@PathVariable(name = "ID") long ID, @ModelAttribute ImageRequest imageRequest, HttpServletResponse response) throws IOException, StorageException {
        return imageService.updateImage(imageRequest, "Recipe", ID, response);
    }

    @PostMapping("/delete/profile")
    public String deleteProfilePicture( HttpServletResponse response) throws StorageException {
        return imageService.deleteImage("Profile", 0, response);
    }

    @PostMapping("/delete/background")
    public String deleteBackgroundPicture( HttpServletResponse response) throws StorageException {
        return imageService.deleteImage("Background", 0, response);
    }

    @PostMapping("/delete/post/{ID}")
    public String deletePostPicture(@PathVariable(name = "ID") long ID, HttpServletResponse response) throws StorageException {
        return imageService.deleteImage("Post", ID, response);
    }

    @PostMapping("/delete/recipe/{ID}")
    public String deleteRecipePicture(@PathVariable(name = "ID") long ID, HttpServletResponse response) throws StorageException {
        return imageService.deleteImage("Recipe", ID, response);
    }

}
