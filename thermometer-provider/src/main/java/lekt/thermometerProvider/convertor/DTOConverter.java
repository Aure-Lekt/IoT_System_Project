package lekt.thermometerProvider.convertor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import lekt.factoryLineCommon.dto.TemperatureResponseDTO;
import lekt.thermometerProvider.entity.TempLog;

public class DTOConverter {

	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public static TemperatureResponseDTO convertToResponseDTO(final TempLog tl) {
		Assert.notNull(tl, "car is null");
		return new TemperatureResponseDTO(tl.getTime(), tl.getValue());
	}
	
	//-------------------------------------------------------------------------------------------------
	public static List<TemperatureResponseDTO> convertToResponseDTOList(final List<TempLog> tls) {
		Assert.notNull(tls, "car list is null");
		final List<TemperatureResponseDTO> carResponse = new ArrayList<>(tls.size());
		for (final TempLog car : tls) {
			carResponse.add(convertToResponseDTO(car));
		}
		return carResponse;
	}

	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	public DTOConverter() {
		throw new UnsupportedOperationException(); 
	}
}
