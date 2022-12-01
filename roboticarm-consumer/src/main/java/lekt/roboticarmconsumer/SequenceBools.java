package lekt.roboticarmconsumer;

public class SequenceBools {
	
	//=================================================================================================
	// members

	private boolean readOk;
	private boolean occupation;

	//=================================================================================================
	// methods 

	//-------------------------------------------------------------------------------------------------
	public SequenceBools() {}
		
	//-------------------------------------------------------------------------------------------------
	public SequenceBools(final boolean readOk, final boolean occupation) {
		this.readOk = readOk;
		this.occupation = occupation;
	}

	//-------------------------------------------------------------------------------------------------
	public boolean getOk() { return readOk; }
	public boolean getOccupation() { return occupation; }

	//-------------------------------------------------------------------------------------------------
	public void seOk(final boolean readOk) { this.readOk = readOk; }
	public void setOccupation(final boolean occupation) { this.occupation = occupation; }		
}

