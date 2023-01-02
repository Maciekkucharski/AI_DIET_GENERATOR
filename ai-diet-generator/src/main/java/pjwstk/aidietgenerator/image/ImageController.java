package pjwstk.aidietgenerator.image;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @PostMapping("/profile")
    public void uploadProfileImage(@RequestParam("image")MultipartFile file, HttpServletResponse response){

    }

    @PostMapping("/post")
    public void uploadPostImage(@RequestParam("image")MultipartFile file, HttpServletResponse response){

    }

    @PostMapping("/recipe")
    public void uploadRecipeImage(@RequestParam("image")MultipartFile file, HttpServletResponse response){

    }

    @PostMapping("/background")
    public void uploadBackgroudImage(@RequestParam("image")MultipartFile file, HttpServletResponse response){

    }
}
