/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pd05529.poly.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;

/**
 *
 * @author 84339
 */
public class ClockThread implements Runnable {

    private JLabel JLabel;

    public ClockThread(JLabel jLabel) {
        this.JLabel = jLabel;
    }

    @Override
    public void run() {
        while (true) {
            Date now = new Date();
                SimpleDateFormat formater = new SimpleDateFormat();
                formater.applyPattern("hh:mm:ss aa");
                
                String time = formater.format(now);
                JLabel.setText(time);
            try {              
                Thread.sleep(1000);
            } catch (Exception e) {

            }
        }
    }

}
