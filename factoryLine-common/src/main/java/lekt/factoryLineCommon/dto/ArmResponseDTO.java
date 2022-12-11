package lekt.factoryLineCommon.dto;

import java.io.Serializable;

public class ArmResponseDTO implements Serializable {
	
	//=================================================================================================
	// members
	
	private static final long serialVersionUID = 7525673135874508785L;
	
	boolean sensorReadStatus;
	boolean loadingProcessStatus;
	
	//=================================================================================================
	// methods 
	
	public ArmResponseDTO() {}

	public ArmResponseDTO(final boolean sensorReadStatus, final boolean loadingProcessStatus) {
		this.sensorReadStatus = sensorReadStatus;
		this.loadingProcessStatus = loadingProcessStatus;
	}
	
	//--------------------------------------------------------------
	public boolean getSensorReadStatus() { return this.sensorReadStatus; }
	public boolean getLoadingProcessStatus() { return this.loadingProcessStatus; }
	
	//--------------------------------------------------------------
	public void setSensorReadStatus(final boolean sensorReadStatus) { this.sensorReadStatus = sensorReadStatus; }
	public void setLoadingProcessStatus(final boolean loadingProcessStatus) { this.loadingProcessStatus = loadingProcessStatus; }
	
}
