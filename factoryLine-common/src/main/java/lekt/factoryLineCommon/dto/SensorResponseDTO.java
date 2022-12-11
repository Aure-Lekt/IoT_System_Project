package lekt.factoryLineCommon.dto;

import java.io.Serializable;

public final class SensorResponseDTO extends Object implements Serializable {

	private static final long serialVersionUID = 7491010873642425359L;
	
	private boolean detection;

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public SensorResponseDTO() {} 
	
	//-------------------------------------------------------------------------------------------------
	public SensorResponseDTO(final boolean detection) {
		this.detection = detection;
	}

	//-------------------------------------------------------------------------------------------------
	public boolean getDetection() { return this.detection; }
	
	//-------------------------------------------------------------------------------------------------
	public void setDetection(final boolean detection) { this.detection = detection; } 
	
}
