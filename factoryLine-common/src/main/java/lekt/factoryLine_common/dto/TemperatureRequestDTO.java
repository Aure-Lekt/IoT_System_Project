package lekt.factoryLine_common.dto;

import java.io.Serializable;

public class TemperatureRequestDTO implements Serializable {

	//=================================================================================================
	// members

	private static final long serialVersionUID = -5363562707054976998L;

	private long time;
	private double value;

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public TemperatureRequestDTO(final long time, final double value) {
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
