package pjwstk.aidietgenerator.imageGCS.service;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.entity.UserExtras;
import pjwstk.aidietgenerator.imageGCS.request.ImageRequest;
import pjwstk.aidietgenerator.repository.PostRepository;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.repository.UserExtrasRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.service.UserDetailsService;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    private final UserDetailsService userDetailsService;
    private final RecipeRepository recipeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final UserExtrasRepository userExtrasRepository;

    @Autowired
    public ImageService(UserDetailsService userDetailsService,
                        RecipeRepository recipeRepository,
                        PostRepository postRepository,
                        UserRepository userRepository,
                        UserExtrasRepository userExtrasRepository) {
        this.userDetailsService = userDetailsService;
        this.recipeRepository = recipeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userExtrasRepository = userExtrasRepository;
    }

    String projectId = "foodie-369621";
    String bucketName = "foodie-images";
    Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

    public String uploadImage(ImageRequest imageRequest, String folder, HttpServletResponse response) throws IOException, StorageException {
        String fileName = "images/" + folder + "/" + UUID.randomUUID();
        String URL = "https://storage.cloud.google.com/foodie-images/" + fileName;
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("multipart").build();
        try {
            Blob blob = storage.create(blobInfo, imageRequest.getImage().getBytes());
            response.setStatus(HttpStatus.OK.value());
            return URL;
        } catch (StorageException e){
            response.setStatus(e.getCode());
            return e.getMessage();
        }
    }

    public String updateImage(ImageRequest imageRequest, String folder, Optional<Long> ID, HttpServletResponse response) throws IOException, StorageException {
        boolean deleted = false;
        if(folder.equals("Profile")){
            User existingUser = userDetailsService.findCurrentUser();
            String imagePath = existingUser.getImagePath();
            if(imagePath != null) {
                String fileName = imagePath.replace("https://storage.cloud.google.com/foodie-images/", "");
                BlobId blobId = BlobId.of(bucketName, fileName);
                deleted = storage.delete(blobId);
            }
        } else if (folder.equals("Background")) {
            UserExtras existingUserExtras = userExtrasRepository.findByuser(userDetailsService.findCurrentUser());
            String imagePath = existingUserExtras.getBackground_image();
            if(imagePath != null) {
                String fileName = imagePath.replace("https://storage.cloud.google.com/foodie-images/", "");
                BlobId blobId = BlobId.of(bucketName, fileName);
                deleted = storage.delete(blobId);
            }
        } else if (folder.equals("Post")) {
            Optional<Post> existingPost = postRepository.findById(ID.get());
            if(existingPost.isPresent()){
                String imagePath = existingPost.get().getImagePath();
                if(imagePath != null){
                    String fileName = imagePath.replace("https://storage.cloud.google.com/foodie-images/", "");
                    BlobId blobId = BlobId.of(bucketName, fileName);
                    deleted = storage.delete(blobId);
                }
            }
        } else if (folder.equals("Recipe")) {
            Optional<Recipe> existingRecipe = recipeRepository.findById(ID.get());
            if(existingRecipe.isPresent()){
                String imagePath = existingRecipe.get().getImagePath();
                if(imagePath != null){
                    String fileName = imagePath.replace("https://storage.cloud.google.com/foodie-images/", "");
                    BlobId blobId = BlobId.of(bucketName, fileName);
                    deleted = storage.delete(blobId);
                }
            }
        }
        if(deleted){
            return uploadImage(imageRequest, folder, response);
        } else {
            response.setStatus(HttpStatus.CONFLICT.value());
            return "Error occurred. Image was not updated";
        }
    }
}
