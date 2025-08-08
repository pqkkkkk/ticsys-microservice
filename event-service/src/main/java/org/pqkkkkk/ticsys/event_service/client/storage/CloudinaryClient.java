package org.pqkkkkk.ticsys.event_service.client.storage;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryClient implements StorageClient {
    private final Cloudinary cloudinary;

    public CloudinaryClient(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    @Override
    public String uploadFile(MultipartFile file) {
        try{
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String fileId = (String) uploadResult.get("public_id");
            if (fileId == null) {
                throw new Exception("Failed to upload file");
            }
            return fileId;
        }
        catch(Exception e){
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public String deleteFile(String fileId) {
        try{
            Map<?, ?> deleteResult = cloudinary.uploader().destroy(fileId, ObjectUtils.emptyMap());
            String result = (String) deleteResult.get("result");
            if (!"ok".equals(result)) {
                throw new Exception("Failed to delete file");
            }
            return fileId;
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

}
