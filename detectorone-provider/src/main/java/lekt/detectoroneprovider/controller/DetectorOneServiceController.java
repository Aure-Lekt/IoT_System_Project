package lekt.detectoroneprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

import eu.arrowhead.common.exception.BadPayloadException;

import lekt.factoryLineCommon.LineCommonConstants;
import lekt.factoryLineCommon.dto.SensorResponseDTO;



@RestController
@RequestMapping(LineCommonConstants.DETONE_URI)
@PropertySource("classpath:application.properties")
public class DetectorOneServiceController {
	
	// Parameters
	@Value("${custom.range:100}") 
	private int RANGE;
	
	@Value("${custom.treshold:10}") 
	private int TRESH;

	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public SensorResponseDTO getDetOne() {
		
		int dice = (int)(Math.random()*RANGE); 
		boolean resp = (dice>=TRESH);
		
		SensorResponseDTO SR = new SensorResponseDTO(resp);
		return SR;
	}

}
