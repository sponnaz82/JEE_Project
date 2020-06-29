package fr.esgi.secureupload.files.adapters.helpers;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import fr.esgi.secureupload.files.port.StorageFileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StorageFileHandlerImpl implements StorageFileHandler {

    @Value("${AZURE_STORAGE_CONNECTION_STRING}")
    private String connectStr;

    @Value("${AZURE_STORAGE_CONTAINER_NAME}")
    private String containerStr;

    BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectStr).buildClient();

    BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerStr);

    @Override
    public boolean deleteFile(String id) {
        try{
            BlobClient blobClient = containerClient.getBlobClient(id);
            blobClient.delete();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @Override
    public boolean storeFile(InputStream file, long size, String id) {
        try {
            BlobClient blobClient = containerClient.getBlobClient(id);
            blobClient.upload(file, size,true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}