package tourGuide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.dto.NearbyAttractionsDTO;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 2000;
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;
	
	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	public List<NearbyAttractionsDTO> get5NearestAttractions(User user) {
		Location userLocation = user.getLastVisitedLocation().location;

		List<Attraction> Attractions = gpsUtil.getAttractions();

		Map<Double, Attraction> allAttractionsWithDistance = new TreeMap<>();
		for (Attraction attraction : Attractions) {
			allAttractionsWithDistance.put(getDistance(attraction, userLocation), attraction);
		}

		ArrayList<Attraction> nearby5Attractions = new ArrayList<Attraction>(allAttractionsWithDistance.values());
		ArrayList<Double> distance = new ArrayList<>(allAttractionsWithDistance.keySet());
		while (nearby5Attractions.size() > 5) {
			nearby5Attractions.remove(5);
		}

		while (distance.size() > 5) {
			distance.remove(5);
		}

		List<NearbyAttractionsDTO> nearbyAttractionsDTOS = new ArrayList<NearbyAttractionsDTO>();
		for (int i=0; i < nearby5Attractions.size(); i++){

			NearbyAttractionsDTO nearbyAttractionsDTO = new NearbyAttractionsDTO(
					nearby5Attractions.get(i).attractionName,
					nearby5Attractions.get(i).longitude,
					nearby5Attractions.get(i).latitude,
					userLocation.longitude,
					userLocation.latitude,
					distance.get(i),
					1
			);

			nearbyAttractionsDTOS.add(nearbyAttractionsDTO);
		}

		return nearbyAttractionsDTOS;
	}
	
	public void calculateRewards(User user) {
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsUtil.getAttractions();
		
		for(VisitedLocation visitedLocation : userLocations) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}
	}
	
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}
	
	private int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

}
