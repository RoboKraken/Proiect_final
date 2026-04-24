package com.elearning.model;

import java.util.Comparator;

public class CourseByPriceComparator implements Comparator<Course> {
    @Override
    public int compare(Course a, Course b) {
        return Double.compare(a.getPret(), b.getPret());
    }
}
