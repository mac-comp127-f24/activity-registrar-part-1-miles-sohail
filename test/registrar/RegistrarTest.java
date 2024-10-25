package registrar;


import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("WeakerAccess")
class RegistrarTest {
    // ------ Setup ------

    private TestObjectFactory School = new TestObjectFactory();
    private Course comp127, math6, basketWeaving101;
    private Student sally, fred, zongo;

    @BeforeEach
    public void createStudents() {
        sally = School.makeStudent("Sally");
        fred = School.makeStudent("Fred");
        zongo = School.makeStudent("Zongo Jr.");
    }

    @BeforeEach
    public void createCourses() {
        comp127 = School.makeCourse("COMP 127", "Software Fun Fun");
        comp127.setEnrollmentLimit(16);

        math6 = School.makeCourse("Math 6", "All About the Number Six");
        basketWeaving101 = School.makeCourse("Underwater Basket Weaving 101", "Senior spring semester!");
    }

    // ------ Enrolling ------

    @Test
    void studentStartsInNoCourses() {
        assertEquals(Set.of(), sally.getCourses());
    }

    @Test
    void studentCanEnroll() {
        sally.enrollIn(comp127);
        assertEquals(Set.of(comp127), sally.getCourses());
        assertEquals(Set.of(sally), comp127.getRoster());
    }

    // ------ Enrollment limits ------

    @Test
    void coursesHaveEnrollmentLimits() {
        comp127.setEnrollmentLimit(16);
        assertEquals(16, comp127.getEnrollmentLimit());
    }

    @Test
    void enrollmentLimitDefaultsToUnlimited() {
        School.enrollMultipleStudents(math6, 1000);
        assertEquals(1000, math6.getRoster().size());
    }

    @Test
    void enrollingUpToLimitAllowed() {
        School.enrollMultipleStudents(comp127, 15);
        assertTrue(sally.enrollIn(comp127));
        assertTrue(comp127.getRoster().contains(sally));
    }

    @Test
    void cannotEnrollPastLimit() {
        School.enrollMultipleStudents(comp127, 16);
        assertFalse(sally.enrollIn(comp127));
        assertFalse(comp127.getRoster().contains(sally));
    }

    // ------ Post-test invariant check ------
    //
    // This is a bit persnickety for day-to-day testing, but these kinds of checks are appropriate
    // for security sensitive or otherwise mission-critical code. Some people even add them as
    // runtime checks in the code, instead of writing them as tests.

    @AfterEach
    public void checkInvariants() {
        for (Student s : School.allStudents())
            checkStudentInvariants(s);
        for (Course c : School.allCourses())
            checkCourseInvariants(c);
    }

    private void checkStudentInvariants(Student s) {
        for (Course c : s.getCourses())
            assertTrue(
                c.getRoster().contains(s),
                s + " thinks they are enrolled in " + c
                    + ", but " + c + " does not have them in the Set of students");
    }

    private void checkCourseInvariants(Course c) {
        for (Student s : c.getRoster()) {
            assertTrue(
                s.getCourses().contains(c),
                c + " thinks " + s + " is enrolled, but " + s + " doesn't think they're in the class");
        }

        assertTrue(
            c.getRoster().size() <= c.getEnrollmentLimit(),
            c + " has an enrollment limit of " + c.getEnrollmentLimit()
                + ", but has " + c.getRoster().size() + " students");
    }
    @Test
    void clientsCannotModifyCourses() {
       Set<Course> courses = sally.getCourses();
       assertThrows(UnsupportedOperationException.class, () -> {
        courses.add(comp127);
     });
    }

    @Test
    void clientsCannotModifyRosters() {
       Set<Student> student = comp127.getRoster();
       assertThrows(UnsupportedOperationException.class, () -> {
        student.add(sally);
     });
    }
    @Test
    void clientsCannotAddMultiple(){
        sally.enrollIn(comp127);
        sally.enrollIn(comp127);
        sally.enrollIn(comp127);
        Set<Student> student = comp127.getRoster();
        Set<Course> courses = sally.getCourses();
        assertEquals(1, student.size());
        assertEquals(1, courses.size());
    }
     
}
