package com.elearning.model;

import java.util.Comparator;

public class CourseByTeacherComparator implements Comparator<Course> {
    @Override
    public int compare(Course a, Course b) {
        return a.getProfesorTitular().getNume().compareTo(b.getProfesorTitular().getNume());
    }
}
