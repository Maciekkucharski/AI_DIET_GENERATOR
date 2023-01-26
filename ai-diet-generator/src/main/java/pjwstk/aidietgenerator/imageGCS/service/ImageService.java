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
        String URL = "https://storage.googleapis.com/foodie-images/" + fileName;
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("multipart").build();
        try {
            storage.create(blobInfo, imageRequest.getImage().getBytes());
            response.setStatus(HttpStatus.OK.value());
            return URL;
        } catch (StorageException e) {
            response.setStatus(e.getCode());
            return e.getMessage();
        }
    }

    public String updateImage(ImageRequest imageRequest, String folder, long ID, HttpServletResponse response) throws IOException, StorageException {
        boolean deleted = delete(folder, ID);
        if (deleted) {
            String newURL = uploadImage(imageRequest, folder, response);
            User currentUser = userDetailsService.findCurrentUser();
            if (newURL.contains("https://storage.googleapis.com/foodie-images/")) {
                switch (folder) {
                    case "Profile": {
                        currentUser.setImagePath(newURL);
                        userRepository.save(currentUser);
                        response.setStatus(HttpStatus.OK.value());
                        return "Profile image updated.";
                    }
                    case "Background": {
                        UserExtras existingUserExtras = userExtrasRepository.findByuser(currentUser);
                        existingUserExtras.setBackgroundImagePath(newURL);
                        userExtrasRepository.save(existingUserExtras);
                        response.setStatus(HttpStatus.OK.value());
                        return "Background image updated.";
                    }
                    case "Post": {
                        Optional<Post> existingPost = postRepository.findById(ID);
                        if (existingPost.isPresent() && existingPost.get().getUser() == currentUser) {
                            existingPost.get().setImagePath(newURL);
                            postRepository.save(existingPost.get());
                        }
                        response.setStatus(HttpStatus.OK.value());
                        return "Post image updated.";
                    }
                    case "Recipe": {
                        Optional<Recipe> existingRecipe = recipeRepository.findById(ID);
                        if (existingRecipe.isPresent() && existingRecipe.get().getUser() == currentUser) {
                            existingRecipe.get().setImagePath(newURL);
                            recipeRepository.save(existingRecipe.get());
                        }
                        response.setStatus(HttpStatus.OK.value());
                        return "Recipe image updated.";
                    }
                }
            } else {
                response.setStatus(HttpStatus.CONFLICT.value());
                return "Error occurred. Image was not updated";
            }
        }
        response.setStatus(HttpStatus.CONFLICT.value());
        return "Error occurred. Image was not updated";
    }

    public String deleteImage(String folder, long ID, HttpServletResponse response) {
        boolean deleted = delete(folder, ID);
        if (deleted) {
            response.setStatus(HttpStatus.OK.value());
            return "Image deleted.";
        } else {
            response.setStatus(HttpStatus.CONFLICT.value());
            return "Error occurred. Image was not deleted";
        }
    }

    private boolean delete(String folder, long ID) {
        boolean deleted = false;
        User currentUser = userDetailsService.findCurrentUser();
        switch (folder) {
            case "Profile": {
                String imagePath = currentUser.getImagePath();
                if (imagePath != null && imagePath.contains("https://storage.googleapis.com/foodie-images/")) {
                    String fileName = imagePath.replace("https://storage.googleapis.com/foodie-images/", "");
                    BlobId blobId = BlobId.of(bucketName, fileName);
                    deleted = storage.delete(blobId);
                    if (deleted) {
                        currentUser.setImagePath(null);
                        userRepository.save(currentUser);
                    }
                }
                break;
            }
            case "Background": {
                UserExtras existingUserExtras = userExtrasRepository.findByuser(currentUser);
                String imagePath = existingUserExtras.getBackgroundImagePath();
                if (imagePath != null && imagePath.contains("https://storage.googleapis.com/foodie-images/")) {
                    String fileName = imagePath.replace("https://storage.googleapis.com/foodie-images/", "");
                    BlobId blobId = BlobId.of(bucketName, fileName);
                    deleted = storage.delete(blobId);
                    if (deleted) {
                        existingUserExtras.setBackgroundImagePath(null);
                        userExtrasRepository.save(existingUserExtras);
                    }
                }
                break;
            }
            case "Post":
                Optional<Post> existingPost = postRepository.findById(ID);
                if (existingPost.isPresent()) {
                    String imagePath = existingPost.get().getImagePath();
                    if (imagePath != null && imagePath.contains("https://storage.googleapis.com/foodie-images/") && existingPost.get().getUser() == currentUser) {
                        String fileName = imagePath.replace("https://storage.googleapis.com/foodie-images/", "");
                        BlobId blobId = BlobId.of(bucketName, fileName);
                        deleted = storage.delete(blobId);
                        if (deleted) {
                            existingPost.get().setImagePath(null);
                            postRepository.save(existingPost.get());
                        }
                    }
                }
                break;
            case "Recipe":
                Optional<Recipe> existingRecipe = recipeRepository.findById(ID);
                if (existingRecipe.isPresent()) {
                    String imagePath = existingRecipe.get().getImagePath();
                    if (imagePath != null && imagePath.contains("https://storage.googleapis.com/foodie-images/") && existingRecipe.get().getUser() == currentUser) {
                        String fileName = imagePath.replace("https://storage.googleapis.com/foodie-images/", "");
                        BlobId blobId = BlobId.of(bucketName, fileName);
                        deleted = storage.delete(blobId);
                        if (deleted) {
                            existingRecipe.get().setImagePath(null);
                            recipeRepository.save(existingRecipe.get());
                        }
                    }
                }
                break;
        }
        return deleted;
    }
}
