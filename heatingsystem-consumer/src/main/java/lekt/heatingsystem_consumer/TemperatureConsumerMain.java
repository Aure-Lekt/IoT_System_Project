package lekt.heatingsystem_consumer;

//import java.util.List;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
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

import lekt.factoryLine_common.dto.TemperatureResponseDTO;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, TemperatureConsumerConstants.BASE_PACKAGE, "lekt"})
public class TemperatureConsumerMain implements ApplicationRunner {
    
    //=================================================================================================
	// members
	
    @Autowired
	private ArrowheadService arrowheadService;
    
    @Autowired
	protected SSLProperties sslProperties;
    
    private final Logger logger = LogManager.getLogger(TemperatureConsumerMain.class);
    private final DateFormat df = new SimpleDateFormat("dd/MM/yy - HH:mm:ss");
    
    //=================================================================================================
	// methods

	//------------------------------------------------------------------------------------------------
    public static void main( final String[] args ) {
    	SpringApplication.run(TemperatureConsumerMain.class, args);
    }

    //-------------------------------------------------------------------------------------------------
    @Override
	public void run(final ApplicationArguments args) throws Exception {
    	getCarServiceOrchestrationAndConsumption();
	}
    
    //-------------------------------------------------------------------------------------------------
    public void getCarServiceOrchestrationAndConsumption() {
    	logger.info("Orchestration request for " + TemperatureConsumerConstants.GET_TEMP_SERVICE_DEFINITION + " service:");
    	final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(TemperatureConsumerConstants.GET_TEMP_SERVICE_DEFINITION)
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
		} else if (orchestrationResponse.getResponse().isEmpty()) {
			logger.info("No provider found during the orchestration");
		} else {
			final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
			validateOrchestrationResult(orchestrationResult, TemperatureConsumerConstants.GET_TEMP_SERVICE_DEFINITION);
			
			logger.info("Get most recent temperature log :");
			final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
			@SuppressWarnings("unchecked")
			final TemperatureResponseDTO Temp = arrowheadService.consumeServiceHTTP(TemperatureResponseDTO.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(TemperatureConsumerConstants.HTTP_METHOD)),
																					orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
																					getInterface(), token, null, new String[0]);
			//printOut(Temp);
			printOut2(Temp);
		}
    }
    
    //=================================================================================================
	// assistant methods
    
    //-------------------------------------------------------------------------------------------------
    private String getInterface() {
    	return sslProperties.isSslEnabled() ? TemperatureConsumerConstants.INTERFACE_SECURE : TemperatureConsumerConstants.INTERFACE_INSECURE;
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
