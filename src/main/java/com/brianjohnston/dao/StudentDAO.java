package com.brianjohnston.dao;

import com.brianjohnston.models.Student;
import org.hibernate.Session;

import java.util.List;

/**
 * StudentLabTracker
 * 6/11/16 6:53 PM
 */
public class StudentDAO {
    public static int getStudentCount(Session session){
        return BaseDAO.getCount(session, "numberOfStudents");
    }

    public static List<Student> getStudents(Session session){
        return session.createQuery("from Student ").list();
    }
}
