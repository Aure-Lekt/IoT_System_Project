package lekt.factoryLineCommon.boolSequences;

import lekt.factoryLineCommon.dto.SensorResponseDTO;

public class sensorBoolSequence {

	//=================================================================================================
	// members
	private boolean readOk;
	
	private boolean occupation;

	//=================================================================================================
	// methods 
	public sensorBoolSequence() {}
		
	//-------------------------------------------------------------------------------------------------
	public sensorBoolSequence(final boolean readOk, final boolean occupation) {
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
	public static sensorBoolSequence getFromSuccess(final SensorResponseDTO dto) { // to use only for success reading !
		return new sensorBoolSequence(true,dto.getDet());
	}
	
	public static sensorBoolSequence getFromFailure() {
		return new sensorBoolSequence(false,false);
	}
	
}