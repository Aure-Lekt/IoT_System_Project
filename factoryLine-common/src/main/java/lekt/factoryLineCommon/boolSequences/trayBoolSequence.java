package lekt.factoryLineCommon.boolSequences;

import lekt.factoryLineCommon.dto.TrayGetDTO;

public class trayBoolSequence {

	//=================================================================================================
	// members
	private boolean readOk;
	
	private boolean inProcess;
	private boolean loadMode; //true = load // false = unload
	

	//=================================================================================================
	// methods 
	public trayBoolSequence() {}
		
	//-------------------------------------------------------------------------------------------------
	public trayBoolSequence(final boolean readOk, final boolean inProcess, final boolean loadMode) {
		this.readOk = readOk;
		this.inProcess = inProcess;
		this.loadMode = loadMode;
	}

	//-------------------------------------------------------------------------------------------------
	public boolean getOk() { return readOk; }
	public boolean getInProcces() { return this.inProcess; }
	public boolean getLoadMode() { return this.loadMode; }

	//-------------------------------------------------------------------------------------------------
	public void setOk(final boolean readOk) { this.readOk = readOk; }
	public void setInProcess(final boolean inProcess) { this.inProcess = inProcess; }
	public void setLoadMode(final boolean loadMode) { this.loadMode = loadMode; }
	
	//-------------------------------------------------------------------------------------------------
	public static trayBoolSequence getFromSuccess(final TrayGetDTO dto) { // to use only for success reading !
		return new trayBoolSequence(true,dto.getProcessing(),dto.getLoadingMode());
	}
	
	public static trayBoolSequence getFromFailure() {
		return new trayBoolSequence(false,false,false);
	}
	
}