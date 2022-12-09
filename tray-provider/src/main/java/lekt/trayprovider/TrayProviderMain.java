package lekt.trayprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import eu.arrowhead.common.CommonConstants;

import lekt.factoryLineCommon.LineCommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, LineCommonConstants.TEMPLATE_BASE_PACKAGE, LineCommonConstants.CUSTOM_BASE_PACKAGE})
public class TrayProviderMain {

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public static void main(final String[] args) {
		SpringApplication.run(TrayProviderMain.class, args);
	}	
}
