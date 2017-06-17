package com.badrtask.gasstations2.pojos;

/**
 * Created by ITIain on 6/17/2017.
 */

public class Place {

    private String name;
    private String rating;

    private double lat;
    private double lng;

    private double distance;

    /**
     *
     * @return
     */
    public double getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     */
    public double getLng() {
        return lng;
    }

    /**
     *
     * @param lng
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}// End of class
