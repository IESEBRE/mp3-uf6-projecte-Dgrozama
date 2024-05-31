package org.example.view;

import org.example.model.entities.Usuari;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ModelComponentsVisuals {

    private DefaultTableModel model;
    private DefaultTableModel modelPais;
    private ComboBoxModel<Usuari.Pais.paisos> comboBoxModel;

    //Getters


    public ComboBoxModel<Usuari.Pais.paisos> getComboBoxModel() {
        return comboBoxModel;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public DefaultTableModel getModelPais() {
        return modelPais;
    }

    public ModelComponentsVisuals() {


        //Anem a definir l'estructura de la taula dels alumnes
        model =new DefaultTableModel(new Object[]{"Usuari","Seguidors","Verificat","Object"},0){
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }



            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Integer.class;
                    case 2:
                        return Boolean.class;
                    default:
                        return Object.class;
                }
            }
        };




        //Anem a definir l'estructura de la taula de les matrícules
        modelPais =new DefaultTableModel(new Object[]{"País","Top Local"},0){
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }

            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Usuari.Pais.paisos.class;
                    case 1:
                        return Integer.class;
                    default:
                        return Object.class;
                }
            }
        };



        //Estructura del comboBox
        comboBoxModel=new DefaultComboBoxModel<>(Usuari.Pais.paisos.values());



    }
}
