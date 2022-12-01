package lekt.detectortwoprovider.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
import lekt.factoryLineCommon.dto.SensorDTO;

@RestController
@RequestMapping(LineCommonConstants.DETTWO_URI)
public class DetectorTwoServiceController {
	
		//=================================================================================================
		// methods
		private final int RANGE = 100;
		private final int TRESH = 20;
		
		//-------------------------------------------------------------------------------------------------
		@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody public SensorDTO getDetTwo() {
			
			int dice = (int)(Math.random()*RANGE); 
			boolean resp = (dice>=TRESH);
			
			SensorDTO SR = new SensorDTO(resp);
			return SR;
		}
		
}
