package pjwstk.aidietgenerator.imageGCS.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.imageGCS.repository.BackgroundImageRepository;
import pjwstk.aidietgenerator.imageGCS.repository.PostImageRepository;
import pjwstk.aidietgenerator.imageGCS.repository.ProfileImageRepository;
import pjwstk.aidietgenerator.imageGCS.repository.RecipeImageRepository;
import pjwstk.aidietgenerator.imageGCS.request.ImageRequest;
import pjwstk.aidietgenerator.service.UserDetailsService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class ImageService {

    private BackgroundImageRepository backgroundImageRepository;
    private ProfileImageRepository profileImageRepository;
    private PostImageRepository postImageRepository;
    private RecipeImageRepository recipeImageRepository;
    private UserDetailsService userDetailsService;

    @Autowired
    public ImageService(BackgroundImageRepository backgroundImageRepository,
                        ProfileImageRepository profileImageRepository,
                        PostImageRepository postImageRepository,
                        RecipeImageRepository recipeImageRepository,
                        UserDetailsService userDetailsService) {
        this.backgroundImageRepository = backgroundImageRepository;
        this.profileImageRepository = profileImageRepository;
        this.postImageRepository = postImageRepository;
        this.recipeImageRepository = recipeImageRepository;
        this.userDetailsService = userDetailsService;
    }

    String projectId = "foodie-369621";
    Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
    String bucketName = "foodie-images"; // Change this to something unique

    public String uploadImage(ImageRequest imageRequest, String folder, HttpServletResponse response) throws IOException {
        String fileName = "images/" + folder + "/" + imageRequest.getMultipartFile().getName();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("multipart").build();
        Blob blob = storage.create(blobInfo, imageRequest.getMultipartFile().getBytes());

        return "xxx";
    }

    public String updateImage(ImageRequest imageRequest, String folder, HttpServletResponse response){

        return "xxx";
    }
}
