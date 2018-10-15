package com.brianjohnston.dao;

import com.brianjohnston.models.Location;
import org.hibernate.Session;

import java.util.List;

/**
 * StudentLabTracker
 * 6/11/16 6:48 PM
 */
public class LocationDAO {
    public static int getLocationCount(Session session){
        return BaseDAO.getCount(session, "numberOfLocations");
    }
    public static List<Location> getLocations(Session session){
        return (List<Location>)session.createQuery("from Location").list();
    }
}
