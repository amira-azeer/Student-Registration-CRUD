package com.example.studentregistrationui;

public class TableRecords {

    String studentID, studentName, studentNumber, studentCourse;

    public TableRecords(String studentID, String studentName, String studentNumber, String studentCourse) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.studentCourse = studentCourse;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(String studentCourse) {
        this.studentCourse = studentCourse;
    }
}