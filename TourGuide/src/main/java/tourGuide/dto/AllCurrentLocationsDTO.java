package tourGuide.dto;

import gpsUtil.location.Location;

import java.util.UUID;

public class AllCurrentLocationsDTO {

    private UUID userID;
    private Location location;

    public AllCurrentLocationsDTO() {
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
