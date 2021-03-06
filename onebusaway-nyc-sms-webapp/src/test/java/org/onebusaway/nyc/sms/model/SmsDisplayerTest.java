/**
 * Copyright (c) 2011 Metropolitan Transportation Authority
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.onebusaway.nyc.sms.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

import org.junit.Test;
import org.onebusaway.nyc.presentation.model.FormattingContext;
import org.onebusaway.nyc.presentation.model.RouteItem;
import org.onebusaway.nyc.presentation.model.DistanceAway;
import org.onebusaway.nyc.presentation.model.Mode;
import org.onebusaway.nyc.presentation.model.search.SearchResult;
import org.onebusaway.nyc.presentation.model.search.StopSearchResult;

public class SmsDisplayerTest {
  
  private RouteItem makeAvailableRoute(List<DistanceAway> distanceAways) {
    // helper function to create available routes
    return new RouteItem("routeid", "route description", "route headsign", "0", distanceAways);
  }

  @Test
  public void testSingleStopResponseNoArrivals() {
	  RouteItem availableRoute = makeAvailableRoute(new ArrayList<DistanceAway>());
    List<RouteItem> routes = new ArrayList<RouteItem>();
    routes.add(availableRoute);
    StopSearchResult stopSearchResult = new StopSearchResult("123456","foo bar", Arrays.asList(new Double[] {42.0, 74.0}), "N", routes, null);
    List<SearchResult> searchResults = new ArrayList<SearchResult>();
    searchResults.add(stopSearchResult);
    
    SmsDisplayer sms = new SmsDisplayer(searchResults);
    sms.singleStopResponse();
    String actual = sms.toString();
    assertEquals("routeid: No upcoming arrivals\n", actual);
  }

  @Test
  public void testSingleStopResponseCoupleOfArrivals() {
    DistanceAway distanceAway1 = new DistanceAway(1, 300, new Date(), Mode.SMS,300, FormattingContext.STOP, null);
    DistanceAway distanceAway2 = new DistanceAway(2, 900, new Date(), Mode.SMS,300, FormattingContext.STOP, null);
    List<DistanceAway> distanceAways = new ArrayList<DistanceAway>();
    distanceAways.add(distanceAway1);
    distanceAways.add(distanceAway2);
    RouteItem availableRoute = makeAvailableRoute(distanceAways);
    List<RouteItem> routes = new ArrayList<RouteItem>();
    routes.add(availableRoute);
    StopSearchResult stopSearchResult = new StopSearchResult("123456","foo bar", Arrays.asList(new Double[] {42.0, 74.0}), "N", routes, null);
    List<SearchResult> searchResults = new ArrayList<SearchResult>();
    searchResults.add(stopSearchResult);
    
    SmsDisplayer sms = new SmsDisplayer(searchResults);
    sms.singleStopResponse();
    String actual = sms.toString();
    String exp = "routeid: 1 stop away\n" + "routeid: 2 stops away\n";
    assertEquals(exp, actual);
  }

  @Test
  public void testSingleStopResponseLotsOfArrivals() {
    List<DistanceAway> distanceAways = new ArrayList<DistanceAway>();
    for (int i = 0; i < 20; i++) {
      DistanceAway distanceAway = new DistanceAway(i+1, (i+1) * 100, new Date(),  Mode.SMS,300, FormattingContext.STOP, null);
      distanceAways.add(distanceAway);
    }
    RouteItem availableRoute = makeAvailableRoute(distanceAways);
    List<RouteItem> routes = new ArrayList<RouteItem>();
    routes.add(availableRoute);
    StopSearchResult stopSearchResult = new StopSearchResult("123456","foo bar", Arrays.asList(new Double[] {42.0, 74.0}), "N", routes, null);
    List<SearchResult> searchResults = new ArrayList<SearchResult>();
    searchResults.add(stopSearchResult);
    
    SmsDisplayer sms = new SmsDisplayer(searchResults);
    sms.singleStopResponse();
    String actual = sms.toString();
    String exp = "routeid: at stop\n" +
                 "routeid: 2 stops away\n" +
                 "routeid: 3 stops away\n" +
                 "routeid: 0.1 mi. away\n" +
                 "routeid: 0.1 mi. away\n" +
                 "routeid: 0.1 mi. away\n" +
                 "routeid: 0.1 mi. away\n";
    assertEquals(exp, actual);
  }

  @Test
  public void testNoResultsResponse() {
    List<SearchResult> searchResults = new ArrayList<SearchResult>();
    SmsDisplayer sms = new SmsDisplayer(searchResults);
    sms.noResultsResponse();
    String actual = sms.toString();
    assertEquals("No stops found\n", actual);
  }
  
  private StopSearchResult makeStopSearchResult(List<RouteItem> routes, String stopDirection) {
    StopSearchResult stopSearchResult = new StopSearchResult("AgencyId_123456","foo bar", Arrays.asList(new Double[] {42.0, 74.0}), stopDirection, routes, null);
    return stopSearchResult;
  }
  
  @Test
  public void testTwoStopResponseNoArrivals() {
    List<RouteItem> routes = new ArrayList<RouteItem>();
    List<DistanceAway> distanceAways = new ArrayList<DistanceAway>();
    RouteItem availableRoute = makeAvailableRoute(distanceAways);
    routes.add(availableRoute);

    StopSearchResult stopResult1 = makeStopSearchResult(routes, "N");
    StopSearchResult stopResult2 = makeStopSearchResult(routes, "S");
    List<SearchResult> searchResults = new ArrayList<SearchResult>();
    searchResults.add(stopResult1);
    searchResults.add(stopResult2);
    
    SmsDisplayer sms = new SmsDisplayer(searchResults);
    sms.twoStopResponse();
    
    String actual = sms.toString();
    String exp = "N-bound (123456):\n" +
                 "routeid: No upcoming arrivals\n" +
                 "S-bound (123456):\n" +
                 "routeid: No upcoming arrivals\n";
    assertEquals(exp, actual);
  }

  @Test
  public void testTwoStopResponse() {
    List<RouteItem> routes = new ArrayList<RouteItem>();
    List<DistanceAway> distanceAways = new ArrayList<DistanceAway>();
    distanceAways.add(new DistanceAway(1, 100, new Date(),  Mode.SMS,300, FormattingContext.STOP, null));
    distanceAways.add(new DistanceAway(2, 200, new Date(),  Mode.SMS,300, FormattingContext.STOP, null));
    RouteItem availableRoute = makeAvailableRoute(distanceAways);
    routes.add(availableRoute);
    
    StopSearchResult stopResult1 = makeStopSearchResult(routes, "N");
    StopSearchResult stopResult2 = makeStopSearchResult(routes, "S");
    List<SearchResult> searchResults = new ArrayList<SearchResult>();
    searchResults.add(stopResult1);
    searchResults.add(stopResult2);
    
    SmsDisplayer sms = new SmsDisplayer(searchResults);
    sms.twoStopResponse();
    
    String actual = sms.toString();
    String exp = "N-bound (123456):\n" +
                 "routeid: at stop\n" +
                 "routeid: 2 stops away\n" +
                 "S-bound (123456):\n" +
                 "routeid: at stop\n" +
                 "routeid: 2 stops away\n";
    assertEquals(exp, actual);
  }
  
  @Test
  public void testTwoStopResponseManyArrivals() {
    List<RouteItem> routes = new ArrayList<RouteItem>();
    List<DistanceAway> distanceAways = new ArrayList<DistanceAway>();
    for (int i = 0; i < 20; i++) {
      DistanceAway distanceAway = new DistanceAway(i+1, (i+1) * 100, new Date(), Mode.SMS,300, FormattingContext.STOP, null);
      distanceAways.add(distanceAway);
    }
    RouteItem availableRoute = makeAvailableRoute(distanceAways);
    routes.add(availableRoute);
    
    StopSearchResult stopResult1 = makeStopSearchResult(routes, "N");
    StopSearchResult stopResult2 = makeStopSearchResult(routes, "S");
    List<SearchResult> searchResults = new ArrayList<SearchResult>();
    searchResults.add(stopResult1);
    searchResults.add(stopResult2);
    
    SmsDisplayer sms = new SmsDisplayer(searchResults);
    sms.twoStopResponse();
    
    String actual = sms.toString();
    String exp = "N-bound (123456):\n" +
                 "routeid: at stop\n" +
                 "routeid: 2 stops away\n" +
                 "routeid: 3 stops away\n" +
                 "S-bound (123456):\n" +
                 "routeid: at stop\n" +
                 "routeid: 2 stops away\n" +
                 "routeid: 3 stops away\n";
    assertEquals(exp, actual);
  }

  @Test
  public void testManyStopResponse() {
    List<RouteItem> routes = new ArrayList<RouteItem>();
    List<DistanceAway> distanceAways = new ArrayList<DistanceAway>();
    distanceAways.add(new DistanceAway(1, 100, new Date(),  Mode.SMS,300, FormattingContext.STOP, null));
    distanceAways.add(new DistanceAway(2, 200, new Date(),  Mode.SMS,300, FormattingContext.STOP, null));
    RouteItem availableRoute = makeAvailableRoute(distanceAways);
    routes.add(availableRoute);
    
    List<SearchResult> searchResults = new ArrayList<SearchResult>();
    searchResults.add(makeStopSearchResult(routes, "N"));
    searchResults.add(makeStopSearchResult(routes, "S"));
    searchResults.add(makeStopSearchResult(routes, "E"));
    searchResults.add(makeStopSearchResult(routes, "W"));

    SmsDisplayer sms = new SmsDisplayer(searchResults);
    sms.manyStopResponse();
    
    String actual = sms.toString();
    String exp = "Send:\n" +
                 "123456 for N-bound\n" +
                 "123456 for S-bound\n" +
                 "123456 for E-bound\n" +
                 "123456 for W-bound\n";
    assertEquals(exp, actual);
  }
  
  @Test
  public void testNoAvailableRoutesCase() {
    List<SearchResult> searchResults = new ArrayList<SearchResult>();
    searchResults.add(makeStopSearchResult(new ArrayList<RouteItem>(), "N"));
    
    SmsDisplayer sms = new SmsDisplayer(searchResults);
    sms.singleStopResponse();
    
    String actual = sms.toString();
    String exp = "No upcoming service at this stop\n";
    assertEquals(exp, actual);
  }

}
