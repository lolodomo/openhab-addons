# Matter Binding

The Matter Binding for openHAB allows seamless integration with Matter-compatible devices. Matter is a unifying standard for smart home devices, enabling reliable, secure connectivity across various platforms. This binding facilitates the control and management of Matter devices within the openHAB environment.

By leveraging the Matter Binding, users can connect and control their Matter devices.

## Supported Things

The Matter Binding supports the following types of things:

- `controller`: The main controller that interfaces with Matter devices. It requires configuration parameters such as `port`, `host`, `portId`, and `pairCode`.
- `endpoint`: Represents an individual endpoint within the Matter network. Configuration parameters include `nodeId` and `endpointId`.

## Discovery

Matter controllers must be added manually.  Endpoint (devices) will be discovered when a `pairCode` is used to search for a device to add. 

## Binding Configuration

This binding does not require any general configuration settings.

## Thing Configuration

### Controller Thing Configuration

| Name                      | Type    | Description                                                                                                                       | Default | Required | Advanced |
|---------------------------|---------|-----------------------------------------------------------------------------------------------------------------------------------|---------|----------|----------|
| nodeId                    | number  | The matter node ID for this controller                                                                                            | 0       | yes      | no       |
| decommissionNodesOnDelete | boolean | Decommission (unpair) Matter nodes from the Matter network when the last endpoint thing of a node is deleted/removed from openHAB | true    | yes      | no       |
| pairCode                  | text    | The 12 digit pairing code or short code and key to add a new IP matter device                                                     | N/A     | no       | no       |
| port                      | number  | The port number for the controller                                                                                                | N/A     | no       | yes      |
| host                      | text    | The host address of the controller                                                                                                | N/A     | no       | yes      |

Note: pairCode configuration field is only available in 4.3 builds and only usable through the Main UI or API 

### Endpoint Thing Configuration

| Name       | Type   | Description                        | Default | Required | Advanced |
|------------|--------|------------------------------------|---------|----------|----------|
| nodeId     | text   | The node ID of the endpoint        | N/A     | yes      | no       |
| endpointId | number | The endpoint ID within the node    | N/A     | yes      | no       |

## Channels

### Controller Channels
The Matter Binding provides various channels for controlling and monitoring Matter devices. Here are some of the available channels:

| Channel  | Type   | Read/Write | Description                   |
|----------|--------|------------|-------------------------------|
| pairCode  | String | RW         | 12 digit pairing code or short code and key (separated by a space)       |

### Endpoint Channels
Endpoint channels are dynamically added based on the endpoint type.
Possible channels include:

## Endpoint Channels

