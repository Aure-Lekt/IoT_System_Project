package lekt.factoryLineCommon.boolSequences;

import lekt.factoryLineCommon.dto.SensorResponseDTO;

public class SensorBoolSequence {

	//=================================================================================================
	// members
	private boolean readOk;
	private boolean occupation;

	//=================================================================================================
	// methods 
	public SensorBoolSequence() {}
		
	//-------------------------------------------------------------------------------------------------
	public SensorBoolSequence(final boolean readOk, final boolean occupation) {
		this.readOk = readOk;
		this.occupation = occupation;
	}

	//-------------------------------------------------------------------------------------------------
	public boolean getOk() { return this.readOk; }
	public boolean getOccupation() { return this.occupation; }

	//-------------------------------------------------------------------------------------------------
	public void setOk(final boolean readOk) { this.readOk = readOk; }
	public void setOccupation(final boolean occupation) { this.occupation = occupation; }	
	
	//-------------------------------------------------------------------------------------------------
	public static SensorBoolSequence getFromSuccess(final SensorResponseDTO dto) { // to use only for successful reading !
		return new SensorBoolSequence(true,dto.getDetection());
	}
	
	public static SensorBoolSequence getFromFailure() {
		return new SensorBoolSequence(false,false);
	}
	
}