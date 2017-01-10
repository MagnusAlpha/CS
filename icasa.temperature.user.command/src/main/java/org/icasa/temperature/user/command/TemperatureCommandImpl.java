package org.icasa.temperature.user.command;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.icasa.temperature.api.TemperatureManagerAdministration;

import fr.liglab.adele.icasa.command.handler.Command;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

@Component
@Instantiate
@CommandProvider( namespace="temperature" )
public class TemperatureCommandImpl {
	@Requires
	TemperatureManagerAdministration temperatureManagerAdministration;
	
	@Command
	public void tempIsTooHigh(String roomName) {
		temperatureManagerAdministration.temperatureIsTooHigh(roomName);
	}

	@Command
	public void tempIsTooLow(String roomName) {
		temperatureManagerAdministration.temperatureIsTooLow(roomName);
	}

}