| Channel ID                   | Item Type                | Label                       | Description                                          | Category       | ReadOnly | Pattern       |
|------------------------------|--------------------------|-----------------------------|------------------------------------------------------|----------------|----------|----------------|
| battery_voltage              | Number:ElectricPotential | Battery Voltage             | The current battery voltage                          | Energy         | yes      | %.1f %unit%    |
| battery_alarm                | String                   | Battery Alarm               | The battery alarm state                              | Energy         | yes      |                |
| color_color                  | Color                    | Color                       | Control the color of a light                         | ColorLight     | no       |                |
| door_state                   | Switch                   | Door Lock State             | Locks and unlocks the door                           | Door           | no       |                |
| fancontrol                   | Number                   | Fan Mode                    | Set the fan mode                                     | HVAC           | no       |                |
| electrical_activepower       | Number:Power             | Total Active Power          | The total power consumed by the device               | Energy         | yes      | %.1f %unit%    |
| electrical_rmscurrent        | Number:ElectricCurrent   | Current                     | The current RMS current measurement                  | Energy         | yes      | %.1f %unit%    |
| electrical_rmsvoltage        | Number:ElectricPotential | Voltage                     | The current RMS voltage measurement                  | Energy         | yes      | %.1f %unit%    |
| binaryinput                  | Switch                   | Binary Input Sensor         | Indicates a binary input sensor state                |                | yes      |                |
| ias_contactportal1           | Switch                   | Contact Sensor              | Contact sensor                                       | Door           | yes      |                |
| ias_motionintrusion          | Switch                   | Motion Intrusion            | Motion intrusion sensor                              | Motion         | yes      |                |
| ias_codetector               | Switch                   | Carbon Monoxide Detector    | Carbon Monoxide Alarm                                | Sensor         | yes      |                |
| ias_fire                     | Switch                   | Fire Detector               | Fire Indication Alarm                                | SmokeDetector  | yes      |                |
| ias_motionpresence           | Switch                   | Motion Presence             | Motion presence sensor                               | Motion         | yes      |                |
| ias_standard_system          | Switch                   | System Alarm                |                                                      |                | yes      |                |
| ias_water                    | Switch                   | Water Sensor                | Water Sensor Alarm                                   | Sensor         | yes      |                |
| ias_movement                 | Switch                   | Movement Sensor             | Movement Sensor Alarm                                | Sensor         | yes      |                |
| ias_vibration                | Switch                   | Vibration Sensor            | Vibration Sensor Alarm                               | Sensor         | yes      |                |
| ias_tamper                   | Switch                   | Tamper                      | Indicates if a device is tampered with               | Alarm          | yes      |                |
| measurement_illuminance      | Number                   | Illuminance                 | Indicates the current illuminance in lux             |                | yes      | %.0f           |
| measurement_pressure         | Number:Pressure          | Atmospheric Pressure        | Indicates the current pressure                       | Pressure       | yes      | %.1f %unit%    |
| measurement_relativehumidity | Number                   | Humidity                    | Indicates the current relative humidity              | Humidity       | yes      | %.1f           |
| measurement_temperature      | Number:Temperature       | Temperature                 | Indicates the current temperature                    | Temperature    | yes      | %.1f %unit%    |
| metering_instantdemand       | Number                   | Instantaneous Demand        | The instantaneous demand from the metering system    | Number         | no       |                |
| metering_sumdelivered        | Number                   | Summation Delivered         | The total delivered from the metering system         | Number         | no       |                |
| metering_sumreceived         | Number                   | Summation Received          | The total received from the metering system          | Number         | no       |                |
| sensor_occupancy             | Switch                   | Occupancy                   | Indicates if an occupancy sensor is triggered        | Motion         | yes      |                |
| switch_onoff                 | Switch                   | Switch                      | Switches the power on and off                        | Light          | no       |                |
| switch_level                 | Dimmer                   | Dimmer                      | Sets the level of the light                          | Light          | no       |                |
| thermostat_localtemp         | Number:Temperature       | Local Temperature           | Indicates the local temperature provided by the thermostat | HVAC       | yes      | %.1f %unit%    |
| thermostat_outdoortemp       | Number:Temperature       | Outdoor Temperature         | Indicates the outdoor temperature provided by the thermostat | HVAC   | yes      | %.1f %unit%    |
| thermostat_occupiedheating   | Number:Temperature       | Occupied Heating Setpoint   | Set the heating temperature when the room is occupied | HVAC          | no       | %.1f %unit%    |
| thermostat_occupiedcooling   | Number:Temperature       | Occupied Cooling Setpoint   | Set the cooling temperature when the room is occupied | HVAC          | no       | %.1f %unit%    |
| thermostat_unoccupiedheating | Number:Temperature       | Unoccupied Heating Setpoint | Set the heating temperature when the room is unoccupied | HVAC       | no       | %.1f %unit%    |
| thermostat_unoccupiedcooling | Number:Temperature       | Unoccupied Cooling Setpoint | Set the cooling temperature when the room is unoccupied | HVAC       | no       | %.1f %unit%    |
| thermostat_systemmode        | Number                   | System Mode                 | Set the system mode of the thermostat                | HVAC           | no       |                |
| thermostat_runningmode       | Number                   | Running Mode                | The running mode of the thermostat                   | HVAC           | yes      |                |
| thermostat_heatingdemand     | Number:Dimensionless     | Heating Demand              | The level of heating currently demanded by the thermostat | HVAC       | yes      | %.0f %%        |
| thermostat_coolingdemand     | Number:Dimensionless     | Cooling Demand              | The level of cooling currently demanded by the thermostat | HVAC       | yes      | %.0f %%        |
| warning_device               | String                   | Warning device              | Triggers warnings on a warning device                | Siren          | yes      |                |
| windowcovering_lift          | Rollershutter            | Window Covering Lift        | Sets the window covering level                       | Blinds         | no       |                |


## Full Example

### Thing Configuration
```java
Thing configuration example for the Matter controller:
Thing matter:controller:myController [ port=1234, host="192.168.1.100", portId=1 ]

Thing configuration example for a Matter endpoint:
Thing matter:endpoint:myEndpoint [ nodeId="node1", endpointId=1 ]
```

### Item Configuration
```java
Dimmer MyEndpointDimmer "My Endpoint Dimmer" { channel="matter:endpoint:myController:myEndpoint:LevelControl_dimmer" }
```

### Sitemap Configuration
```perl
Optional Sitemap configuration:
sitemap home label="Home"
{
    Frame label="Matter Devices"
    {
        Dimmer item=MyEndpointDimmer
    }
}
```