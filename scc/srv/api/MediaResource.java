package scc.srv.api;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import scc.data.daos.Hash;
import scc.utils.AzureProperties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.InvalidKeyException;


@Path("/media")
public class MediaResource
{
	private static final String BLOB_KEY = AzureProperties.getProperty(AzureProperties.BLOB_KEY);

	private CloudBlobContainer container;

	public MediaResource() throws StorageException, InvalidKeyException, URISyntaxException {
		container = createClientToBlobStorage();
	}

	private CloudBlobContainer createClientToBlobStorage() throws URISyntaxException, StorageException, InvalidKeyException {
		CloudStorageAccount storageAccount =
				CloudStorageAccount.parse(BLOB_KEY);
		CloudBlobClient blobClient =
				storageAccount.createCloudBlobClient();

		return blobClient.getContainerReference("images");
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.APPLICATION_JSON)
	public Response upload(byte[] contents) throws IOException, StorageException, URISyntaxException {
		String photoId = Hash.of(contents);
		//CloudBlob blob = container.getBlockBlobReference(photoId);

		System.out.println("\nUploading to Blob storage as blob:\n\t" + photoId);

		File path = new File("/mnt/vol/"+photoId);
		Files.write(path.toPath(),contents);
		// Upload the blob
		//blob.uploadFromByteArray(contents, 0, contents.length);

		return Response.ok().entity(photoId).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@PathParam("id") String id) throws StorageException, IOException, URISyntaxException {
		// Get reference to blob
		//CloudBlob blob = container.getBlobReferenceFromServer(id);
		//ByteArrayOutputStream out = new ByteArrayOutputStream();
		//blob.download(out);
		//out.close();
		//byte[] contents = out.toByteArray();
		File path = new File("/mnt/vol/"+id);
		byte[] contents = Files.readAllBytes(path.toPath());

		return Response.ok().entity(contents).build();
	}

}
