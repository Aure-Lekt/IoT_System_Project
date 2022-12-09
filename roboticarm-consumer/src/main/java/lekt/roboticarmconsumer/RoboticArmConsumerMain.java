package lekt.roboticarmconsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.PropertySource;

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
import lekt.factoryLineCommon.dto.TrayGetDTO;
import lekt.factoryLineCommon.dto.TrayPostDTO;
import lekt.factoryLineCommon.boolSequences.sensorBoolSequence;
import lekt.factoryLineCommon.boolSequences.trayBoolSequence;
import lekt.factoryLineCommon.LineCommonConstants;  

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, LineCommonConstants.TEMPLATE_BASE_PACKAGE, LineCommonConstants.CUSTOM_BASE_PACKAGE})
@PropertySource("classpath:application.properties")
public class RoboticArmConsumerMain implements ApplicationRunner {
    
    //=================================================================================================
	// members
	
    @Autowired
	private ArrowheadService arrowheadService;
    
    @Autowired
	protected SSLProperties sslProperties;
    
    private final Logger logger = LogManager.getLogger(RoboticArmConsumerMain.class);
    
    
    // custom variables
    @Value("${custom.maxloop:10}") 
    private int MaxLoop;
    
    @Value("${custom.maxretry:10}") 
    private int MaxRetry;
    
    @Value("${custom.waittime:15}") 
    private long WaitTime; //in seconds
    
    @Value("${custom.traychecktime:40}") 
    private long TrayCheckTime; // in seconds
    
    private final String[] DetectServiceName = {LineCommonConstants.GET_DETONE_SERVICE_DEFINITION,LineCommonConstants.GET_DETTWO_SERVICE_DEFINITION};
    private final String[] RebotProcessMessage = {"Loading Piece in the tray : ","Unloading piece from th tray"};
    
    //=================================================================================================
	// methods

	//------------------------------------------------------------------------------------------------
    public static void main( final String[] args ) {
    	SpringApplication.run(RoboticArmConsumerMain.class, args);
    }

    //-------------------------------------------------------------------------------------------------
    @Override
	public void run(final ApplicationArguments args) throws Exception {
    	System.out.println("\n Check : MaxLoop = "+MaxLoop+" / MaxRetry = "+MaxRetry+" / WaitTime = "+WaitTime+" / TrayCheckTime = "+TrayCheckTime+"\n");
    	
    	int i = 0; //robot loop counter
    	int j = 0; //tray check retry counter
    	long timeSaveLoop = System.currentTimeMillis();
    	long timeSaveRetry = System.currentTimeMillis();
    	sensorBoolSequence sensorBuffer;
    	trayBoolSequence trayBuffer;
    	boolean communicationOk = true;
    	boolean trayIsWaitingOk = true;
    	int sensorNumber = 0;
    	
    	while((i<MaxLoop)&&(j<MaxRetry)&&(communicationOk)) {
    		if (((System.currentTimeMillis()-timeSaveRetry)>=(TrayCheckTime*1000))||(trayIsWaitingOk)) {
    			if ((System.currentTimeMillis()-timeSaveLoop)>=(WaitTime*1000)) {
    				trayBuffer = getTrayServiceOrchestrationAndConsumption();
    				communicationOk = trayBuffer.getOk();
    				j++;
    				if (trayBuffer.getOk()) {
    					trayIsWaitingOk = !trayBuffer.getInProcces();
    					if(trayIsWaitingOk) {
    						sensorNumber = armProcessDecision(trayBuffer.getLoadMode());
    						sensorBuffer = getDetecServiceOrchestrationAndConsumption(DetectServiceName[sensorNumber]);
    						communicationOk = (communicationOk)&&(sensorBuffer.getOk());
    					} 
    				}	
    				if(communicationOk&&trayIsWaitingOk) {
        				System.out.println(RebotProcessMessage[sensorNumber]);
        				communicationOk =postTrayServiceOrchestrationAndConsumption(trayBuffer.getLoadMode());
        				i++;
        				j=0;
    				}
    			}
    		}
    	}
    	if(i>=MaxLoop) {
    		System.out.println("Loop ended naturally !");
    	} else if (j>=MaxRetry) {
    		System.out.println("Loop ended because the tray is somehow stuck in process !");
    	} else {
    		System.out.println("Loop ended due to communication problem !");
    	}
	}
    
