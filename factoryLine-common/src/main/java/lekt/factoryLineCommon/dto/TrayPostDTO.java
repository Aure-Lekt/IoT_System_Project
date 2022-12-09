package lekt.factoryLineCommon.dto;

import java.io.Serializable;

public final class TrayPostDTO extends Object implements Serializable {
	
	private static final long serialVersionUID = -7337426196484239573L;
	
	private boolean loadMode;

	//=================================================================================================
	// methods

	public TrayPostDTO() {}
	
	public TrayPostDTO(final boolean loadMode) {
		this.loadMode = loadMode; 
	}
	
	public boolean getMode() { return this.loadMode; }
	
	public void setMode(final boolean loadMode) { this.loadMode = loadMode; }
	
}
