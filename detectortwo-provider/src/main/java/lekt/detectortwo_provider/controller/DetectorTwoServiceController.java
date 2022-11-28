package lekt.detectortwo_provider.controller;

//import java.util.ArrayList;
//import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
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

import lekt.detectortwo_provider.DetectorTwoProviderConstants;
import lekt.factoryLine_common.dto.SensorRequestDTO;
import lekt.factoryLine_common.dto.SensorResponseDTO;

//import eu.arrowhead.common.exception.BadPayloadException;

@RestController
@RequestMapping(DetectorTwoProviderConstants.DETEC_URI)
public class DetectorTwoServiceController {
	
	//=================================================================================================
	// members	
	
	private int errorMax = 1000;
	private double errorTresh = 10;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public SensorResponseDTO getDetect() {
		
		int dice = (int)(Math.random()*errorMax) + 1;
		boolean detector = (dice)>=(errorTresh);
	
		return new SensorResponseDTO(detector);
	}
	
	//-------------------------------------------------------------------------------------------------
	@GetMapping(path = DetectorTwoProviderConstants.BY_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public SensorResponseDTO getDetectById(@PathVariable(value = DetectorTwoProviderConstants.PATH_VARIABLE_ID) final int id) {
		throw new UnsupportedOperationException();
	}
	
	//-------------------------------------------------------------------------------------------------
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public SensorResponseDTO createDetect(@RequestBody final SensorRequestDTO dto) {
		throw new UnsupportedOperationException();
	}
	
	//-------------------------------------------------------------------------------------------------
	@PutMapping(path = DetectorTwoProviderConstants.BY_ID_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public SensorResponseDTO updateDetect(@PathVariable(name = DetectorTwoProviderConstants.PATH_VARIABLE_ID) final int id, @RequestBody final SensorRequestDTO dto) {
		throw new UnsupportedOperationException();
	}
	
	
	//-------------------------------------------------------------------------------------------------
	@DeleteMapping(path = DetectorTwoProviderConstants.BY_ID_PATH)
	public void removeDetectById(@PathVariable(value = DetectorTwoProviderConstants.PATH_VARIABLE_ID) final int id) {
		throw new UnsupportedOperationException();
	}
}
