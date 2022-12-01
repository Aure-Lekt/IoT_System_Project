package lekt.factoryLineCommon.dto;

import java.io.Serializable;

public final class SensorDTO extends Object implements Serializable {

	private static final long serialVersionUID = 7059675755996020851L;
	
	private boolean detect;

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public SensorDTO() {}
	
	//-------------------------------------------------------------------------------------------------
	public SensorDTO(final boolean detect) {
		this.detect = detect;
	}

	//-------------------------------------------------------------------------------------------------
	public boolean getDet() { return this.detect; }
	
	//-------------------------------------------------------------------------------------------------
	public void setDet(final boolean detect) { this.detect = detect; } 
	
}
