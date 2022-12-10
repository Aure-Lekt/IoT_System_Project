package lekt.factoryLineCommon.boolSequences;

import lekt.factoryLineCommon.dto.ArmResponseDTO;

public class armBoolSequence {
	
		//=================================================================================================
		// members
		private boolean armReadOk;
		private boolean sensorReadOk;
		private boolean loadUnloadSuccess; //true = load // false = unload
		

		//=================================================================================================
		// methods 
		public armBoolSequence() {}
		
		//-------------------------------------------------------------------------------------------------
		public armBoolSequence(final boolean armReadOk, final boolean sensorReadOk, final boolean loadUnloadSuccess) {
			this.armReadOk = armReadOk;
			this.sensorReadOk = sensorReadOk;
			this.loadUnloadSuccess = loadUnloadSuccess;
		}

		//-------------------------------------------------------------------------------------------------
		public boolean getArmReadOk() { return armReadOk; }
		public boolean getSensorReadOk() { return this.sensorReadOk; }
		public boolean getLoadUnloadSuccess() { return this.loadUnloadSuccess; }

		//-------------------------------------------------------------------------------------------------
		public void setArmReadOk(final boolean armReadOk) { this.armReadOk = armReadOk; }
		public void setsSensorReadOk(final boolean sensorReadOk) { this.sensorReadOk = sensorReadOk; }
		public void setLoadUnloadSuccess(final boolean loadUnloadSuccess) { this.loadUnloadSuccess= loadUnloadSuccess; }
		
		//-------------------------------------------------------------------------------------------------
		public static armBoolSequence getFromFailure() {
			return new armBoolSequence(false,false,false);
		}
		
		public static armBoolSequence getFromSuccess(final ArmResponseDTO dto) {
			return new armBoolSequence(true,dto.getSensorReadStatus(),dto.getLoadingProcessStatus());
		}
		
}
