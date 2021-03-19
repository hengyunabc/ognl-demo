package com.example.ognl;

/**
 * 
 * @author hengyunabc 2021-03-19
 *
 */
public class TestService {

    public Student test(int i, String str, Student student) {
        if (System.getProperty("exceptionCase") != null) {
            throw new IllegalArgumentException("error");
        }
        student.setId(i);
        student.setName(str);
        return student;
    }

}
