package lekt.factoryLine_common.dto;

import java.io.Serializable;

public class TemperatureResponseDTO implements Serializable {

	//=================================================================================================
	// members

	private static final long serialVersionUID = -8371510478751740542L;
	
	private long time;
	private double value;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public TemperatureResponseDTO() {}
	
	//-------------------------------------------------------------------------------------------------
	public TemperatureResponseDTO(final long time, final double value) {
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
