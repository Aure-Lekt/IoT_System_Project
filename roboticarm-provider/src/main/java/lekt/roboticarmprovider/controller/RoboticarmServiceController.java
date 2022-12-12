package lekt.roboticarmprovider.controller;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
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

import eu.arrowhead.common.Utilities;
import lekt.factoryLineCommon.dto.ArmResponseDTO;
import lekt.roboticarmprovider.consumerpart.RoboticArmConsumer;
import lekt.factoryLineCommon.dto.ArmRequestDTO;
import lekt.factoryLineCommon.boolSequences.sensorBoolSequence;
import lekt.factoryLineCommon.LineCommonConstants;

@RestController
@RequestMapping(LineCommonConstants.ARM_URI)
public class RoboticarmServiceController {
	
	//=================================================================================================
	// members	
	
	@Autowired
	private RoboticArmConsumer roboticArmConsumer;
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public ArmResponseDTO loadUnloadPiece(@RequestBody final ArmRequestDTO in_dto) {
	
		boolean loadMode = in_dto.getLoadingMode(); //true = load , false = unload
		ArmResponseDTO responseDto;
		
		if(loadMode) {
			System.out.println("Load request :");
			sensorBoolSequence sensorResponse = roboticArmConsumer.getInSensor();
			if (sensorResponse.getOk()) {
				if (sensorResponse.getOccupation()) {
					System.out.println("Piece found on the input slot.");
					System.out.println("Loading the piece into the tray.");
					responseDto =  new ArmResponseDTO(true,true);
				} else {
					System.out.println("No Piece found on the input slot.");
					System.out.println("Can't load.");	
					responseDto = new ArmResponseDTO(true,false);
				}
			} else {
				System.out.println("Fail to read sensor");
				responseDto = new ArmResponseDTO(false,false);
			}
		} else {
			System.out.println("Unload request :");
			sensorBoolSequence sensorResponse = roboticArmConsumer.getOutSensor();	
			if (sensorResponse.getOk()) {
				if (!sensorResponse.getOccupation()) {
					System.out.println("No Piece found on the output slot.");
					System.out.println("UnLoading the piece into the tray.");
					responseDto = new ArmResponseDTO(true,true);
				} else {
					System.out.println("Piece found on the output slot.");
					System.out.println("Can't unload.");	
					responseDto = new ArmResponseDTO(true,false);
				}
			} else {
				System.out.println("Fail to read sensor");
				responseDto = new ArmResponseDTO(false,false);
			}
		}
		
		System.out.println("\n Response to send : ");
		printOut(responseDto);
		
		return responseDto;
	}
	
	private void printOut(final Object object) {
    	System.out.println(Utilities.toPrettyJson(Utilities.toJson(object)));
    }
	
}
