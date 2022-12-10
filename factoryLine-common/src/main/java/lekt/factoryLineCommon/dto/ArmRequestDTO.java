package lekt.factoryLineCommon.dto;

import java.io.Serializable;

public class ArmRequestDTO implements Serializable {

	
	//=================================================================================================
	// members
	
	private static final long serialVersionUID = 6145893701466673909L;
	
	boolean loadingMode;
	
	//=================================================================================================
	// methods
	
	public ArmRequestDTO() {}

	public ArmRequestDTO(final boolean loadingMode) {
		this.loadingMode = loadingMode;
	}
	
	//--------------------------------------------------------------
	public boolean getLoadingMode() { return this.loadingMode; }
	
	//--------------------------------------------------------------
	public void setLoadingMode(final boolean loadingMode) { this.loadingMode = loadingMode; }
	
}