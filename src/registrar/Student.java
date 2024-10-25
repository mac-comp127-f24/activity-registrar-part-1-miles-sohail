package registrar;


import java.util.Collections;
import java.util.HashSet;

import java.util.Objects;
import java.util.Set;

/**
 * A student at a school.
 */
@SuppressWarnings("WeakerAccess")
public class Student {
    final private String name;
    private Set<Course> courses = new HashSet<>();

    public Student(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    public String getName() {
        return name;
    }

    /**
     * Returns all courses this student is currently enrolled in.
     */
    public Set<Course> getCourses() {
        
        return Collections.unmodifiableSet(courses); // We're returning the runtime-type Array List beacause its an implementation of the List interface
    }

    /**
     * Add this student to the given course's roster.
     * Has no effect if the student is already registered.
     * *
     * @return True if the student was enrolled, false if not.
     */
    public boolean enrollIn(Course course) {
        boolean success = course.enroll(this);
        if(success) {
            courses.add(course);
        }
        return success;
    }

    @Override
    public String toString() {
        return name;
    }
}
