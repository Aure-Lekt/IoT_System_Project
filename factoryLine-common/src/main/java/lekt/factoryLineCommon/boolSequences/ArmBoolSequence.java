package lekt.factoryLineCommon.boolSequences;

import lekt.factoryLineCommon.dto.ArmResponseDTO;

public class ArmBoolSequence {
	
		//=================================================================================================
		// members
		private boolean armReadOk;
		private boolean sensorReadOk;
		private boolean loadUnloadSuccess;
		

		//=================================================================================================
		// methods 
		public ArmBoolSequence() {}
		
		//-------------------------------------------------------------------------------------------------
		public ArmBoolSequence(final boolean armReadOk, final boolean sensorReadOk, final boolean loadUnloadSuccess) {
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
		public static ArmBoolSequence getFromFailure() {
			return new ArmBoolSequence(false,false,false);
		}
		
		public static ArmBoolSequence getFromSuccess(final ArmResponseDTO dto) {
			return new ArmBoolSequence(true,dto.getSensorReadStatus(),dto.getLoadingProcessStatus());
		}
		
}
