package scc.srv.search;

import com.microsoft.azure.CloudException;
import com.microsoft.azure.management.Azure;
import com.microsoft.rest.LogLevel;

import java.io.File;
import java.io.IOException;

public class AzureManagementUtils
{
	public static Azure createManagementClient(String authFile) throws CloudException, IOException {
		File credFile = new File(authFile);

		Azure azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(credFile).withDefaultSubscription();
		System.out.println("Azure client created with success");
		return azure;
	}

}
