# Matter Binding

The Matter Binding for openHAB allows seamless integration with Matter-compatible devices.

**Please note this binding requires openHAB 4.3 snapshot builds or later that support discovery codes when searching for devices.** 

# General Matter Ecosystem Overview

Matter is an open-source connectivity standard for smart home devices, allowing seamless communication between a wide range of devices, controllers, and ecosystems. 

Below is a high-level overview of the Matter ecosystem as well as common terminology used in the Matter standard.

## Matter and IPv6

Matter **requires** IPv6 to be enabled and be routable between openHAB and the Matter device.  This means IPv6 needs to be enabled on the host openHAB is running, and the network must be able route IPv6 unicast and multicast messages.  Docker, VLANs, subnets and other configurations can prohibit Matter from working if not configured correctly.  If devices are not able to be discovered with openHAB, this will often be the root issue.   

## Matter Devices

### Nodes and Endpoints

In the Matter ecosystem, a **node** represents a single device that joins a Matter network and will have a locally routable IPv6 address. A **node** can have multiple **endpoints**, which are logical representations of specific features or functionalities of the device. For example, a smart thermostat (node) may have an endpoint for general thermostat control (heating, cooling, current temperature, operating state, etc....) and another endpoint for humidity sensing.  Many devices will only have a single endpoint.  [Matter Bridges](#bridges) will expose multiple endpoints for each device they are bridging, and the bridge itself will be a node.

**Example:**
- A Thermostat node with an endpoint for general temperature control and another endpoint for a remote temperature or humidity sensor.

### Controllers

A **controller** manages the interaction between Matter devices and other parts of the network. Controllers can send commands, receive updates, and facilitate device communication. They also handle the commissioning process when new devices are added to the network.

**Example:**
- openHAB or another smart home hub or a smartphone app that manages your smart light bulbs, door locks, and sensors (Google Home, Apple Home, Amazon Alexa, etc...)

### Bridges

A **bridge** is a special type of node that connects non-Matter devices to a Matter network, effectively translating between protocols. Bridges allow legacy devices to be controlled via the Matter standard.

**Example:**
- A bridge that connects Zigbee or Z-Wave devices, making them accessible within a Matter ecosystem. The Ikea Dirigera and Philips Hue Bridge both act as matter bridges and are supported in openHAB.

### Thread Border Routers

A **Thread Border Router** is a device that allows devices connected via Thread (a low-power wireless protocol) to communicate with devices on other networks, such as Wi-Fi or Ethernet. It facilitates IPv6-based communication between Thread networks and the local IP network.

**Example:**
- An Open Thread Boarder Router (open source) as well as recent versions of Apple TVs, Amazon Echos and Google Nest Hubs all have embedded thread boarder routers. 

## IPv6 and Network Connectivity

Matter devices operate over an IPv6 network, and obtaining an IPv6 address is required for communication. Devices can connect to the network via different interfaces:

### Ethernet
Ethernet-connected Matter devices receive an IPv6 address through standard DHCPv6 or stateless address auto-configuration (SLAAC).

### Wi-Fi
Wi-Fi-enabled Matter devices also receive an IPv6 address using DHCPv6 or SLAAC. They rely on the existing Wi-Fi infrastructure for communication within the Matter ecosystem.

### Thread
Thread-based Matter devices connect to the network via a **Thread Border Router**. They receive an IPv6 address from the Thread router

## IPv6 Requirements

For Matter devices to function correctly, **IPv6 must be enabled** and supported in both the local network (router) and the Matter controllers. Without IPv6, devices won’t be able to communicate properly within the Matter ecosystem. Ensure that your router has IPv6 enabled and that any Matter controllers (like smart hubs, apps or openHAB) are configured to support IPv6 as well.

**Note that environments like Docker require special configurations to enable IPv6**

## Matter Commissioning and Pairing Codes

Commissioning a Matter device involves securely adding it to the network using a **pairing code**. This process ensures that only authorized devices can join the network.

### Pairing Code from the Device
When commissioning a new Matter device, it typically has a printed QR code or numeric pairing code that you scan or enter during setup. This pairing code allows the controller to establish a secure connection to the device and add it to the network. Once a device pairing code is in use, it can not be used again to pair other controllers.

### Additional Pairing Code from a Controller
If a device has already been commissioned and you want to add it to another Matter controller, the existing controller can generate an additional pairing code. This is useful when sharing access to a device across multiple hubs or apps. Apple Home, Google Home, Amazon Alexa and openHAB all support generating pairing codes for existing paired devices.

### Example:
- When setting up a smart lock, you may scan a QR code directly from the lock, or use the 11 digit pairing code printed on it to pair it with openHAB. If you later want to control the lock from another app or hub, you would retrieve a new pairing code directly from openHAB.

# Matter Binding Configuration

## Supported Things

The Matter Binding supports the following types of things:

- `controller`: The main controller that interfaces with Matter devices. It requires configuration parameters such as `port`, `host`, and `portId`.
- `endpoint`: Represents an individual endpoint within the Matter network. Configuration parameters include `nodeId` and `endpointId`.

The Following Matter Device types (endpoints) are currently supported:

| Device Type                    | Description                                                                 | Associated Clusters                               |
|---------------------------------|-----------------------------------------------------------------------------|---------------------------------------------------|
| OnOffLight                     | A simple light device that can be turned on or off                          | On/Off                                            |
| OnOffLightSwitch               | A switch to control On/Off lights                                            | On/Off                                            |
| OnOffPlugInUnit                | A plug-in unit that can be turned on or off                                  | On/Off                                            |
| DimmableLight                  | A light device that supports dimming in addition to on/off                   | On/Off, Level Control                             |
| DimmablePlugInUnit             | A plug-in unit that supports dimming and on/off control                      | On/Off, Level Control                             |
| DimmerSwitch                   | A switch to control dimmable lights                                          | On/Off, Level Control                             |
| ColorDimmerSwitch              | A switch to control both dimming and color of lights                         | On/Off, Level Control, Color Control              |
| ExtendedColorLight             | A light that supports extended color control (e.g., RGB) and dimming         | On/Off, Level Control, Color Control              |
| ColorTemperatureLight          | A light that supports color temperature control and dimming                 | On/Off, Level Control, Color Control (Temperature)|
| Thermostat                     | A device that controls temperature in an environment                        | Thermostat, Temperature Measurement               |
| WindowCovering                 | A device that controls window coverings such as blinds or curtains           | Window Covering                                   |

## Discovery

Matter controllers must be added manually.  Endpoint (devices) will be discovered when a `pairCode` is used to search for a device to add. 

### Device Pairing: General

In order to pair a device, you must have an 11 digit manual pairing code (eg 123-4567-8901 or 12345678901) or a QR Code (eg MT:ABCDEF1234567890123).  If the device has not been paired before, use the code provided by the manufacturer and **ensure the device is in pairing mode**, refer to your devices instructions for pairing for more information.

If the device is paired with another Matter ecosystem (Apple, Google, Amazon, etc..) use that ecosystem to generate a pairing code and search for devices.  The pairing code and device will only be available for commissioning for a limited time.  Refer to the ecosystem that generated the code for the exact duration (typically 1-15 minutes).

### Device Pairing: Thread Devices

Thread devices require a Thread Border Router and a bluetooth enabled device to facilitate the thread joining process (typically a mobile device).  Until there is a supported thread border router integration in openHAB and the openHAB mobile apps, it's strongly recommended to pair the device to a commercial router with thread support first (Apple TV 4k, Google Nest Hub 2, Amazon Gen 4 Echo, etc... ), then generate a matter pairing code and add the device normally.  This will still allow openHAB to have direct access to the device using only the embedded thread border router and does not interact with the underlying providers home automation stack.

Support for using a Open Thread Border Router has been verified to work and will be coming soon to openHAB, but in some cases requires strong expertise in IPv6 routing as well as support in our mobile clients. 

## Binding Configuration

This binding does not require any general configuration settings.

## Thing Configuration

### Controller Thing Configuration

The controller thing must be created manually before devices can be discovered.

| Name                      | Type    | Description                                                                                                                       | Default | Required | Advanced |
|---------------------------|---------|-----------------------------------------------------------------------------------------------------------------------------------|---------|----------|----------|
| nodeId                    | number  | The matter node ID for this controller                                                                                            | 0       | yes      | no       |
| decommissionNodesOnDelete | boolean | Decommission (unpair) Matter nodes from the Matter network when the last endpoint thing of a node is deleted/removed from openHAB | false    | yes      | no       |
| port                      | number  | The port number for the controller                                                                                                | N/A     | no       | yes      |
| host                      | text    | The host address of the controller                                                                                                | N/A     | no       | yes      |

### Endpoint Thing Configuration

Endpoints are discovered automatically (see [Discovery](#Discovery) for more information).

| Name       | Type   | Description                        | Default | Required | Advanced |
|------------|--------|------------------------------------|---------|----------|----------|
| nodeId     | text   | The node ID of the endpoint        | N/A     | yes      | no       |
| endpointId | number | The endpoint ID within the node    | N/A     | yes      | no       |

## Channels

### Controller Channels
The Matter Binding provides various channels for controlling and monitoring Matter devices. Here are some of the available channels:

| Channel  | Type   | Read/Write | Description                   |
|----------|--------|------------|-------------------------------|
| pairCode  | String | RW         | 11 digit pairing code (with out without dashes), QR pairing code,  or short code and key (separated by a space)       |

### Endpoint Channels
Endpoint channels are dynamically added based on the endpoint type and matter cluster supported.
Possible channels include:

## Endpoint Channels

| Channel ID                   | Item Type                | Label                       | Description                                          | Category       | ReadOnly | Pattern       |
|------------------------------|--------------------------|-----------------------------|------------------------------------------------------|----------------|----------|----------------|
| battery_voltage              | Number:ElectricPotential | Battery Voltage             | The current battery voltage                          | Energy         | yes      | %.1f %unit%    |
| battery_alarm                | String                   | Battery Alarm               | The battery alarm state                              | Energy         | yes      |                |
| colorcontrol_color                  | Color                    | Color                       | Control the color of a light                         | ColorLight     | no       |                |
| door_state                   | Switch                   | Door Lock State             | Locks and unlocks the door                           | Door           | no       |                |
| fancontrol_fanmode                   | Number                   | Fan Mode                    | Set the fan mode                                     | HVAC           | no       |                |
| onoffcontrol_onoff                 | Switch                   | Switch                      | Switches the power on and off                        | Light          | no       |                |
| levelcontrol_level                 | Dimmer                   | Dimmer                      | Sets the level of the light                          | Light          | no       |                |
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
Dimmer MyEndpointDimmer "My Endpoint Dimmer" { channel="matter:endpoint:myController:myEndpoint:levelcontrol_level" }
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