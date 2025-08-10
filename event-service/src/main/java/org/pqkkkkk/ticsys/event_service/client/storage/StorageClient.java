package org.pqkkkkk.ticsys.event_service.client.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageClient {
    public String uploadFile(MultipartFile file);
    public String deleteFile(String fileId);
}
