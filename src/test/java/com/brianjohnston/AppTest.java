package com.brianjohnston;

import com.brianjohnston.models.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void testDb() {
//        Session session = HibernateUtil.getSessionFactory()
//                                       .openSession();
//
//        session.beginTransaction();
//
//        Location location = new Location();
//        location.setBuilding("ChemBuilding");
//        location.setCampus("Seminole");
//        location.setRoom(202);
//
//        //create student
//        Student student = new Student();
//        student.setFirstName("Harry");
//        student.setLastName("Potter");
//
//        //create instructor
//        final Instructor instructor = new Instructor();
//        instructor.setFirstName("Severus");
//        instructor.setLastName("Snape");
//
//        //create a course that is taught by above instructor
//        Course chem1 = new Course();
//        chem1.setInstructor(instructor);
//        chem1.setLocation(location);
//
//        //set the courses the instructor teaches
//        instructor.setCourses(Collections.singletonList(chem1));
//
//        //create lab event
//        LabEvent labEvent = new LabEvent();
//        labEvent.setStudent(student);
//        labEvent.setInstructor(instructor);
//        labEvent.setEnter(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)));
//        labEvent.setLeave(new Date(System.currentTimeMillis()));
//        labEvent.setCourse(chem1);
//
//        session.beginTransaction();
//        session.save(labEvent);
//        session.getTransaction()
//               .commit();
//
//        final Query query = session.createQuery("From Student");
//        final List<Student> list = query.list();
//        list.forEach(student1 -> System.out.println(student1.getId()));
//
////        final Query labQuery = session.createQuery("FROM LabEvent WHERE student.id = 13");
////        final List<LabEvent> list1 = labQuery.list();
////
////        list1.forEach(labevent1 -> System.out.println("lab id " + labevent1.getId() + " student id: " + labevent1.getStudent().getId()));
////        final LabEvent labEvent1 = list1.get(0);
//        System.out.println("---------");
////        System.out.println(labEvent1);
//
//        String hql = "from LabEvent where enter > :time";
//        Query timeQuery = session.createQuery(hql);
//        timeQuery.setDate("time", new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
//        final List<LabEvent> timebasedresults = timeQuery.list();
//        timebasedresults.forEach(System.out::println);
//
//        final Query namedQuery = session.getNamedQuery("findStudentByName")
//                                        .setParameter("first", "Harry")
//                                        .setParameter("last", "Potter");
//        query.setMaxResults(1).uniqueResult();
//        final List<Student> studentsByName = namedQuery.list();
//        studentsByName.forEach(System.out::println);
//
//        int numCourses = ((Long)session.getNamedQuery("numberOfCourses").uniqueResult()).intValue();
//        System.out.println(numCourses);
    }
}
