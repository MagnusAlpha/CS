package org.icasa.temperature.manager;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.icasa.temperature.api.TemperatureConfiguration;
import org.icasa.temperature.api.TemperatureManagerAdministration;

@Component
@Instantiate
@Provides( specifications = TemperatureManagerAdministration.class)
public class TemperatureManagerImpl implements TemperatureManagerAdministration {

	@Requires
	private TemperatureConfiguration temperatureConfiguration;
	
	@Override
	public void temperatureIsTooHigh(String roomName) {
		System.out.println(temperatureConfiguration);
		System.out.println(temperatureConfiguration.getTargetedTemperature(roomName));
		System.out.println("get target temp good");
		temperatureConfiguration.setTargetedTemperature(roomName, temperatureConfiguration.getTargetedTemperature(roomName) - 1);
		
	}

	@Override
	public void temperatureIsTooLow(String roomName) {
		System.out.println(temperatureConfiguration);
		System.out.println(temperatureConfiguration.getTargetedTemperature(roomName));
		System.out.println("get target temp good");
		temperatureConfiguration.setTargetedTemperature(roomName, temperatureConfiguration.getTargetedTemperature(roomName) + 1);		
	}

}
