package org.onebusaway.nyc.vehicle_tracking.impl;

import org.onebusaway.nyc.vehicle_tracking.services.VehicleTrackingConfigurationService;
import org.springframework.stereotype.Component;

@Component
class VehicleTrackingConfigurationServiceImpl implements
    VehicleTrackingConfigurationService {

  private double _vehicleOffRouteDistanceThreshold = 500;

  private int _vehicleStalledTimeThreshold = 2 * 60;

  /****
   * {@link VehicleTrackingConfigurationService} Interface
   ****/

  @Override
  public double getVehicleOffRouteDistanceThreshold() {
    return _vehicleOffRouteDistanceThreshold;
  }

  @Override
  public int getVehicleStalledTimeThreshold() {
    return _vehicleStalledTimeThreshold;
  }

  @Override
  public void setVehicleOffRouteDistanceThreshold(double vehicleOffRouteDistanceThreshold) {
    _vehicleOffRouteDistanceThreshold = vehicleOffRouteDistanceThreshold;
  }

  @Override
  public void setVehicleStalledTimeThreshold(int vehicleStalledTimeThreshold) {
    _vehicleStalledTimeThreshold = vehicleStalledTimeThreshold;
  }
}