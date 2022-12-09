package lekt.factoryLineCommon.dto;

import java.io.Serializable;

public final class TrayGetDTO extends Object implements Serializable {
	
	private static final long serialVersionUID = 5850563720465349800L;
	
	private boolean isProcessing;
	private boolean loadingMode;

	//=================================================================================================
	// methods

	public TrayGetDTO() {}
	
	public TrayGetDTO(final boolean isProcessing, final boolean loadingMode) {
		this.isProcessing = isProcessing; 
		this.loadingMode = loadingMode;
	}
	
	public boolean getProcessing() { return this.isProcessing; }
	public boolean getLoadingMode() { return this.loadingMode; }
	
	public void setProcessing(final boolean isProcessing) { this.isProcessing = isProcessing; }
	public void setLoadingMode(final boolean loadingMode) { this.loadingMode = loadingMode; }
	
	
}
