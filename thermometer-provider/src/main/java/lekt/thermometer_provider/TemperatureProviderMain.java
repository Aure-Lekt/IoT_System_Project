package lekt.thermometer_provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import eu.arrowhead.common.CommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, TemperatureProviderConstants.BASE_PACKAGE, "lekt"})
public class TemperatureProviderMain {

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public static void main(final String[] args) {
		SpringApplication.run(TemperatureProviderMain.class, args);
	}	
}
