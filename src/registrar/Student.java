package registrar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A student at a school.
 */
@SuppressWarnings("WeakerAccess")
public class Student {
    final private String name;
    private List<Course> courses = new ArrayList<>();

    public Student(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    public String getName() {
        return name;
    }

    /**
     * Returns all courses this student is currently enrolled in.
     */
    public List<Course> getCourses() {
        return new ArrayList<>(courses); // We're returning the runtime-type Array List beacause its an implementation of the List interface
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
