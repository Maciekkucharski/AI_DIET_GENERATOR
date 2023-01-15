package pjwstk.aidietgenerator.imageGCS.service;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.imageGCS.request.ImageRequest;
import pjwstk.aidietgenerator.repository.PostRepository;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.service.UserDetailsService;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {

    private UserDetailsService userDetailsService;
    private RecipeRepository recipeRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public ImageService(UserDetailsService userDetailsService,
                        RecipeRepository recipeRepository,
                        PostRepository postRepository,
                        UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.recipeRepository = recipeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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
            Blob blob = storage.create(blobInfo, imageRequest.getMultipartFile().getBytes());
            response.setStatus(HttpStatus.OK.value());
            return URL;
        } catch (StorageException e){
            response.setStatus(e.getCode());
            return e.getMessage();
        }
    }

    // BlobId b = BlobId.of(bucketName, blobName);
    // boolean deleted = storage.delete(b);
}
