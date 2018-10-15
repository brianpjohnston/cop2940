package com.brianjohnston.dao;

import org.hibernate.Session;

/**
 * StudentLabTracker
 * 6/11/16 6:46 PM
 */
public class BaseDAO {
    public static int getCount(Session session, String namedQuery){
        return ((Long)session.getNamedQuery(namedQuery).uniqueResult()).intValue();
    }
}
