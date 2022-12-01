package lekt.thermometerProvider.entity;

public class TempLog {

	//=================================================================================================
	// members

	private long time;
	private double value;

	//=================================================================================================
	// methods 

	//-------------------------------------------------------------------------------------------------
	public TempLog(final long time, final double value) {
		this.time = time;
		this.value = value;
	}

	//-------------------------------------------------------------------------------------------------
	public long getTime() { return time; }
	public double getValue() { return value; }

	//-------------------------------------------------------------------------------------------------
	public void setTime(final long time) { this.time = time; }
	public void setValue(final double value) { this.value = value; }		
}
