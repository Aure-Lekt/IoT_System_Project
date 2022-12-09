package lekt.thermometerProvider.controller;

import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;
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

import lekt.factoryLineCommon.dto.TemperatureRequestDTO;
import lekt.factoryLineCommon.dto.TemperatureResponseDTO;
import lekt.thermometerProvider.database.DTOConverter;
import lekt.thermometerProvider.entity.TempLog;

import lekt.factoryLineCommon.LineCommonConstants;

//import eu.arrowhead.common.exception.BadPayloadException;

@RestController
@RequestMapping(LineCommonConstants.TEMP_URI)
@PropertySource("classpath:application.properties")
public class TemperatureServiceController {
	
	//=================================================================================================
	// members	
	
	@Value("${some.mean:50.0}")
	private double Mean;
	
	@Value("${some.var:3.0}")
	private double Variation;
	
	@Value("${some.period:60}")
	private long Period;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public TemperatureResponseDTO getTemp(@RequestParam(name = LineCommonConstants.REQUEST_PARAM_TIME, required = false) final Long time,
													  @RequestParam(name = LineCommonConstants.REQUEST_PARAM_VALUE, required = false) final Double value) {
		//completely ignore your input !
		long timeStamp = System.currentTimeMillis();
		long arg_angle = (timeStamp/1000)%Period; //position is a [period] sec cycle
        double angle = ((double)arg_angle/Period)*2*Math.PI;
        double tempValue = Mean + Variation*Math.sin(angle);

        TempLog TL = new TempLog(timeStamp,tempValue);
		TemperatureResponseDTO res_TL = DTOConverter.convertToResponseDTO(TL);
		
		return res_TL;
	}
	
	//-------------------------------------------------------------------------------------------------
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public TemperatureResponseDTO forceTemp(@RequestBody final TemperatureRequestDTO in_dto) {
		
		System.out.println("This method is useless and simply return what you send....");
		
		TemperatureResponseDTO res_dto = new TemperatureResponseDTO(in_dto.getTime(),in_dto.getValue());
		
		return res_dto;
	}
	
}
