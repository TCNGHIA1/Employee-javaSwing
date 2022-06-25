package pd05529.poly.models;

import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 84339
 */
public class EmployeeModel implements Serializable{
    private String ID;
    private String nameString;
    private int age;
    private String emaiString;
    private double salary;

    public EmployeeModel() {
    }

    public EmployeeModel(String ID, String nameString, int age, String emaiString, double salary) {
        this.ID = ID;
        this.nameString = nameString;
        this.age = age;
        this.emaiString = emaiString;
        this.salary = salary;
    }

    public EmployeeModel(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNameString() {
        return nameString;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmaiString() {
        return emaiString;
    }

    public void setEmaiString(String emaiString) {
        this.emaiString = emaiString;
    }

    public double getSalary() {
        return  salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        
        return getID()+" " + getNameString() +" "+getAge()+" "+getEmaiString()+" "+getSalary();
    }
    
    
    
}
