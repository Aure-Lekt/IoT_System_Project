package lekt.trayprovider.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.dto.shared.ServiceInterfaceResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.dto.shared.OrchestrationFlags.Flag;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO.Builder;
import eu.arrowhead.common.exception.BadPayloadException;
import eu.arrowhead.common.exception.InvalidParameterException;

import lekt.factoryLineCommon.LineCommonConstants;
import lekt.factoryLineCommon.dto.TrayGetDTO;
import lekt.factoryLineCommon.dto.TrayPostDTO;
import lekt.trayprovider.TrayProviderMain;

@RestController
@RequestMapping(LineCommonConstants.TRAY_URI)
@PropertySource("classpath:application.properties")
public class TrayServiceController implements ApplicationRunner {
	
	//=================================================================================================
	// members	
	
	/*@Autowired
	private ArrowheadService arrowheadService;
    
    @Autowired
	protected SSLProperties sslProperties;
    
    private final Logger logger = LogManager.getLogger(TrayProviderMain.class); */
    
	@Value("${custom.cycletime:30}") 
	private long CycleTime;
	 
	@Value("${custom.maxload:6}") 
	private int MaxLoad;
	
	private int currentlyLoaded = 0;
	
	private boolean currentlyWorking = false;
	private boolean loadingMode = true; //true = loading //false = unloading
	
	private boolean ExitCondition = false;
	
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
    @Override
	public void run(final ApplicationArguments args) throws Exception { 
    	System.out.println("\n Check : CycleTime = "+CycleTime+" / MaxLoad = "+MaxLoad+" \n");
    	

    	while(!ExitCondition) {
    		if (currentlyWorking) {
    			System.out.println("Process started !");
    			long StartTime = System.currentTimeMillis();
    			while((System.currentTimeMillis()-StartTime)<(CycleTime*1000)) {
    				//Processing (including tray movement)
    			}
    			currentlyWorking = false; //stop cycle
    			loadingMode = false; //unload mode
    			System.out.println("Process finished !");
    		}
    	}
    	
    }
    
    //-------------------------------------------------------------------------------------------------
  	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody public TrayGetDTO getTrayStates() {
  		return new TrayGetDTO(this.currentlyWorking,this.loadingMode);
  	}
	
	//-------------------------------------------------------------------------------------------------
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String loadUnloadTray(@RequestBody final TrayPostDTO dto) {
		// no matter what the result nothing here is system failure.
		if (this.currentlyWorking) {
			return ("Currenty processing, action not allowed.");
		}
		if (dto.getMode()!=this.loadingMode) {
			return ("Wrong load mode, action not allowed.");
		}
		if(dto.getMode()) {
			//load
			if (this.currentlyLoaded<MaxLoad) {
				loadTray();
				return ("Sucessfully loaded.");
			}else {
				return ("The tray is already full.");
			}
		} else {
			//unload
			if (this.currentlyLoaded>0) {
				unloadTray();
				return ("Sucessfully unloaded.");
			}else {
				return ("The tray is already empty.");
			}
		}
	}
	
	//=================================================================================================
	// custom methods
	
	//-------------------------------------------------------------------------------------------------
	private void loadTray() {
		this.currentlyLoaded++;
		if (this.currentlyLoaded==MaxLoad) { 
			this.currentlyWorking = true; 
		}
	}
	   
	//-------------------------------------------------------------------------------------------------
	private void unloadTray() {
		this.currentlyLoaded--;
		if (this.currentlyLoaded==0) { 
			this.loadingMode = true; 
		}
	}
}
