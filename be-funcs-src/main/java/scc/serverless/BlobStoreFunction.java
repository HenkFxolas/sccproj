package scc.serverless;

import com.microsoft.azure.functions.annotation.*;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import scc.srv.BlobStorageFactory;

import com.microsoft.azure.functions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * Azure Functions with Timer Trigger.
 */
public class BlobStoreFunction
{

    @FunctionName("blobReplication")
	public void run(
			@BlobTrigger(name = "blob", dataType = "binary", path = "images/{name}", connection = "BlobStoreConnection") byte[] content,
			@BindingName("name") String blobname, final ExecutionContext context) {
		try {
			String blobKey = System.getenv("RemoteRegionBlobStoreConnection");
			CloudBlobContainer container = BlobStorageFactory.createClientToBlobStorage(blobKey);
			CloudBlob blob = container.getBlockBlobReference(blobname);

			System.out.println("\nUploading to Blob storage as blob:\n\t" + blobname);

			// Upload the blob
			blob.uploadFromByteArray(content, 0, content.length);

		} catch (StorageException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