    //-------------------------------------------------------------------------------------------------
    private int armProcessDecision(final boolean loadingMode) { //return true if something in the system fail, not if the arm cannot perform it's action. 
    	if(loadingMode ) { 
    		return 0; 
    	} else {
    		return 1;
    	}
    }
    
    //-------------------------------------------------------------------------------------------------
    private sensorBoolSequence getDetecServiceOrchestrationAndConsumption(final String ServiceDef)  {
    	logger.info("Orchestration request for " + ServiceDef + " service:");
    	final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(ServiceDef)
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
			return sensorBoolSequence.getFromFailure();
		} else if (orchestrationResponse.getResponse().isEmpty()) {
			logger.info("No provider found during the orchestration");
			return sensorBoolSequence.getFromFailure();
		} else {
			final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
			validateOrchestrationResult(orchestrationResult, ServiceDef);
			
			logger.info("Get slot occupation on "+"IN"+" : ");
			final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
			@SuppressWarnings("unchecked")
			final SensorDTO Det = arrowheadService.consumeServiceHTTP(SensorDTO.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(LineCommonConstants.HTTP_METHOD)),
					orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
					getInterface(), token, null, new String[0]);
		
			printOutBool(Det.getDet());
			return sensorBoolSequence.getFromSuccess(Det);
		}
    }
    
    //-------------------------------------------------------------------------------------------------
    private trayBoolSequence getTrayServiceOrchestrationAndConsumption()  {
    	String ServiceDef= LineCommonConstants.GET_TRAY_SERVICE_DEFINITION;
    	logger.info("Orchestration request for " + ServiceDef + " service:");
    	final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(ServiceDef)
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
			return trayBoolSequence.getFromFailure();
		} else if (orchestrationResponse.getResponse().isEmpty()) {
			logger.info("No provider found during the orchestration");
			return trayBoolSequence.getFromFailure();
		} else {
			final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
			validateOrchestrationResult(orchestrationResult, ServiceDef);
			
			logger.info("Get slot occupation on "+"IN"+" : ");
			final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
			@SuppressWarnings("unchecked")
			final TrayGetDTO Det = arrowheadService.consumeServiceHTTP(TrayGetDTO.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(LineCommonConstants.HTTP_METHOD)),
					orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
					getInterface(), token, null, new String[0]);
		
			printOut(Det);
			return trayBoolSequence.getFromSuccess(Det);
		}
    }
    
    //-------------------------------------------------------------------------------------------------
    private boolean postTrayServiceOrchestrationAndConsumption(boolean mode)  {
    	String ServiceDef= LineCommonConstants.POST_TRAY_SERVICE_DEFINITION;
    	logger.info("Orchestration request for " + ServiceDef + " service:");
    	final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(ServiceDef)
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
			validateOrchestrationResult(orchestrationResult, ServiceDef);
			
			final TrayPostDTO loadMoseToPost =  new TrayPostDTO(mode);
			printOut(loadMoseToPost);
	
			final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
			@SuppressWarnings("unchecked")
			
			final String response = arrowheadService.consumeServiceHTTP(String.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(LineCommonConstants.HTTP_METHOD)),
						orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
						getInterface(), token, loadMoseToPost, new String[0]);
				
			if ((response != null) && (!response.isBlank())) {
				System.out.println("Reponse : "+response);
				return true;
			} else { //somehow it failed to get any response at this point.
				return false;
			}
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
    
    private void printOutBool(boolean A) {
		if (A) {
			System.out.println("There is an object here !");
		} else {
			System.out.println("There is no object here.");
		}
    }
    
    //-------------------------------------------------------------------------------------------------
    
    
}
