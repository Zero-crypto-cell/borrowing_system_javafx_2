package com.csslab.model;

public class BorrowRecord {
    private final String recordId;
    private final String studentId;
    private final String studentName;
    private final String section;
    private final Equipment equipment;
    private final String dateBorrowed;
    private String status;

    public BorrowRecord(String recordId, String studentId, String studentName, String section,
                        Equipment equipment, String dateBorrowed, String status) {
        this.recordId = recordId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.section = section;
        this.equipment = equipment;
        this.dateBorrowed = dateBorrowed;
        this.status = status;
    }

    public String getRecordId() { return recordId; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getSection() { return section; }
    public Equipment getEquipment() { return equipment; }
    public String getDateBorrowed() { return dateBorrowed; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}