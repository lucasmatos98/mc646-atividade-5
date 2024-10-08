package activity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SmartEnergyManagementSystemTest {

    @Test
    public void testManageEnergy() {
        SmartEnergyManagementSystem smartEnergyManagementSystem = new SmartEnergyManagementSystem();

        double currentPrice = 15.0;
        double priceThreshold = 10.0;
        Map<String, Integer> devicePriorities = new HashMap<>();
        devicePriorities.put("Device1", 2);
        devicePriorities.put("Device2", 1);
        LocalDateTime currentTime = LocalDateTime.now();
        double currentTemperature = 20.0;
        double[] desiredTemperatureRange = {18.0, 22.0};
        double energyUsageLimit = 50.0;
        double totalEnergyUsedToday = 40.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = new ArrayList<>();
        scheduledDevices.add(new SmartEnergyManagementSystem.DeviceSchedule("Device3", currentTime));

        SmartEnergyManagementSystem.EnergyManagementResult result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices);

        assertEquals(false, result.deviceStatus.get("Device1"));
        assertEquals(true, result.deviceStatus.get("Device2"));
        assertTrue(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
    }

    @Test
    public void testNoEnergySavingMode() {
        SmartEnergyManagementSystem smartEnergyManagementSystem = new SmartEnergyManagementSystem();

        double currentPrice = 5.0;
        double priceThreshold = 10.0;
        Map<String, Integer> devicePriorities = new HashMap<>();
        devicePriorities.put("Device1", 2);
        devicePriorities.put("Device2", 1);
        LocalDateTime currentTime = LocalDateTime.now();
        double currentTemperature = 20.0;
        double[] desiredTemperatureRange = {18.0, 22.0};
        double energyUsageLimit = 50.0;
        double totalEnergyUsedToday = 40.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = new ArrayList<>();

        SmartEnergyManagementSystem.EnergyManagementResult result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices);

        assertEquals(true, result.deviceStatus.get("Device1"));
        assertEquals(true, result.deviceStatus.get("Device2"));
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
    }

    @Test
    public void testNightMode() {
        SmartEnergyManagementSystem smartEnergyManagementSystem = new SmartEnergyManagementSystem();

        double currentPrice = 5.0;
        double priceThreshold = 10.0;
        Map<String, Integer> devicePriorities = new HashMap<>();
        devicePriorities.put("Device1", 2);
        devicePriorities.put("Security", 1);
        devicePriorities.put("Refrigerator", 1);
        LocalDateTime currentTime = LocalDateTime.of(2023, 10, 31, 23, 0, 0);
        double currentTemperature = 20.0;
        double[] desiredTemperatureRange = {18.0, 22.0};
        double energyUsageLimit = 50.0;
        double totalEnergyUsedToday = 40.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = new ArrayList<>();

        SmartEnergyManagementSystem.EnergyManagementResult result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices);

        assertEquals(false, result.deviceStatus.get("Device1"));
        assertEquals(true, result.deviceStatus.get("Security"));
        assertEquals(true, result.deviceStatus.get("Refrigerator"));
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
    }

    @Test
    public void testTemperatureRegulationHeating() {
        SmartEnergyManagementSystem smartEnergyManagementSystem = new SmartEnergyManagementSystem();

        double currentPrice = 5.0;
        double priceThreshold = 10.0;
        Map<String, Integer> devicePriorities = new HashMap<>();
        devicePriorities.put("Device1", 2);
        devicePriorities.put("Device2", 1);
        LocalDateTime currentTime = LocalDateTime.now();
        double currentTemperature = 15.0;
        double[] desiredTemperatureRange = {18.0, 22.0};
        double energyUsageLimit = 50.0;
        double totalEnergyUsedToday = 40.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = new ArrayList<>();

        SmartEnergyManagementSystem.EnergyManagementResult result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices);

        Boolean heatingStatus = result.deviceStatus.get("Heating");
        Boolean coolingStatus = result.deviceStatus.get("Cooling");

        assertTrue(heatingStatus != null && heatingStatus);
        assertFalse(coolingStatus != null && coolingStatus);
        assertFalse(result.energySavingMode);
        assertTrue(result.temperatureRegulationActive);
    }

    @Test
    public void testTemperatureRegulationCooling() {
        SmartEnergyManagementSystem smartEnergyManagementSystem = new SmartEnergyManagementSystem();

        double currentPrice = 5.0;
        double priceThreshold = 10.0;
        Map<String, Integer> devicePriorities = new HashMap<>();
        devicePriorities.put("Device1", 2);
        devicePriorities.put("Device2", 1);
        LocalDateTime currentTime = LocalDateTime.now();
        double currentTemperature = 25.0;
        double[] desiredTemperatureRange = {18.0, 22.0};
        double energyUsageLimit = 50.0;
        double totalEnergyUsedToday = 40.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = new ArrayList<>();

        SmartEnergyManagementSystem.EnergyManagementResult result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices);

        Boolean heatingStatus = result.deviceStatus.get("Heating");
        Boolean coolingStatus = result.deviceStatus.get("Cooling");

        assertFalse(heatingStatus != null && heatingStatus);
        assertTrue(coolingStatus != null && coolingStatus);
        assertFalse(result.energySavingMode);
        assertTrue(result.temperatureRegulationActive);
    }

    @Test
    public void testTemperatureRegulationNoAction() {
        SmartEnergyManagementSystem smartEnergyManagementSystem = new SmartEnergyManagementSystem();

        double currentPrice = 5.0;
        double priceThreshold = 10.0;
        Map<String, Integer> devicePriorities = new HashMap<>();
        devicePriorities.put("Device1", 2);
        devicePriorities.put("Device2", 1);
        LocalDateTime currentTime = LocalDateTime.now();
        double currentTemperature = 20.0;
        double[] desiredTemperatureRange = {18.0, 22.0};
        double energyUsageLimit = 50.0;
        double totalEnergyUsedToday = 40.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = new ArrayList<>();

        SmartEnergyManagementSystem.EnergyManagementResult result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices);

        Boolean heatingStatus = result.deviceStatus.get("Heating");
        Boolean coolingStatus = result.deviceStatus.get("Cooling");

        assertFalse(heatingStatus != null && heatingStatus);
        assertFalse(coolingStatus != null && coolingStatus);
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
    }

    @Test
    public void testEnergyUsageLimit() {
        SmartEnergyManagementSystem smartEnergyManagementSystem = new SmartEnergyManagementSystem();

        double currentPrice = 5.0;
        double priceThreshold = 10.0;
        Map<String, Integer> devicePriorities = new HashMap<>();
        devicePriorities.put("Device1", 2);
        devicePriorities.put("Device2", 1);
        LocalDateTime currentTime = LocalDateTime.now();
        double currentTemperature = 20.0;
        double[] desiredTemperatureRange = {18.0, 22.0};
        double energyUsageLimit = 50.0;
        double totalEnergyUsedToday = 50.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = new ArrayList<>();

        SmartEnergyManagementSystem.EnergyManagementResult result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices);

        assertFalse(result.deviceStatus.get("Device1"));
        assertTrue(result.deviceStatus.get("Device2"));
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
    }

    @Test
    public void testScheduledDevices() {
        SmartEnergyManagementSystem smartEnergyManagementSystem = new SmartEnergyManagementSystem();

        double currentPrice = 5.0;
        double priceThreshold = 10.0;
        Map<String, Integer> devicePriorities = new HashMap<>();
        devicePriorities.put("Device1", 2);
        devicePriorities.put("Device2", 1);
        LocalDateTime currentTime = LocalDateTime.now();
        double currentTemperature = 20.0;
        double[] desiredTemperatureRange = {18.0, 22.0};
        double energyUsageLimit = 50.0;
        double totalEnergyUsedToday = 40.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = new ArrayList<>();
        scheduledDevices.add(new SmartEnergyManagementSystem.DeviceSchedule("Device3", currentTime));

        SmartEnergyManagementSystem.EnergyManagementResult result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices);

        assertTrue(result.deviceStatus.get("Device3"));
    }
}