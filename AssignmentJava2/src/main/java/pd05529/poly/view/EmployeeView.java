/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pd05529.poly.view;

import pd05529.poly.models.EmployeeModel;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pd05529.poly.controller.ClockThread;
import pd05529.poly.controller.EmployeeList;

/**
 *
 * @author 84339
 */
public class EmployeeView extends javax.swing.JFrame {

    EmployeeList empList = new EmployeeList();
    DefaultTableModel model = new DefaultTableModel();
    final String FILE_NAME = "D:\\QLNV1.txt";
    int index = empList.getCurrentIndex();

    /**
     * Creates new form EmployeeView
     */
    public EmployeeView() {
        initComponents();
        setLocationRelativeTo(null);
//        addList();
        initColumnTable();
        timeThread();
        setCloseWindow();
    }

    private void timeThread() {
        Thread t1 = new Thread(new ClockThread(jLabel_time));
        t1.start();
    }

    private void close() throws HeadlessException {
        int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn lưu dữ liệu Không?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
        switch (choice) {
            case JOptionPane.YES_OPTION:
                JOptionPane.showMessageDialog(null, "Dữ liệu đã được lưu");
                try {
                    empList.writeToFile();
                    
                } catch (IOException ex) {
                    Logger.getLogger(EmployeeView.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(0);
            case JOptionPane.NO_OPTION:
                System.exit(0);
            default:
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                break;
        }
    }

    private void setCloseWindow() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }

    private void clickNew() {
        // TODO add your handling code here:
        txtID.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtEmail.setText("");
        txtSalary.setText("");
        txtID.setBackground(Color.white);
        txtName.setBackground(Color.white);
        txtAge.setBackground(Color.white);
        txtEmail.setBackground(Color.white);
        txtSalary.setBackground(Color.white);
    }
//Luu va sua nhan vien

    private void clickSave() {
        StringBuilder sb = new StringBuilder();
        ValidateID(sb);
        ValidateName(sb);
        ValidateAge(sb);
        validateSalary(sb);
        validateEmail(sb);
        if (sb.length() > 0) {
            JOptionPane.showMessageDialog(this, sb, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        EmployeeModel emp = new EmployeeModel();
        emp.setID(txtID.getText());
        emp.setNameString(txtName.getText());
        emp.setAge(Integer.parseInt(txtAge.getText()));
        emp.setEmaiString(txtEmail.getText());
        emp.setSalary(Double.parseDouble(txtSalary.getText()));

        if (empList.findEmployee(txtID.getText()) == null) {
            empList.add(emp);
            JOptionPane.showMessageDialog(this, "Thêm thành công! ", "Confirm", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn muốn sửa nhân viên?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                empList.updateEmployee(emp);
                JOptionPane.showMessageDialog(this, "Bạn đã sửa thành công!", "Confirm", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        clickNew();
        empList.addtoTable(model);
    }
//Nut tim kiem

    private void clickFind() {
        if (empList.findEmployee(txtID.getText()) == null) {
            JOptionPane.showMessageDialog(this, "Không có nhân viên mã " + txtID.getText());
        } else {
            EmployeeModel emp = empList.findEmployee(txtID.getText());
            initEmployee(emp);
        }
    }
//Nut delete

    private void clickDelete() {
        StringBuilder sb = new StringBuilder();
        ValidateID(sb);
        if (sb.length() > 0) {
            JOptionPane.showMessageDialog(this, sb, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        txtID.setBackground(Color.white);
        if (empList.findEmployee(txtID.getText()) == null) {
            JOptionPane.showMessageDialog(this, "Mã " + txtID.getText() + " không tồn tại!!");
        } else {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn thực sự muốn xóa nhân viên?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION) {
                return;
            } else {
                empList.deleteEmployee(txtID.getText());
                JOptionPane.showMessageDialog(this, "Bạn đã xóa thành công!", "Confirm", JOptionPane.INFORMATION_MESSAGE);
            }

        }
        empList.addtoTable(model);
        clickNew();
    }
//Nut Open

    private void clickOpenFile() {
        try {
            empList.loadtoFile();
            jLabel_record.setText(empList.record());
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        empList.addtoTable(model);
    }
//Click len bang

    private void clickRowTable() {

        index = jTable_Employee.getSelectedRow();
        empList.setCurrentIndex(index);
        try {
            txtID.setText(model.getValueAt(index, 0).toString());
            txtName.setText(model.getValueAt(index, 1).toString());
            txtAge.setText(model.getValueAt(index, 2).toString());
            txtEmail.setText(model.getValueAt(index, 3).toString());
            txtSalary.setText(model.getValueAt(index, 4).toString());
            jLabel_record.setText(empList.record());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    //Nut |<
    private void clickFirst() {
        empList.first();
        EmployeeModel emp = empList.getEmployeeModel();
        initEmployee(emp);
    }
//Nut <<

    private void clickPrev() {
        empList.prev();
        EmployeeModel emp = empList.getEmployeeModel();
        initEmployee(emp);
    }
//Nut >>

    private void clickNext() {
        empList.next();
        EmployeeModel emp = empList.getEmployeeModel();
        initEmployee(emp);
    }
//Nut >|

    private void clickLast() {
        empList.last();
        EmployeeModel emp = empList.getEmployeeModel();
        initEmployee(emp);
    }
//----- Cac nut sap xep ------------

    private void clickSortID() {
        empList.sortID();
        empList.addtoTable(model);
    }

    private void clickSortSalary() {
        empList.sortSalary();
        empList.addtoTable(model);
    }

    private void clickSortAge() {
        empList.sortAge();
        empList.addtoTable(model);
    }

    private void clickSortName() {
        empList.sortName();
        empList.addtoTable(model);
    }
//---------------------
//Validate 

    private void validateEmail(StringBuilder sb) {
        if (txtEmail.getText().equals("")) {
            txtEmail.setBackground(Color.yellow);
            sb.append("Không để trống EMAIL\n");
        } else {
            txtEmail.setBackground(Color.white);
            String regex = "\\S+@\\S+\\.\\S+";
            Pattern pattern = Pattern.compile(regex);
            String value = txtEmail.getText();
            Matcher matcher = pattern.matcher(value);
            if (!matcher.matches()) {
                sb.append("EMAIL sai định dạng!\n ");
                txtEmail.setBackground(Color.yellow);
            }
        }
    }

    private void validateSalary(StringBuilder sb) {
        if (txtSalary.getText().equals("")) {
            sb.append("Không để trống LƯƠNG NHÂN VIÊN! \n");
            txtSalary.setBackground(Color.yellow);
        } else {
            txtSalary.setBackground(Color.white);
            try {
                double salary = Double.parseDouble(txtSalary.getText());
                if (salary < 5000000) {
                    sb.append("LƯƠNG phải trên 5000000! \n");
                    txtSalary.setBackground(Color.yellow);
                }
            } catch (Exception e) {
                txtSalary.setBackground(Color.yellow);
                sb.append("LƯƠNG không hợp lệ!\n ");
            }
        }
    }

    private void ValidateAge(StringBuilder sb) {
        if (txtAge.getText().equals("")) {
            sb.append("Không để trống TUỔI NHÂN VIÊN! \n");
            txtAge.setBackground(Color.yellow);
        } else {
            txtAge.setBackground(Color.white);
            try {
                int age = Integer.parseInt(txtAge.getText());
                if (age < 16 || age > 55) {
                    sb.append("TUỔI từ 16 đến 55! \n");
                    txtAge.setBackground(Color.yellow);
                }
            } catch (Exception e) {
                txtAge.setBackground(Color.yellow);
                sb.append("TUỔI không hợp lệ! \n");
            }
        }
    }

    private void ValidateID(StringBuilder sb) {
        if (txtID.getText().equals("")) {
            sb.append("Không để trống MÃ NHÂN VIÊN! \n");
            txtID.setBackground(Color.yellow);
        } else {
            txtID.setBackground(Color.white);
        }

    }

    private void ValidateName(StringBuilder sb) {
        if (txtName.getText().equals("")) {
            sb.append("Không để trống TÊN NHÂN VIÊN! \n");
            txtName.setBackground(Color.yellow);
        } else {
            txtName.setBackground(Color.white);
        }
    }

//-----------Do du lieu nhan vien len cac dong------------------
    private void initEmployee(EmployeeModel emp) {
        txtID.setText(emp.getID());
        txtName.setText(emp.getNameString());
        txtAge.setText(emp.getAge() + "");
        txtEmail.setText(emp.getEmaiString());
        txtSalary.setText(emp.getSalary() + "");
        jLabel_record.setText(empList.record());
    }

    //Thay đổi tên cot table
    private void initColumnTable() {
        model.setColumnIdentifiers(new Object[]{"MÃ", "HỌ VÀ TÊN", "TUỔI", "EMAIL", "LƯƠNG"});
        jTable_Employee.setModel(model);
    }

//----------------------------------------
    public void addList() {
        empList.add(new EmployeeModel("Pd05529", "Nghia", 23, "nghiatcpd05529@fpt.edu.vn", 7000000));
        empList.add(new EmployeeModel("pd0001", "An", 18, "An@fpt.edu.vn", 5000000));
        empList.add(new EmployeeModel("pd0871", "Trung", 23, "Trung@com.vn", 10000000));
        empList.add(new EmployeeModel("Pd04329", "Chinh", 23, "chinh123@edu.vn", 9500000));
        empList.add(new EmployeeModel("pd10234", "Hong", 23, "pd10234@com.bao.vn", 6000000));
        empList.add(new EmployeeModel("pd23012", "My", 23, "Myaaa@fpt.vn", 4000000));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_North = new javax.swing.JPanel();
        jLabel_text = new javax.swing.JLabel();
        jLabel_time = new javax.swing.JLabel();
        jPanel_form = new javax.swing.JPanel();
        jLabel_salary = new javax.swing.JLabel();
        jLabel_name = new javax.swing.JLabel();
        jLabel_email = new javax.swing.JLabel();
        jLabel_age = new javax.swing.JLabel();
        jLabel_id = new javax.swing.JLabel();
        jButton_first = new javax.swing.JButton();
        jButton_prev = new javax.swing.JButton();
        jButton_next = new javax.swing.JButton();
        jButton_last = new javax.swing.JButton();
        jLabel_record = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        txtAge = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtSalary = new javax.swing.JTextField();
        jPanel_btn = new javax.swing.JPanel();
        jButton_new = new javax.swing.JButton();
        jButton_save = new javax.swing.JButton();
        jButton_delete = new javax.swing.JButton();
        jButton_find = new javax.swing.JButton();
        jButton_open = new javax.swing.JButton();
        jButton_exit = new javax.swing.JButton();
        jScrollPane_table = new javax.swing.JScrollPane();
        jTable_Employee = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnSortID = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnSortSalary = new javax.swing.JButton();
        btnSortAge = new javax.swing.JButton();
        btnSortName = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Quan ly ");
        setMinimumSize(new java.awt.Dimension(550, 550));
        setName("frameMenu"); // NOI18N

        jLabel_text.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel_text.setForeground(new java.awt.Color(51, 51, 255));
        jLabel_text.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_text.setText("QUẢN LÝ NHÂN VIÊN");

        jLabel_time.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_time.setForeground(new java.awt.Color(255, 0, 0));
        jLabel_time.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_time.setText("00:00 AM");
        jLabel_time.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabel_timeAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel_NorthLayout = new javax.swing.GroupLayout(jPanel_North);
        jPanel_North.setLayout(jPanel_NorthLayout);
        jPanel_NorthLayout.setHorizontalGroup(
            jPanel_NorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_NorthLayout.createSequentialGroup()
                .addGap(421, 421, 421)
                .addComponent(jLabel_time, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                .addGap(63, 63, 63))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_NorthLayout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(jLabel_text, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(157, 157, 157))
        );
        jPanel_NorthLayout.setVerticalGroup(
            jPanel_NorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_NorthLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_text)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_time)
                .addContainerGap())
        );

        jPanel_form.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, java.awt.Color.lightGray));

        jLabel_salary.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_salary.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel_salary.setText("LƯƠNG");

        jLabel_name.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_name.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel_name.setText("HỌ VÀ TÊN");

        jLabel_email.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_email.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel_email.setText("EMAIL");

        jLabel_age.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_age.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel_age.setText("TUỔI");

        jLabel_id.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_id.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel_id.setText("MÃ NHÂN VIÊN");
        jLabel_id.setMaximumSize(new java.awt.Dimension(32, 20));
        jLabel_id.setMinimumSize(new java.awt.Dimension(32, 20));
        jLabel_id.setPreferredSize(new java.awt.Dimension(32, 20));

        jButton_first.setText("|<");
        jButton_first.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_firstActionPerformed(evt);
            }
        });

        jButton_prev.setText("<<");
        jButton_prev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_prevActionPerformed(evt);
            }
        });

