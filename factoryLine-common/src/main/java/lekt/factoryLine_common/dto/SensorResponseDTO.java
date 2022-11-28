package lekt.factoryLine_common.dto;

import java.io.Serializable;

public class SensorResponseDTO implements Serializable {
	
	//=================================================================================================
	// members
	private static final long serialVersionUID = 7277153668449540313L;
	
	private boolean detect;

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public SensorResponseDTO(final boolean detect) {
		this.detect = detect;
	}

	//-------------------------------------------------------------------------------------------------
	public boolean getDectect() { return detect; }

	//-------------------------------------------------------------------------------------------------
	public void setDetect(final boolean detect) { this.detect = detect; }
	
}
