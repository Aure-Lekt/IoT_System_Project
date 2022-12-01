package lekt.roboticarmconsumer;

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

import lekt.factoryLineCommon.dto.SensorDTO;
import lekt.factoryLineCommon.LineCommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, LineCommonConstants.TEMPLATE_BASE_PACKAGE, LineCommonConstants.CUSTOM_BASE_PACKAGE})
public class RoboticArmConsumerMain implements ApplicationRunner {
    
    //=================================================================================================
	// members
	
    @Autowired
	private ArrowheadService arrowheadService;
    
    @Autowired
	protected SSLProperties sslProperties;
    
    private final Logger logger = LogManager.getLogger(RoboticArmConsumerMain.class);
    
    private final int MaxLoop = 10;
    private final long WaitTime = 15; //in seconds
    private final long ModeChangeTime = 60; // in seconds
    
    //=================================================================================================
	// methods

	//------------------------------------------------------------------------------------------------
    public static void main( final String[] args ) {
    	SpringApplication.run(RoboticArmConsumerMain.class, args);
    }

    //-------------------------------------------------------------------------------------------------
    @Override
	public void run(final ApplicationArguments args) throws Exception {
    	int i = 0; //loop count, Exit condition
    	long time_save1 = System.currentTimeMillis(); //timer
    	long time_save2 = System.currentTimeMillis(); //timer
    	SequenceBools A = new SequenceBools(true,false); //Useful buffer, Exit condition
    	boolean mode = true; //useful variable
    	
    	while((A.getOk())&&(i<MaxLoop)) { 
    		if ((System.currentTimeMillis()-time_save2)>=(ModeChangeTime*1000)) {
    			mode = !mode;
    			time_save2 = System.currentTimeMillis();
    		}
    		if ((System.currentTimeMillis()-time_save1)>=(WaitTime*1000)) {
    			if (mode) {
	    			A = getDetecServiceOrchestrationAndConsumption(1);
	    			System.out.println("Loading : ");
	    			if (A.getOccupation()) {
	    				System.out.println("The robot can load.");
	    			} else {
	    				System.out.println("No pieces on the input lot, the robot can't load.");
	    			}
    			} else {
    				A = getDetecServiceOrchestrationAndConsumption(2);
	    			System.out.println("Unloading : ");
	    			if (!A.getOccupation()) {
	    				System.out.println("The robot can unload.");
	    			} else {
	    				System.out.println("Piece stuck on th outuput slot, the robot can't unload.");
	    			}
    			}
    	    	time_save1 = System.currentTimeMillis();
    	    	i++;
    		}
    	}
    	
    	if(i>=MaxLoop) {
    		System.out.println("Loop ended naturally !");
    	} else {
    		System.out.println("Loop ended due to a reading error !");
    	}
	}
    
    //-------------------------------------------------------------------------------------------------
    public SequenceBools getDetecServiceOrchestrationAndConsumption(int no)  {
    	String ServDef;
    	String DetTag;
    	if (no==1) {
    		ServDef = LineCommonConstants.GET_DETONE_SERVICE_DEFINITION;
    		DetTag = "input convoyer";
    	} else if (no==2) {
    		ServDef = LineCommonConstants.GET_DETTWO_SERVICE_DEFINITION;
    	    DetTag = "output convoyer";
    	} else {
    		throw new IllegalArgumentException("Detector number is invalid !");
    	}
    	
    	
    	logger.info("Orchestration request for " + ServDef + " service:");
    	final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(ServDef)
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
			return new SequenceBools(false,false);
		} else if (orchestrationResponse.getResponse().isEmpty()) {
			logger.info("No provider found during the orchestration");
			return new SequenceBools(false,false);
		} else {
			final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
			validateOrchestrationResult(orchestrationResult, ServDef);
			
			logger.info("Get slot occupation on "+"IN"+" : ");
			final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
			@SuppressWarnings("unchecked")
			final SensorDTO Det = arrowheadService.consumeServiceHTTP(SensorDTO.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(LineCommonConstants.HTTP_METHOD)),
					orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
					getInterface(), token, null, new String[0]);
			
			boolean Det_Value = Det.getDet();
			printOutBool(Det_Value,DetTag);
			return new SequenceBools(true,Det_Value);
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
    
    private void printOutBool(boolean A, String tag) {
		if (A) {
			System.out.println("There is an object at "+tag+" !");
		} else {
			System.out.println("There is no object at "+tag+" !");
		}
    }
}