        jButton_next.setText(">>");
        jButton_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_nextActionPerformed(evt);
            }
        });

        jButton_last.setText(">|");
        jButton_last.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_lastActionPerformed(evt);
            }
        });

        jLabel_record.setForeground(new java.awt.Color(255, 0, 51));
        jLabel_record.setText("Record: 0 to 0");

        txtID.setPreferredSize(new java.awt.Dimension(71, 30));

        txtName.setPreferredSize(new java.awt.Dimension(71, 30));

        txtAge.setPreferredSize(new java.awt.Dimension(71, 30));

        txtEmail.setPreferredSize(new java.awt.Dimension(71, 30));

        txtSalary.setPreferredSize(new java.awt.Dimension(71, 30));

        javax.swing.GroupLayout jPanel_formLayout = new javax.swing.GroupLayout(jPanel_form);
        jPanel_form.setLayout(jPanel_formLayout);
        jPanel_formLayout.setHorizontalGroup(
            jPanel_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_formLayout.createSequentialGroup()
                .addGroup(jPanel_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_formLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jButton_first, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_prev, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_next, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_last, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_record))
                    .addGroup(jPanel_formLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel_id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_name, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel_salary, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_email, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_age, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAge, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel_formLayout.setVerticalGroup(
            jPanel_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_formLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_age, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_salary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_first)
                    .addComponent(jButton_prev)
                    .addComponent(jButton_next)
                    .addComponent(jButton_last)
                    .addComponent(jLabel_record))
                .addContainerGap())
        );

        jPanel_btn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel_btn.setPreferredSize(new java.awt.Dimension(90, 350));

        jButton_new.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_new.setText("New");
        jButton_new.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_newActionPerformed(evt);
            }
        });

        jButton_save.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_save.setText("Save");
        jButton_save.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_saveActionPerformed(evt);
            }
        });

        jButton_delete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_delete.setText("Delete");
        jButton_delete.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_deleteActionPerformed(evt);
            }
        });

        jButton_find.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_find.setText("Find");
        jButton_find.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton_find.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_findActionPerformed(evt);
            }
        });

        jButton_open.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_open.setText("Open");
        jButton_open.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton_open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_openActionPerformed(evt);
            }
        });

        jButton_exit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_exit.setText("Exit");
        jButton_exit.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_exitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_btnLayout = new javax.swing.GroupLayout(jPanel_btn);
        jPanel_btn.setLayout(jPanel_btnLayout);
        jPanel_btnLayout.setHorizontalGroup(
            jPanel_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_btnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_new, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_find, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_exit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_open, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_btnLayout.setVerticalGroup(
            jPanel_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_btnLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jButton_new, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton_save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton_delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton_find, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton_open, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton_exit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable_Employee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable_Employee.setSurrendersFocusOnKeystroke(true);
        jTable_Employee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_EmployeeMouseClicked(evt);
            }
        });
        jScrollPane_table.setViewportView(jTable_Employee);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnSortID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSortID.setText("Mã");
        btnSortID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortIDActionPerformed(evt);
            }
        });

        jLabel1.setText("Sắp xếp");

        btnSortSalary.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSortSalary.setText("Lương");
        btnSortSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortSalaryActionPerformed(evt);
            }
        });

        btnSortAge.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSortAge.setText("Tuổi");
        btnSortAge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortAgeActionPerformed(evt);
            }
        });

        btnSortName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSortName.setText("Tên");
        btnSortName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnSortID, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnSortSalary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSortAge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSortName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSortID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSortName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSortAge)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSortSalary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_North, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_form, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane_table))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel_btn, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel_North, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel_form, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_btn, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane_table, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel_btn.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_newActionPerformed
        clickNew();
    }//GEN-LAST:event_jButton_newActionPerformed


    private void jButton_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_saveActionPerformed
        clickSave();
    }//GEN-LAST:event_jButton_saveActionPerformed


    private void jButton_openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_openActionPerformed
        clickOpenFile();
    }//GEN-LAST:event_jButton_openActionPerformed

    private void jTable_EmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_EmployeeMouseClicked
        // TODO add your handling code here:
        clickRowTable();
    }//GEN-LAST:event_jTable_EmployeeMouseClicked

    private void jButton_findActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_findActionPerformed
        clickFind();
    }//GEN-LAST:event_jButton_findActionPerformed


    private void jButton_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_deleteActionPerformed
        // TODO add your handling code here:
        clickDelete();
    }//GEN-LAST:event_jButton_deleteActionPerformed

    private void jButton_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_exitActionPerformed
        // TODO add your handling code here:
        close();
    }//GEN-LAST:event_jButton_exitActionPerformed

    private void jButton_firstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_firstActionPerformed
        // TODO add your handling code here:
        clickFirst();
    }//GEN-LAST:event_jButton_firstActionPerformed

    private void jButton_lastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_lastActionPerformed
