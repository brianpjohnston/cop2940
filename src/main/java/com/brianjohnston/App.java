package com.brianjohnston;

import com.brianjohnston.dao.CourseDAO;
import com.brianjohnston.dao.LabEventDAO;
import com.brianjohnston.dao.LocationDAO;
import com.brianjohnston.dao.StudentDAO;
import com.brianjohnston.models.*;
import org.hibernate.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

import static com.brianjohnston.HibernateUtil.*;

/**
 * You have been hired to create a student tracking system to support student/instructor lab time documentation.
 * <p>
 * Create a program to keep track of when a student arrives and when a student leaves a lab. Also allow an instructor to sign in and sign out of the system.
 * <p>
 * Data that should be track include, student, instructor, courses, locations (campus, building, room) and times.
 * <p>
 * Create a report that will produce a list of instructors and the students that visited the lab under those instructors broken down by date.
 * <p>
 * You can use any development environment you choose, e.g. Python, ASP.Net, C#, Java, PHP, C++, etc. The data can be stored in a flat file or database but should include documentation on the structure of the data. Make sure you provide instructions on how to install, setup and use the application as well. If you have MySQL, SQL Server or Oracle as your database, include all the scripts necessary to load data in your database. I can setup up a database and run the scripts when grading your project. If your project needs to be hosted in IIS, Apache or other web environment please make sure that is documented and any additional modules that need to be installed need to be clearly identified.
 * <p>
 * My recommendation would be to create a native app that would run without the need for additional setup. An example of this would be SQLite which works in Java, .Net and other environments, xbase databases such as Firebird or Access or flat file csv or json text files that would work well in Python, C#, Java and most every other language.
 * <p>
 * If you have any questions, please ask them early. This is a significant project.
 * <p>
 * Usability and functionality should be the prime objectives. You have creative control but please see the rubric to make sure you know what you will be graded on.
 * <p>
 * This submission should include:
 * ·         A zip containing your project files and your database with instructions on how to setup your project. If you use a flat file, xbase database or SQLite for your database, just make sure your hard coded paths are relative.
 * ·         A narrated Jing video in which you describe the operation and features of your project or a Word document that is descriptive enough to function as complete documentation for your application. This documentation should make it possible for a non-technical person to install and use your application.
 * ·         Review the rubric as it is the guide faculty will use for grading.
 */
@SuppressWarnings(value = {"unchecked"})
public class App {
    static final Scanner scan    = new Scanner(System.in);
    static final Session session = getSessionFactory().openSession();


    public static void main(String[] args) throws SQLException, IOException {
        //
        // add shutdown hook so we can properly close sessions and exit safely
        Runtime.getRuntime()
               .addShutdownHook(new Thread(App::quit));
        try {
            System.out.println("Welcome to the Student Lab Tracking System");
            Instructor instructor = instructorLogin();
            String option;
            printMainMenu(instructor);
            while (!(option = scan.next()).equals("quit")) {
                switch (option) {
                    case "1":
                        validateLocation();
                        validateCourses(instructor);
                        validateStudents();
                        logLabTime(instructor);
                        break;
                    case "2":
                        modifyCourses(instructor);
                        break;
                    case "3":
                        modifyLocationData();
                        break;
                    case "4":
                        modifyStudents();
                        break;
                    case "5":
                        printReport(instructor);
                        break;
                    case "6":
                        System.out.println("Logging out: " + instructor.getFirstName() + " " + instructor.getLastName());
                        instructor = instructorLogin();
                        break;
                    case "7":
                        quit();
                        break;
                    default:
                        System.out.println("Operation not recognized.");
                }
                printMainMenu(instructor);
            }
        } catch (Exception e) {
            System.out.println("Error in application " + e);
            quit();
        }
        //
        // if we get here, just quit
        quit();

    }

    private static void validateLocation() {
        if (LocationDAO.getLocationCount(session) == 0){
            System.out.println("There aren't any locations in the database, creating new one.");
            modifyLocationData();
        }
    }

    private static void printReport(Instructor instructor) {
        final List<LabEvent> eventsForInstructor = LabEventDAO.getEventsForInstructor(session, instructor);
        eventsForInstructor.forEach(System.out::println);
    }

    private static void validateStudents() {
        if (StudentDAO.getStudentCount(session) == 0) {
            System.out.println("There aren't any students in the database, creating new one.");
            modifyStudents();
        }
    }

