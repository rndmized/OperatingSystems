package ie.gmit.sw.gui;

import javax.swing.*;

public class InputFrame extends javax.swing.JFrame{

	private static final long serialVersionUID = 1L;
	private JPanel jPanel1;
	private JPanel jPanel2;

	public InputFrame() {
        
		this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        //Creating First Panel
        jPanel1 = new javax.swing.JPanel();
        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        
        //Panel 2
        jPanel2 = new javax.swing.JPanel();
        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Process Input"));
       
    }
}
