package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VistaPais extends JFrame{
    private JTabbedPane pestanyes;
    private JTable taula;
    private JScrollPane scrollPane1;
    private JButton insertarButton;
    private JButton modificarButton;
    private JButton borrarButton;
    private JTextField campUsuari;
    private JTextField campSeguidors;
    private JCheckBox caixaVerificat;
    private JPanel panel;
    private JTable taulaPais;
    private JComboBox comboPaisos;
    private JTextField campTop;
    //private JTabbedPane PanelPestanya;

    //Getters


    public JTable getTaulaPais() {
        return taulaPais;
    }

    public JComboBox getComboPaisos() {
        return comboPaisos;
    }

    public JTextField getCampTop() {
        return campTop;
    }

    public JTabbedPane getPestanyes() {
        return pestanyes;
    }

    public JTable getTaula() {
        return taula;
    }

    public JButton getBorrarButton() {
        return borrarButton;
    }

    public JButton getModificarButton() {
        return modificarButton;
    }

    public JButton getInsertarButton() {
        return insertarButton;
    }

    public JTextField getCampUsuari() {
        return campUsuari;
    }

    public JTextField getCampSeguidors() {
        return campSeguidors;
    }

    public JCheckBox getCaixaVerificat() {
        return caixaVerificat;
    }


    //Constructor de la classe
    public VistaPais() {


        //Per poder vore la finestra
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(false);
    }

        private void createUIComponents() {
        // TODO: place custom component creation code here
        scrollPane1 = new JScrollPane();
        taula = new JTable();
        pestanyes = new JTabbedPane();
        taula.setModel(new DefaultTableModel());
        taula.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane1.setViewportView(taula);

    }
}
