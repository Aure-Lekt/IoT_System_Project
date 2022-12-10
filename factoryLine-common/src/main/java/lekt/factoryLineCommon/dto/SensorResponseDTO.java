package lekt.factoryLineCommon.dto;

import java.io.Serializable;

public final class SensorResponseDTO extends Object implements Serializable {

	private static final long serialVersionUID = -2338081986525711986L;
	
	private boolean detect;

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public SensorResponseDTO() {}
	
	//-------------------------------------------------------------------------------------------------
	public SensorResponseDTO(final boolean detect) {
		this.detect = detect;
	}

	//-------------------------------------------------------------------------------------------------
	public boolean getDet() { return this.detect; }
	
	//-------------------------------------------------------------------------------------------------
	public void setDet(final boolean detect) { this.detect = detect; } 
	
}