    private static void validateCourses(Instructor instructor) {
        if (CourseDAO.getCourseCount(session) == 0) {              // we need courses in order to log lab time
            System.out.println("There aren't any courses in the database, creating new one.");
            modifyCourses(instructor);
        }
    }

    private static void modifyStudents() {
        System.out.println("Create new student: ");
        Student student = new Student();
        student.setFirstName(getInput("Enter first name", Scanner::next));
        student.setLastName(getInput("Enter last name", Scanner::next));
        session.save(student);
    }

    private static void modifyLocationData() {
        System.out.println("Create new location: ");
        Location location = new Location();
        location.setCampus(getInput("Enter Campus Name", Scanner::next));
        location.setBuilding(getInput("Enter Building Name", Scanner::next));
        location.setRoom(getInput("Enter Room number", Scanner::nextInt));
        session.save(location);
    }

    private static void modifyCourses(Instructor instructor) {
        if (LocationDAO.getLocationCount(session) > 0) {
            System.out.println("Select Location where this course will be taught");
            final List<Location> locations = LocationDAO.getLocations(session);
            locations.forEach(System.out::println);
            System.out.println("Enter location ID");
            Optional<Location> locationOptional = Optional.empty();
            do {
                int input = getInput(Scanner::nextInt);
                locationOptional = locations.stream()
                                                .filter(location -> location.getId()
                                                                                .equals(input))
                                                .findFirst();
                if (!locationOptional.isPresent()) { // location not present, let the user know they messed up
                    System.out.println("Location ID: " + input + " not found, select another.");
                }
            } while (!locationOptional.isPresent()); // if the location wasn't found, keep trying
            Course course = new Course();
            course.setInstructor(instructor);
            course.setLocation(locationOptional.get());
            course.setName(getInput("Enter course Name", Scanner::next));
            session.save(course);
        } else {
            System.out.println("There doesn't appear to be any location data");
            modifyLocationData();
        }
    }

    private static void logLabTime(Instructor instructor) {
        System.out.println("logging lab time");
        System.out.println("1) Log Arrival");
        System.out.println("2) Log Departure");
        final int command = getInput(Scanner::nextInt);
        switch (command){
            case 1:
                logArrival(instructor);
                break;
            case 2:
                logDeparture(instructor);
                break;
            default:
                break;
        }
    }

    private static void logDeparture(Instructor instructor) {
        final List<LabEvent> loggedInEvents = LabEventDAO.getLoggedInEvents(session, instructor);
        System.out.println("Which lab event would you like to enter a departure time for? (by ID)");
        loggedInEvents.forEach(System.out::println);
        Optional<LabEvent> labEventOptional = Optional.empty();
        do {
            int input = getInput(Scanner::nextInt);
            labEventOptional = loggedInEvents.stream()
                                    .filter(location -> location.getId()
                                                                .equals(input))
                                    .findFirst();
            if (!labEventOptional.isPresent()) { // location not present, let the user know they messed up
                System.out.println("LabEvent ID: " + input + " not found, select another.");
            }
        } while (!labEventOptional.isPresent()); // if the location wasn't found, keep trying
        final LabEvent labEvent = labEventOptional.orElseThrow(() -> new IllegalStateException("Error logging lab event"));
        labEvent.setLeave(new Date(System.currentTimeMillis()));
        session.beginTransaction();
//        session.update(labEvent);
        session.saveOrUpdate(labEvent);
        session.getTransaction().commit();
    }

