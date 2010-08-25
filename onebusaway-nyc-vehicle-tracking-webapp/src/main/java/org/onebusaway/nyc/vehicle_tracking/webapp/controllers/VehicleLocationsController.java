package org.onebusaway.nyc.vehicle_tracking.webapp.controllers;

import java.util.List;

import org.onebusaway.nyc.vehicle_tracking.services.VehicleLocationService;
import org.onebusaway.realtime.api.VehicleLocationRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class VehicleLocationsController {

  private VehicleLocationService _vehicleLocationService;

  @Autowired
  public void setVehicleLocationService(
      VehicleLocationService vehicleLocationService) {
    _vehicleLocationService = vehicleLocationService;
  }

  @RequestMapping("/vehicle-locations.do")
  public ModelAndView index() {

    List<VehicleLocationRecord> records = _vehicleLocationService.getLatestProcessedVehicleLocationRecords();

    ModelAndView mv = new ModelAndView("vehicle-locations.jspx");
    mv.addObject("records", records);
    return mv;
  }
  
  @RequestMapping("/vehicle-location.do")
  public ModelAndView vehicleLocation(@RequestParam() String vehicleId) {
    VehicleLocationRecord record = _vehicleLocationService.getVehicleLocationForVehicle(vehicleId);
    return new ModelAndView("json","record",record);
  }
  
  @RequestMapping("/vehicle-location!reset.do")
  public ModelAndView resetVehicleLocation(@RequestParam() String vehicleId) {
    _vehicleLocationService.resetVehicleLocation(vehicleId);
    return new ModelAndView("json","record",null);
  }
}
