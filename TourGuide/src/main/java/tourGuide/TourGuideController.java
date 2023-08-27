package tourGuide;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.dto.AllCurrentLocationsDTO;
import tourGuide.dto.NearbyAttractionsDTO;
import tourGuide.service.RewardsService;
import tourGuide.service.TripDealsService;
import tourGuide.service.UsersService;
import tourGuide.user.User;
import tripPricer.Provider;

@RestController
public class TourGuideController {

    private final UsersService usersService;
    private final RewardsService rewardsService;
    private TripDealsService tripDealsService;

    public TourGuideController(UsersService usersService, RewardsService rewardsService, TripDealsService tripDealsService) {
        this.usersService = usersService;
        this.rewardsService = rewardsService;
        this.tripDealsService = tripDealsService;
    }

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    @RequestMapping("/getLocation") 
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = usersService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }

    @RequestMapping("/getNearbyAttractions") 
    public List<NearbyAttractionsDTO> getNearbyAttractions(@RequestParam String userName) {
        List<NearbyAttractionsDTO> nearbyAttractionsDTO = rewardsService.get5NearestAttractions(usersService.getUserLocation(usersService.getUser(userName)).location);
    	return nearbyAttractionsDTO;
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(usersService.getUserRewards(getUser(userName)));
    }

    // TODO: Get a list of every user's most recent location as JSON
    //- Note: does not use gpsUtil to query for their current location,
    //        but rather gathers the user's current location from their stored location history.
    //
    // Return object should be the just a JSON mapping of userId to Locations similar to:
    //     {
    //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
    //        ...
    //     }
    @RequestMapping("/getAllCurrentLocations")
    public List<AllCurrentLocationsDTO> getAllCurrentLocations() {
        List<AllCurrentLocationsDTO> allCurrentLocationsDTOS = usersService.getAllUsersLocations();
    	return allCurrentLocationsDTOS;
    }
    
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tripDealsService.getTripDeals(getUser(userName));
    	return JsonStream.serialize(providers);
    }
    
    private User getUser(String userName) {
    	return usersService.getUser(userName);
    }
   

}