    private static void logArrival(Instructor instructor) {
        LabEvent labEvent = new LabEvent();
        labEvent.setInstructor(instructor);
        labEvent.setEnter(new Date(System.currentTimeMillis()));
        final List<Course> courses = CourseDAO.getCourses(session);
        System.out.println("Select course(by ID):");
        courses.forEach(System.out::println);
        Optional<Course> courseOptional = Optional.empty();
        do {
            int input = getInput(Scanner::nextInt);
            courseOptional = courses.stream()
                                        .filter(location -> location.getId()
                                                                    .equals(input))
                                        .findFirst();
            if (!courseOptional.isPresent()) { // location not present, let the user know they messed up
                System.out.println("Course ID: " + input + " not found, select another.");
            }
        } while (!courseOptional.isPresent()); // if the location wasn't found, keep trying
        final Course course = courseOptional.orElseThrow(() -> new IllegalStateException("Error selecting Course"));
        labEvent.setCourse(course);
        final List<Student> students = StudentDAO.getStudents(session);
        students.forEach(System.out::println);
        System.out.println("Select student by ID:");
        Optional<Student> studentOptional = Optional.empty();
        do {
            int input = getInput(Scanner::nextInt);
            studentOptional = students.stream()
                                    .filter(location -> location.getId()
                                                                .equals(input))
                                    .findFirst();
            if (!studentOptional.isPresent()) { // location not present, let the user know they messed up
                System.out.println("Student ID: " + input + " not found, select another.");
            }
        } while (!studentOptional.isPresent()); // if the location wasn't found, keep trying
        final Student student = studentOptional.orElseThrow(() -> new IllegalStateException("Error selecting Student"));
        labEvent.setStudent(student);
        session.save(labEvent);
        System.out.println("Lab event saved:" + labEvent);
    }

    private static void quit() {
        System.out.println("Exiting application");
        scan.close();
        shutdown();
        System.exit(0);
    }

    private static void printMainMenu(Instructor instructor) {
        System.out.println("Welcome " + instructor.getFirstName() + " " + instructor.getLastName());
        System.out.println("Pick from the following menu items:");
        System.out.println("1) Log lab time for student");
        System.out.println("2) Add courses to your schedule");
        System.out.println("3) Edit campus location information");
        System.out.println("4) Modify Student data");
        System.out.println("5) Print Report");
        System.out.println("6) Log out");
        System.out.println("7) Quit application");
    }

    private static Instructor instructorLogin() {
        System.out.println("To start you must log in as an instructor");
        System.out.println("1) Create new instructor account " +
                "\n2) Login as existing instructor");
        Optional<Instructor> instructorOptional = Optional.empty();
        while (!instructorOptional.isPresent()) {
            String option = scan.nextLine();
            switch (option) {
                case "1":
                    instructorOptional = Optional.of(createInstructor());
                    break;
                case "2":
                    instructorOptional = Optional.of(login());
                    break;
                default:
                    System.out.println("Option not recognized.");
                    System.out.println("1) Create new instructor account " +
                                     "\n2) Login as existing instructor");
            }
        }
        return instructorOptional.orElseThrow(() -> new IllegalStateException("Instructor login failed.")); // we know the value is here because we broke from while loop
    }

    private static Instructor login() {
        Optional<Instructor> instructorOptional = Optional.empty();
        List<Instructor> instructors = session.createQuery("from Instructor")
                                                    .list();
        if (instructors.size() == 0) {
            System.out.println("No instructors present, you must create an Instructor account");
            createInstructor();
            //
            // we just created an instructor, update list of instructors
            instructors = session.createQuery("from Instructor")
                                 .list();
        }
        instructors.forEach(instructor -> System.out.println(instructor.getId() + " first: " + instructor.getFirstName() + " last:" + instructor.getLastName()));
        System.out.println("Select instructor by ID");
        do {
            int input = getInput(Scanner::nextInt);
            instructorOptional = instructors.stream()
                                            .filter(instructor -> instructor.getId()
                                                                            .equals(input))
                                            .findFirst();
            if (!instructorOptional.isPresent()) { // instructor not present, let the user know they messed up
                System.out.println("Instructor ID: " + input + " not found, select another.");
            }
        } while (!instructorOptional.isPresent()); // if the instructor wasn't found, keep trying
        // return the instructor, but for some crazy reason if the optional is empty, throw an exception
        return instructorOptional.orElseThrow(() -> new IllegalStateException("Instructor Login failed."));
    }

    public static <T> T getInput(Function<Scanner, T> function) {
        return function.apply(scan);
    }
    public static <T> T getInput(String message, Function<Scanner, T> function) {
        System.out.println(message);
        return function.apply(scan);
    }

    private static Instructor createInstructor() {
        final Instructor instructor = new Instructor();
        String[] nameParts;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Enter Instructor Name (first and last separated by space)");
            final String name = scanner.nextLine();
            nameParts = name.split(" ");
        } while (nameParts.length == 0);
        instructor.setFirstName(nameParts[0]);
        instructor.setLastName(nameParts[1]);

        transactionalConsumer(session -> session.save(instructor));
        System.out.println("Created: " + instructor);
        return instructor;
    }
}
