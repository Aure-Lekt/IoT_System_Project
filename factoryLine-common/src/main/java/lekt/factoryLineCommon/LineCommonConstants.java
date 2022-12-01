package lekt.factoryLineCommon;

public final class LineCommonConstants {

	public static final String TEMPLATE_BASE_PACKAGE = "ai.aitia";
	public static final String CUSTOM_BASE_PACKAGE = "lekt";

	public static final String INTERFACE_SECURE = "HTTP-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String HTTP_METHOD = "http-method";
	
	public static final String DETONE_URI = "/detone";
	public static final String DETTWO_URI = "/dettwo";
	public static final String TEMP_URI = "/temperature";
	
	public static final String GET_DETONE_SERVICE_DEFINITION = "get-detone";
	public static final String GET_DETTWO_SERVICE_DEFINITION = "get-dettwo";
	public static final String GET_TEMP_SERVICE_DEFINITION = "get-temp";
	public static final String FORCE_TEMP_SERVICE_DEFINITION = "force-temp";
	
	public static final String REQUEST_PARAM_KEY_TIME = "request-param-time";
	public static final String REQUEST_PARAM_TIME = "time";
	public static final String REQUEST_PARAM_KEY_VALUE = "request-param-value";
	public static final String REQUEST_PARAM_VALUE = "value";	
	
	//=================================================================================================
	//methods

	//-------------------------------------------------------------------------------------------------
	private LineCommonConstants() {
		throw new UnsupportedOperationException();
	}
	
}
