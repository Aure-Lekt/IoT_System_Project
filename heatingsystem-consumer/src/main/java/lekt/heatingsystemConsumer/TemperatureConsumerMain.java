package lekt.heatingsystemConsumer;

//import java.util.List;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.shared.OrchestrationFlags.Flag;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO.Builder;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.dto.shared.ServiceInterfaceResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.exception.InvalidParameterException;

import lekt.factoryLineCommon.dto.TemperatureResponseDTO;
import lekt.factoryLineCommon.dto.TemperatureRequestDTO;
import lekt.factoryLineCommon.LineCommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, LineCommonConstants.TEMPLATE_BASE_PACKAGE, LineCommonConstants.CUSTOM_BASE_PACKAGE})
@PropertySource("classpath:application.properties")
public class TemperatureConsumerMain implements ApplicationRunner {
    
    //=================================================================================================
	// members
	
    @Autowired
	private ArrowheadService arrowheadService;
    
    @Autowired
	protected SSLProperties sslProperties;
    
    private final Logger logger = LogManager.getLogger(TemperatureConsumerMain.class);
    
    // custom variables
    @Value("${custom.maxloop:10}") 
    private int MaxLoop;
    
    @Value("${custom.waittime:15}") 
    private long WaitTime; //in seconds
    
    private final DateFormat df = new SimpleDateFormat("dd/MMM/yy - HH:mm:ss");
    
    @Value("${custom.usejoke}") 
    private boolean UseJoke; //in seconds
    
    private final double UselessTemp = 1001.5;
    private final long UselessTime = Long.parseLong("1000210440000"); // dark humour warning !
    
    //=================================================================================================
	// methods

	//------------------------------------------------------------------------------------------------
    public static void main( final String[] args ) {
    	SpringApplication.run(TemperatureConsumerMain.class, args);
    }

    //-------------------------------------------------------------------------------------------------
    @Override
	public void run(final ApplicationArguments args) throws Exception {
    	System.out.println("\n Check : MaxLoop = "+MaxLoop+" / WaitTime = "+WaitTime+" / Joke = "+UseJoke+"\n");
    	
    	int i = 0; //exit condition
    	boolean ok = true; //exit condition
    	long time = System.currentTimeMillis();
    	
    	if(UseJoke) {
	    	ok = forceTempServiceOrchestrationAndConsumption();
	    	System.out.println("\n Now it's time to get real ! \n");
    	}
    	
    	while((i<MaxLoop)&&(ok)) {
    		if ((System.currentTimeMillis()-time)>=(WaitTime*1000)) {
    			ok = getTempServiceOrchestrationAndConsumption();
    			time=System.currentTimeMillis();
    			i++;
    		}
    	}
    	
    	if(i>=MaxLoop) {
    		System.out.println("\n System stopped naturally \n");
    	} else {
    		System.out.println("\n System stopped due to lack of reponse from the provider/cloud \n");
    	}
	}
    
