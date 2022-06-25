package pd05529.poly.controller;

import java.io.IOException;
import pd05529.poly.models.EmployeeModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;

import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author 84339
 */
public class EmployeeList{

    public ArrayList<EmployeeModel> list = new ArrayList<>();
    public int currentIndex = -1;
    private String PATH = "D:\\QLNV1.txt";

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }


    public void writeToFile() throws IOException{
       XFile.writeObject(getPATH(),list);
    }

    public void loadtoFile() throws ClassNotFoundException, IOException{
        list = (ArrayList<EmployeeModel>) XFile.readObject(getPATH());
    }
    
    public String record() {
        return "Record: " + (getCurrentIndex() + 1) + " to " + list.size();
    }
    public void setCurrentIndex(int index){
        this.currentIndex = index;
    }
    public void first() { 
            currentIndex = 0;
    }

    public void last() {
        currentIndex = list.size() - 1;
    }

    public void prev() {
        if (currentIndex > 0) {
            currentIndex--;
        } else{
        currentIndex = list.size() - 1;
    }
    }

    public void next() {
        if (currentIndex < list.size() - 1) {
            currentIndex++;
        } else{
            currentIndex=0;
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public EmployeeModel getEmployeeModel() {
        if (list.size()==0) {
            return null;
        }
        return list.get(currentIndex);
    }

    public void add(EmployeeModel emp) {
        list.add(emp);
    }

    public EmployeeModel findEmployee(String empID) {
        for (EmployeeModel emp : list) {
            if (emp.getID().equalsIgnoreCase(empID)) {
                return emp;
            }
        }
        return null;
    }
    public int find(String empID){
       for(int i =0;i<list.size();i++){
           if(list.get(i).getID().equalsIgnoreCase(empID)){
               return i;
           }
       }
        return -1;
    }
    public boolean deleteEmployee(String empID) {
        for (EmployeeModel emp : list) {
            if (emp.getID().equalsIgnoreCase(empID)) {
                list.remove(emp);
                return true;
            }
        }
        return false;
    }

    public boolean updateEmployee(EmployeeModel emp) {
        EmployeeModel emp_up = findEmployee(emp.getID());
        boolean flag = false;
        if (emp_up != null) {
            emp_up.setNameString(emp.getNameString());
            emp_up.setAge(emp.getAge());
            emp_up.setEmaiString(emp.getEmaiString());
            emp_up.setSalary(emp.getSalary());
            flag = true;
        }
        return flag;
    }

    public void addtoTable(DefaultTableModel tblModel) {
        tblModel.setRowCount(0);
        for (EmployeeModel emp : list) {
            Formatter format = new Formatter();
            format.format("%.1f", emp.getSalary());
            Object[] row = new Object[]{
                emp.getID(), emp.getNameString(), emp.getAge(), emp.getEmaiString(), format
            };
            tblModel.addRow(row);
        }
        tblModel.fireTableDataChanged();
    }
//Cac ham sap xep
    public void sortID(){
        Collections.sort(list,(a,b)->a.getID().toLowerCase().compareTo(b.getID().toLowerCase()));
    }
public void sortAge(){
        Collections.sort(list,(a,b)-> a.getAge()-b.getAge());
    }
public void sortSalary(){
        Collections.sort(list,(a,b)->(int) (a.getSalary()-b.getSalary()));
    }
public void sortName(){
        Collections.sort(list,(a,b)->a.getNameString().toLowerCase().compareTo(b.getNameString().toLowerCase()));
    }
}
