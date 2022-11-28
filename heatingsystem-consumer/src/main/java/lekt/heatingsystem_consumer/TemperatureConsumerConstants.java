package lekt.heatingsystem_consumer;

public class TemperatureConsumerConstants {
	
	//=================================================================================================
	// members
	
	public static final String BASE_PACKAGE = "ai.aitia";
	
	public static final String INTERFACE_SECURE = "HTTP-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String HTTP_METHOD = "http-method";
	
	public static final String GET_TEMP_SERVICE_DEFINITION = "get-temp";
	
	public static final String REQUEST_PARAM_KEY_TIME = "request-param-time";
	public static final String REQUEST_PARAM_KEY_VALUE = "request-param-value";
	
	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	private TemperatureConsumerConstants() {
		throw new UnsupportedOperationException();
	}

}
