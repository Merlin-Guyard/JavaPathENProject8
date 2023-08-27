package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import java.util.ArrayList;
import java.util.List;

public class AttractionsService {

    private final GpsUtil gpsUtil;

    public AttractionsService(GpsUtil gpsUtil) {
        this.gpsUtil = gpsUtil;
    }

    public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
        List<Attraction> nearbyAttractions = new ArrayList<>();
        for(Attraction attraction : gpsUtil.getAttractions()) {

            //TODO : séparer
            if(rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
                nearbyAttractions.add(attraction);
            }
        }

        return nearbyAttractions;
    }
}
