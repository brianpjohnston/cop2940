package com.brianjohnston.dao;

import com.brianjohnston.models.Course;
import org.hibernate.Session;

import java.util.List;

/**
 * StudentLabTracker
 * 6/11/16 6:38 PM
 */
public class CourseDAO {
    public static int getCourseCount(Session session){
        return BaseDAO.getCount(session, "numberOfCourses");
    }

    public static List<Course> getCourses(Session session){
        return session.createQuery("from Course ").list();
    }
}
