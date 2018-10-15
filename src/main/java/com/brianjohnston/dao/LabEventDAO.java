package com.brianjohnston.dao;

import com.brianjohnston.models.Instructor;
import com.brianjohnston.models.LabEvent;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * StudentLabTracker
 * 6/11/16 7:50 PM
 */
public class LabEventDAO {
    public static List<LabEvent> getLoggedInEvents(Session session, Instructor instructor) {
        final Query query = session.getNamedQuery("getLoggedInEvents");
        query.setParameter("id", instructor.getId());
        return query.list();
    }

    public static List<LabEvent> getEventsForInstructor(Session session, Instructor instructor) {
        final Query query = session.getNamedQuery("getEventsByInstructor");
        query.setParameter("id", instructor.getId());
        return query.list();
    }
}
