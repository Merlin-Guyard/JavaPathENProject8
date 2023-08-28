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
        return usersService.getUserLocation(getUser(userName));
    }

    @RequestMapping("/getNearbyAttractions")
    public List<NearbyAttractionsDTO> getNearbyAttractions(@RequestParam String userName) {
        return rewardsService.get5NearestAttractions(usersService.getUser(userName));
    }

    @RequestMapping("/getRewards")
    public List<UserReward> getRewards(@RequestParam String userName) {
        return usersService.getUserRewards(getUser(userName));
    }

    @RequestMapping("/getAllCurrentLocations")
    public List<AllCurrentLocationsDTO> getAllCurrentLocations() {
        return usersService.getAllUsersLocations();
    }

    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        return tripDealsService.getTripDeals(getUser(userName));
    }

    private User getUser(String userName) {
        return usersService.getUser(userName);
    }

}