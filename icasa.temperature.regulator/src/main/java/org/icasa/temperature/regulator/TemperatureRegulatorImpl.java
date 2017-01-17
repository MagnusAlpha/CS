package org.icasa.temperature.regulator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;
import org.icasa.temperature.api.TemperatureConfiguration;

import fr.liglab.adele.icasa.ContextManager;
import fr.liglab.adele.icasa.device.temperature.Cooler;
import fr.liglab.adele.icasa.device.temperature.Heater;
import fr.liglab.adele.icasa.device.temperature.Thermometer;
import fr.liglab.adele.icasa.location.Zone;
import fr.liglab.adele.icasa.service.scheduler.PeriodicRunnable;

@Component
@Instantiate
@Provides( specifications = {TemperatureConfiguration.class,PeriodicRunnable.class})
public class TemperatureRegulatorImpl implements TemperatureConfiguration, PeriodicRunnable
{


	/** Field for heaters dependency */
	@Requires( id="heater", optional=true )
	private Heater[] heaters;
	/** Field for thermometers dependency */
	@Requires( id="thermometer", optional=true )
	private Thermometer[] thermometers;
	/** Field for coolers dependency */
	@Requires( id="cooler", optional=true )
	private Cooler[] coolers;
	
	private Map<Zone, Float> mapZoneTemperature;

	/**
	 * The name of the LOCATION property
	 */
	public static final String LOCATION_PROPERTY_NAME = "Location";

	/**
	 * The name of the location for unknown value
	 */
	public static final String LOCATION_UNKNOWN = "unknown";

	// private double targetTemperature = 20;
	/** Field for contextManager dependency */
	@Requires
	private ContextManager contextManager;

	/** Bind Method for thermometers dependency */
	@Bind(id="thermometer" )
	public void bindThermometer(Thermometer thermometer, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Unbind Method for thermometers dependency */
	@Unbind(id="thermometer" )
	public void unbindThermometer(Thermometer thermometer, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Bind Method for coolers dependency */
	@Bind( id="cooler" )
	public void bindCooler(Cooler cooler, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Unbind Method for coolers dependency */
	@Unbind( id="cooler" )
	public void unbindCooler(Cooler cooler, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Bind Method for heaters dependency */
	@Bind(id="heater")
	public void bindHeater(Heater heater, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Unbind Method for heaters dependency */
	@Unbind(id="heater")
	public void unbindHeater(Heater heater, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Component Lifecycle Method */
	@Invalidate
	public void stop() {
		// TODO: Add your implementation code here
	}

	/** Component Lifecycle Method */
	@Validate
	public void start() {
		mapZoneTemperature = new LinkedHashMap<Zone, Float>();
	}

	@Override
	public void run() {
		double dTemperatureMoyen = 0;
		

		for(Zone zone : contextManager.getZones()){
			dTemperatureMoyen = 0;
			List<Heater> zoneHeaters = getHeaterFromLocalisation(zone.getId() );
			List<Thermometer> zoneThermometers = getThermometerFromLocalisation(zone.getId());
			List<Cooler> zoneCoolers = getCoolerFromLocalisation(zone.getId());
			Float targetTemperature = mapZoneTemperature.get(zone);
			if( targetTemperature == null ){
				targetTemperature = new Float(19.0);
			}
			
			if( !zoneThermometers.isEmpty() ){
				for( Thermometer thermo : zoneThermometers){
					dTemperatureMoyen += thermo.getTemperature();
				}
				dTemperatureMoyen = dTemperatureMoyen / zoneThermometers.size();
				if( dTemperatureMoyen + 3 < targetTemperature+273 ){
					for( Heater heater : zoneHeaters ){
						heater.setPowerLevel(.5);
					}
					for( Cooler cooler : zoneCoolers ){
						cooler.setPowerLevel(0);
					}
				}else if( dTemperatureMoyen - 3 > targetTemperature+273){
					for( Heater heater : zoneHeaters ){
						heater.setPowerLevel(0);
					}
					for( Cooler cooler : zoneCoolers ){
						cooler.setPowerLevel(.5);
					}
				}else{
					for( Heater heater : zoneHeaters ){
						heater.setPowerLevel(0);
					}
					for( Cooler cooler : zoneCoolers ){
						cooler.setPowerLevel(0);
					}
				}
			}
			
		}
		

	}

	@Override
	public long getPeriod() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public TimeUnit getUnit() {
		// TODO Auto-generated method stub
		return TimeUnit.MINUTES;
	}

	private synchronized List<Heater> getHeaterFromLocalisation(String location) {
		List<Heater> heatersLocation = new ArrayList<Heater>();
		for (Heater heater : heaters) {
			if (heater.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				heatersLocation.add(heater);
			}
		}
		return heatersLocation;
	}

	private synchronized List<Cooler> getCoolerFromLocalisation(String location) {
		List<Cooler> coolersLocation = new ArrayList<Cooler>();
		for (Cooler cooler : coolers) {
			if (cooler.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				coolersLocation.add(cooler);
			}
		}
		return coolersLocation;
	}

	private synchronized List<Thermometer> getThermometerFromLocalisation(String location) {
		List<Thermometer> thermometerLocation = new ArrayList<Thermometer>();
		for (Thermometer thermometer : thermometers) {
			if (thermometer.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				thermometerLocation.add(thermometer);
			}
		}
		return thermometerLocation;
	}

	@Override
	public void setTargetedTemperature(String targetedRoom, float temperature) {
		// TODO Auto-generated method stub
		System.out.println("temperature requested is : " + temperature);
		System.out.println("targetroom requested is : " + targetedRoom);
		System.out.println("Zone : "+ contextManager.getZone(targetedRoom));
		mapZoneTemperature.put( contextManager.getZone(targetedRoom) , temperature);
		
	}

	@Override
	public float getTargetedTemperature(String room) {
		System.out.println("Room get target temp : "+ room);
		System.out.println("Zone get target temp : "+ contextManager.getZone(room) );
		Zone zroom = contextManager.getZone(room);
		return mapZoneTemperature.get(zroom);
	}
}
