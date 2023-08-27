package tourGuide;

import java.util.List;

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
import tourGuide.user.UserReward;
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
    public VisitedLocation getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = usersService.getUserLocation(getUser(userName));
        return visitedLocation;
    }

    @RequestMapping("/getNearbyAttractions")
    public List<NearbyAttractionsDTO> getNearbyAttractions(@RequestParam String userName) {
        List<NearbyAttractionsDTO> nearbyAttractionsDTO = rewardsService.get5NearestAttractions(usersService.getUserLocation(usersService.getUser(userName)).location);
        return nearbyAttractionsDTO;
    }

    @RequestMapping("/getRewards")
    public List<UserReward> getRewards(@RequestParam String userName) {
        List<UserReward> userRewards = usersService.getUserRewards(getUser(userName));
        return userRewards;
    }

    @RequestMapping("/getAllCurrentLocations")
    public List<AllCurrentLocationsDTO> getAllCurrentLocations() {
        List<AllCurrentLocationsDTO> allCurrentLocationsDTOS = usersService.getAllUsersLocations();
        return allCurrentLocationsDTOS;
    }

    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tripDealsService.getTripDeals(getUser(userName));
        return providers;
    }

    private User getUser(String userName) {
        return usersService.getUser(userName);
    }

}