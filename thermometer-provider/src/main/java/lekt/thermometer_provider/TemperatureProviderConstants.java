package lekt.thermometer_provider;

public class TemperatureProviderConstants {
	
	//=================================================================================================
	// members
	
	public static final String BASE_PACKAGE = "ai.aitia";
	
	//public static final String CREATE_TEMP_SERVICE_DEFINITION = "create-temp";
	public static final String GET_TEMP_SERVICE_DEFINITION = "get-temp";
	
	public static final String INTERFACE_SECURE = "HTTP-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String HTTP_METHOD = "http-method";
	public static final String TEMP_URI = "/temperature";
	public static final String BY_ID_PATH = "/{id}";
	public static final String PATH_VARIABLE_ID = "id";
	
	public static final String REQUEST_PARAM_KEY_TIME = "request-param-time";
	public static final String REQUEST_PARAM_TIME = "time";
	public static final String REQUEST_PARAM_KEY_VALUE = "request-param-value";
	public static final String REQUEST_PARAM_VALUE = "value";	
	
	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	private TemperatureProviderConstants() {
		throw new UnsupportedOperationException();
	}
}
