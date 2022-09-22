package scc.srv;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public class BlobStorageFactory {

    public static CloudBlobContainer createClientToBlobStorage(String blobKey) throws URISyntaxException, StorageException, InvalidKeyException {
        CloudStorageAccount storageAccount =
                CloudStorageAccount.parse(blobKey);
        CloudBlobClient blobClient =
                storageAccount.createCloudBlobClient();

        return blobClient.getContainerReference("images");
    }
}
