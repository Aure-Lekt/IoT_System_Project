package lekt.traconsumer;

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
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.dto.shared.ServiceInterfaceResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.dto.shared.OrchestrationFlags.Flag;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO.Builder;
import eu.arrowhead.common.exception.InvalidParameterException;

import lekt.factoryLineCommon.LineCommonConstants;
import lekt.factoryLineCommon.dto.ArmResponseDTO;
import lekt.factoryLineCommon.dto.ArmRequestDTO;
import lekt.factoryLineCommon.boolSequences.armBoolSequence;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, LineCommonConstants.TEMPLATE_BASE_PACKAGE, LineCommonConstants.CUSTOM_BASE_PACKAGE})
@PropertySource("classpath:application.properties")
public class TrayConsumerMain implements ApplicationRunner {

    //=================================================================================================
	// members
	
    @Autowired
	private ArrowheadService arrowheadService;
    
    @Autowired
	protected SSLProperties sslProperties;
    
    private final Logger logger = LogManager.getLogger(TrayConsumerMain.class);
    
    // custom variables
    @Value("${custom.maxload:10}") 
    private int MaxLoad;
    
    @Value("${custom.waittime:5}") 
    private long WaitTime; //in seconds
    
    @Value("${custom.cycletime:20}") 
    private long CycleTime; //in seconds
    
    @Value("${custom.maxretry:10}") 
    private long MaxRetry;

    @Value("${custom.maxloop:100}") 
    private long MaxLoop;
    
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public static void main(final String[] args) {
		SpringApplication.run(TrayConsumerMain.class, args);
	}

	//-------------------------------------------------------------------------------------------------
    @Override
	public void run(final ApplicationArguments args) throws Exception {
    	System.out.println("\n Check : MaxLoad = "+MaxLoad);
    	System.out.println("MaxLoop = "+MaxLoop+" / MaxRetry = "+MaxRetry);
    	System.out.println("WaitTime = "+WaitTime+" / CycleTime = "+CycleTime+" \n");
    	
    	int i = 0; //loop count
    	int j = 0; //retry count
    	boolean forceExit = false;
    	long timeSave = System.currentTimeMillis();
    	armBoolSequence responseBuffer = new armBoolSequence(true,true,true);
    	boolean loadMode = true;
    	int currentLoad = 0;
    	
    	while((i<MaxLoop)&&(j<MaxRetry)&&(responseBuffer.getArmReadOk())&&(responseBuffer.getSensorReadOk())&&(!forceExit)) {
    		if ((System.currentTimeMillis()-timeSave)>=(WaitTime*1000)) {
    			if (loadMode) {
    				if(currentLoad<MaxLoad) {
    					System.out.println("Loading the Tray :");
    					responseBuffer = postArmServiceOrchestrationAndConsumption(loadMode);
    					if ((responseBuffer.getArmReadOk())&&(responseBuffer.getSensorReadOk())) {
    						if(responseBuffer.getLoadUnloadSuccess()) {
    							System.out.println("Loaded with success.");
    							currentLoad++;
        						j=0;
        						i++;
    						} else {
    							System.out.println("Failed to load.");
        						j++;
    						}
    						System.out.println("Current load : "+currentLoad);
    						timeSave = System.currentTimeMillis();
    					} else {
    						System.out.println("Communication failure.");
    					}	
    				} else if (currentLoad==MaxLoad){
    					System.out.println("The tray is full.");
    					chemicalProcess();
    					timeSave = System.currentTimeMillis();
    					loadMode = false;
    				} else { // > MaxLoad  should not happen
    					System.out.println("Something got wrong");
    					forceExit = true;
    				}
    			} else {
    				if(currentLoad>0) {	
    					System.out.println("Unloading the Tray :");
    					responseBuffer = postArmServiceOrchestrationAndConsumption(loadMode);
    					if ((responseBuffer.getArmReadOk())&&(responseBuffer.getSensorReadOk())) {
    						if(responseBuffer.getLoadUnloadSuccess()) {
    							System.out.println("Unloaded with success.");
    							currentLoad--;
        						j=0;
        						i++;
    						} else {
    							System.out.println("Failed to unload.");
        						j++;
    						}
    						System.out.println("Current load : "+currentLoad);
    						timeSave = System.currentTimeMillis();
    					} else {
    						System.out.println("Communication failure.");
    					}			
    				} else if (currentLoad==0){
    					System.out.println("The tray is empty.");
    					loadMode = true;
    					timeSave = System.currentTimeMillis();
    				} else { // <0  should not happen
    					System.out.println("Something got wrong");
    					forceExit = true;
    				}	
    			}
    		}
    	}
    	
    	if(i>=MaxLoad) {
    		System.out.println("Loop exited naturally.");
    	} else if (j>=MaxRetry) {
    		System.out.println("Loop exited because of communication failure or load/unload failure.");
    	} else {
    		System.out.println("Loop exited because of an unknown error ");
    	}
    	
    	System.exit(0);
	}
    
    //-------------------------------------------------------------------------------------------------
    public void chemicalProcess() {
    	System.out.println("Starting process.");
    	long timer = System.currentTimeMillis();
    	int i = 0;
    	while ((System.currentTimeMillis()-timer)<(CycleTime*1000)) {
    		if ((System.currentTimeMillis()-timer)>=(CycleTime*100*i)) { //every 10%
    			System.out.println(i*10+"%.");
    			i++;
    		}
    	}
    	System.out.println("Process done !"); 	
    }
    
    
    //-------------------------------------------------------------------------------------------------
    public armBoolSequence postArmServiceOrchestrationAndConsumption(boolean loadMode) {
    	logger.info("Orchestration request for " + LineCommonConstants.POST_ARM_SERVICE_DEFINITION + " service:");
    	final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(LineCommonConstants.POST_ARM_SERVICE_DEFINITION)
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
			return armBoolSequence.getFromFailure();
		} else if (orchestrationResponse.getResponse().isEmpty()) {
			logger.info("No provider found during the orchestration");
			return armBoolSequence.getFromFailure();
		} else {
			final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
			validateOrchestrationResult(orchestrationResult, LineCommonConstants.POST_ARM_SERVICE_DEFINITION);
	
			
			final ArmRequestDTO armRequest = new ArmRequestDTO(loadMode);
			printOut(armRequest);
			
			logger.info("Sending redquest :");
			final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
			
			final ArmResponseDTO armResponse = arrowheadService.consumeServiceHTTP(ArmResponseDTO.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(LineCommonConstants.HTTP_METHOD)),
					orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
					getInterface(), token, armRequest, new String[0]);
			logger.info("Provider (Arm) response : ");
			printOut(armResponse);
			
			return armBoolSequence.getFromSuccess(armResponse);
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
	
}