// TODO add your handling code here:
        clickLast();
    }//GEN-LAST:event_jButton_lastActionPerformed

    private void jButton_prevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_prevActionPerformed
        // TODO add your handling code here:
        clickPrev();
    }//GEN-LAST:event_jButton_prevActionPerformed

    private void jButton_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_nextActionPerformed
        // TODO add your handling code here:

        clickNext();
    }//GEN-LAST:event_jButton_nextActionPerformed

    private void jLabel_timeAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel_timeAncestorAdded

    }//GEN-LAST:event_jLabel_timeAncestorAdded

    private void btnSortIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortIDActionPerformed
        clickSortID();
    }//GEN-LAST:event_btnSortIDActionPerformed

    private void btnSortSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortSalaryActionPerformed
        // TODO add your handling code here:
        clickSortSalary();
    }//GEN-LAST:event_btnSortSalaryActionPerformed

    private void btnSortAgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortAgeActionPerformed
        // TODO add your handling code here:
        clickSortAge();
    }//GEN-LAST:event_btnSortAgeActionPerformed

    private void btnSortNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortNameActionPerformed
        // TODO add your handling code here:
        clickSortName();
    }//GEN-LAST:event_btnSortNameActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EmployeeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new EmployeeView().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton btnSortAge;
    javax.swing.JButton btnSortID;
    javax.swing.JButton btnSortName;
    javax.swing.JButton btnSortSalary;
    javax.swing.JButton jButton_delete;
    javax.swing.JButton jButton_exit;
    javax.swing.JButton jButton_find;
    javax.swing.JButton jButton_first;
    javax.swing.JButton jButton_last;
    javax.swing.JButton jButton_new;
    javax.swing.JButton jButton_next;
    javax.swing.JButton jButton_open;
    javax.swing.JButton jButton_prev;
    javax.swing.JButton jButton_save;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel jLabel_age;
    javax.swing.JLabel jLabel_email;
    javax.swing.JLabel jLabel_id;
    javax.swing.JLabel jLabel_name;
    javax.swing.JLabel jLabel_record;
    javax.swing.JLabel jLabel_salary;
    javax.swing.JLabel jLabel_text;
    javax.swing.JLabel jLabel_time;
    javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel_North;
    javax.swing.JPanel jPanel_btn;
    javax.swing.JPanel jPanel_form;
    javax.swing.JScrollPane jScrollPane_table;
    javax.swing.JTable jTable_Employee;
    javax.swing.JTextField txtAge;
    javax.swing.JTextField txtEmail;
    javax.swing.JTextField txtID;
    javax.swing.JTextField txtName;
    javax.swing.JTextField txtSalary;
    // End of variables declaration//GEN-END:variables

}
