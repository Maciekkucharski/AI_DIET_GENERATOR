package pjwstk.aidietgenerator.image;

import com.google.api.services.storage.Storage;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageOptions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class UploadImage {
    public static void uploadObjectFromMemory(
            String projectId,
            String bucketName,
            String objectName,
//            byte[] contents,
            String contentType)
            throws IOException {
////         The ID of your GCP project
//         String projectId = "your-project-id";
//
////         The ID of your GCS bucket
//         String bucketName = "your-unique-bucket-name";
//
////         The ID of your GCS object
//         String objectName = "your-object-name";
//
////         The string of contents you wish to upload
         String contents = "231123";

        Storage storage = (Storage) StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        byte[] content = contents.getBytes(StandardCharsets.UTF_8);
//        storage.create(blobInfo, new ByteArrayInputStream(content));

    }
}
