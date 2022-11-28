package lekt.thermometer_provider.controller;

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

import lekt.factoryLine_common.dto.TemperatureRequestDTO;
import lekt.factoryLine_common.dto.TemperatureResponseDTO;
import lekt.thermometer_provider.TemperatureProviderConstants;
import lekt.thermometer_provider.database.DTOConverter;
import lekt.thermometer_provider.entity.TempLog;

//import eu.arrowhead.common.exception.BadPayloadException;

@RestController
@RequestMapping(TemperatureProviderConstants.TEMP_URI)
public class TemperatureServiceController {
	
	//=================================================================================================
	// members	
	
	private double Mean = 60.0;
	private double Variation = 3.0;
	private long Period = 180;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public TemperatureResponseDTO getTemp(@RequestParam(name = TemperatureProviderConstants.REQUEST_PARAM_TIME, required = false) final Long time,
													  @RequestParam(name = TemperatureProviderConstants.REQUEST_PARAM_VALUE, required = false) final Double value) {
		
		long timeStamp = System.currentTimeMillis();
		long arg_angle = (timeStamp/1000)%Period; //position is a [period] sec cycle
        double angle = ((double)arg_angle/Period)*2*Math.PI;
        double tempValue = Mean + Variation*Math.sin(angle);

        TempLog TL = new TempLog(timeStamp,tempValue);
		TemperatureResponseDTO res_TL = DTOConverter.convertToResponseDTO(TL);
		
		return res_TL;
	}
	
	//-------------------------------------------------------------------------------------------------
	@GetMapping(path = TemperatureProviderConstants.BY_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public TemperatureResponseDTO getTempById(@PathVariable(value = TemperatureProviderConstants.PATH_VARIABLE_ID) final int id) {
		throw new UnsupportedOperationException();
	}
	
	//-------------------------------------------------------------------------------------------------
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public TemperatureResponseDTO createTemp(@RequestBody final TemperatureRequestDTO dto) {
		throw new UnsupportedOperationException();
	}
	
	//-------------------------------------------------------------------------------------------------
	@PutMapping(path = TemperatureProviderConstants.BY_ID_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public TemperatureResponseDTO updateTemp(@PathVariable(name = TemperatureProviderConstants.PATH_VARIABLE_ID) final int id, @RequestBody final TemperatureRequestDTO dto) {
		throw new UnsupportedOperationException();
	}
	
	
	//-------------------------------------------------------------------------------------------------
	@DeleteMapping(path = TemperatureProviderConstants.BY_ID_PATH)
	public void removeTempById(@PathVariable(value = TemperatureProviderConstants.PATH_VARIABLE_ID) final int id) {
		throw new UnsupportedOperationException();
	}
}
