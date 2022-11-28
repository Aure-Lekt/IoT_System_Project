package lekt.factoryLine_common.dto;

import java.io.Serializable;

public class SensorRequestDTO implements Serializable{
	
	//=================================================================================================
	// members

	private static final long serialVersionUID = -6496147797142808225L;
	
	private boolean detect;

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public SensorRequestDTO(final boolean detect) {
		this.detect = detect;
	}

	//-------------------------------------------------------------------------------------------------
	public boolean getDectect() { return detect; }

	//-------------------------------------------------------------------------------------------------
	public void setDetect(final boolean detect) { this.detect = detect; }

}

