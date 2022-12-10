package lekt.detectoroneprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import eu.arrowhead.common.CommonConstants;

import lekt.factoryLineCommon.LineCommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, LineCommonConstants.TEMPLATE_BASE_PACKAGE,LineCommonConstants.CUSTOM_BASE_PACKAGE})
public class DetectorOneProviderMain {

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public static void main(final String[] args) {
		SpringApplication.run(DetectorOneProviderMain.class, args);
	}	
}
