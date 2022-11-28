package lekt.detectortwo_provider;

public class DetectorTwoProviderConstants {
	
	//=================================================================================================
	// members
	
	public static final String BASE_PACKAGE = "ai.aitia";

	public static final String GET_DETEC_SERVICE_DEFINITION = "get-odet";
	
	public static final String INTERFACE_SECURE = "HTTP-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String HTTP_METHOD = "http-method";
	
	public static final String DETEC_URI = "/detectortwo";
	
	public static final String BY_ID_PATH = "/{id}";
	public static final String PATH_VARIABLE_ID = "id";
	
	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	private DetectorTwoProviderConstants() {
		throw new UnsupportedOperationException();
	}
}