    //-------------------------------------------------------------------------------------------------
    public boolean getTempServiceOrchestrationAndConsumption() {
    	logger.info("Orchestration request for " + LineCommonConstants.GET_TEMP_SERVICE_DEFINITION + " service:");
    	final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(LineCommonConstants.GET_TEMP_SERVICE_DEFINITION)
    																		.interfaces(getInterface())
    																		.build();
    	
		final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
		final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder.requestedService(serviceQueryForm)
																					   .flag(Flag.MATCHMAKING, true)
																					   .flag(Flag.OVERRIDE_STORE, true)
																					   .build();
		
		printOut(orchestrationFormRequest);		
		
		final OrchestrationResponseDTO orchestrationResponse = arrowheadService.proceedOrchestration(orchestrationFormRequest);
		
		logger.info("Orchestration response:");
		printOut(orchestrationResponse);		
		
		if (orchestrationResponse == null) {
			logger.info("No orchestration response received");
			return false;
		} else if (orchestrationResponse.getResponse().isEmpty()) {
			logger.info("No provider found during the orchestration");
			return false;
		} else {
			final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
			validateOrchestrationResult(orchestrationResult, LineCommonConstants.GET_TEMP_SERVICE_DEFINITION);
			
			logger.info("Get most recent temperature log :");
			final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
			@SuppressWarnings("unchecked")
			final TemperatureResponseDTO Temp = arrowheadService.consumeServiceHTTP(TemperatureResponseDTO.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(LineCommonConstants.HTTP_METHOD)),
																					orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
																					getInterface(), token, null, new String[0]);
			printOut2(Temp);
			return true;
		}
    }
    
    //-------------------------------------------------------------------------------------------------
    public boolean forceTempServiceOrchestrationAndConsumption() {
    	logger.info("Orchestration request for " + LineCommonConstants.FORCE_TEMP_SERVICE_DEFINITION + " service:");
    	final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(LineCommonConstants.FORCE_TEMP_SERVICE_DEFINITION)
    																		.interfaces(getInterface())
    																		.build();
		final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
		final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder.requestedService(serviceQueryForm)
																					   .flag(Flag.MATCHMAKING, true)
																					   .flag(Flag.OVERRIDE_STORE, true)
																					   .build();
		printOut(orchestrationFormRequest);		
		final OrchestrationResponseDTO orchestrationResponse = arrowheadService.proceedOrchestration(orchestrationFormRequest);
		logger.info("Orchestration response:");
		printOut(orchestrationResponse);		
		
		if (orchestrationResponse == null) {
			logger.info("No orchestration response received");
			return false;
		} else if (orchestrationResponse.getResponse().isEmpty()) {
			logger.info("No provider found during the orchestration");
			return false;
		} else {
			final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
			validateOrchestrationResult(orchestrationResult, LineCommonConstants.FORCE_TEMP_SERVICE_DEFINITION);
			
			System.out.println("Temprature "+UselessTemp+" °C at timeStamp = "+UselessTime);
			final TemperatureRequestDTO forcedTemp = new TemperatureRequestDTO(UselessTime,UselessTemp);
			
			logger.info("Sending redquest :");
			final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
			
			final TemperatureResponseDTO tempCreated = arrowheadService.consumeServiceHTTP(TemperatureResponseDTO.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(LineCommonConstants.HTTP_METHOD)),
					orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
					getInterface(), token, forcedTemp, new String[0]);
			logger.info("Provider response : ");
			printOut2(tempCreated);
			return true;
			}			
    }
    
    //=================================================================================================
	// assistant methods
    
    //-------------------------------------------------------------------------------------------------
    private String getInterface() {
    	return sslProperties.isSslEnabled() ? LineCommonConstants.INTERFACE_SECURE : LineCommonConstants.INTERFACE_INSECURE;
    }
    
    //-------------------------------------------------------------------------------------------------
    private void validateOrchestrationResult(final OrchestrationResultDTO orchestrationResult, final String serviceDefinitin) {
    	if (!orchestrationResult.getService().getServiceDefinition().equalsIgnoreCase(serviceDefinitin)) {
			throw new InvalidParameterException("Requested and orchestrated service definition do not match");
		}
    	
    	boolean hasValidInterface = false;
    	for (final ServiceInterfaceResponseDTO serviceInterface : orchestrationResult.getInterfaces()) {
			if (serviceInterface.getInterfaceName().equalsIgnoreCase(getInterface())) {
				hasValidInterface = true;
				break;
			}
		}
    	if (!hasValidInterface) {
    		throw new InvalidParameterException("Requested and orchestrated interface do not match");
		}
    }
    
    //-------------------------------------------------------------------------------------------------
    private void printOut(final Object object) {
    	System.out.println(Utilities.toPrettyJson(Utilities.toJson(object)));
    }
    
    private void printOut2(final TemperatureResponseDTO dto) {
		Date sampleDate = new Date(dto.getTime());
		String dt = df.format(sampleDate);
		String dv = String.format("%.2f",dto.getValue());
		System.out.println("\n"+dt+ "  :  T° = "+dv+"°C"+"\n");
    }
    
}
