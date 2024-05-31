package org.example.controller;

import org.example.model.entities.Usuari;
import org.example.model.exceptions.DAOException;
import org.example.model.entities.Usuari.Pais;
import org.example.view.ModelComponentsVisuals;
import org.example.model.impls.UsuariDAOJDBCOracleImpl;
import org.example.view.VistaPais;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements PropertyChangeListener { //1. Implementació de interfície PropertyChangeListener


    private ModelComponentsVisuals modelComponentsVisuals =new ModelComponentsVisuals();
    private UsuariDAOJDBCOracleImpl dadesUsuari;
    private VistaPais view;

    public Controller(UsuariDAOJDBCOracleImpl dadesUsuari, VistaPais view) {



        this.dadesUsuari = dadesUsuari;
        this.view = view;

        try {
            this.dadesUsuari.crearTaulaBD();
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(null, "No s'ha pogut connectar a la base de dades", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(99);
        }

        //5. Necessari per a que Controller reaccione davant de canvis a les propietats lligades
        canvis.addPropertyChangeListener(this);

        lligaVistaModel();

        afegirListeners();

        //Si no hem tingut cap poroblema amb la BD, mostrem la finestra
        view.setVisible(true);

    }

    private void lligaVistaModel() {

        //Carreguem la taula d'alumnes en les dades de la BD
        try {
            setModelTaulaAlumne(modelComponentsVisuals.getModel(), dadesUsuari.getAll());
        } catch (DAOException e) {
            this.setExcepcio(e);
        }

            //Fixem el model de la taula dels alumnes
        JTable taula = view.getTaula();
        taula.setModel(this.modelComponentsVisuals.getModel());
        //Amago la columna que conté l'objecte alumne
        taula.getColumnModel().getColumn(3).setMinWidth(0);
        taula.getColumnModel().getColumn(3).setMaxWidth(0);
        taula.getColumnModel().getColumn(3).setPreferredWidth(0);

        //Fixem el model de la taula de matrícules
        JTable taulaPais = view.getTaulaPais();
        taulaPais.setModel(this.modelComponentsVisuals.getModelPais());

        //Posem valor a el combo d'MPs
        view.getComboPaisos().setModel(modelComponentsVisuals.getComboBoxModel());

        //Desactivem la pestanya de la matrícula
        view.getPestanyes().setEnabledAt(1, false);
        view.getPestanyes().setTitleAt(1, "Matrícula de ...");
    }

    private void setModelTaulaAlumne(DefaultTableModel modelTaulaAlumne, List<Usuari> all) {

        // Fill the table model with data from the collection
        for (Usuari usuari : all) {
            modelTaulaAlumne.addRow(new Object[]{usuari.getUsuari(), usuari.getSeguidors(), usuari.isVerificat(), usuari});
        }
    }

    private void afegirListeners() {

        ModelComponentsVisuals modelo = this.modelComponentsVisuals;
        DefaultTableModel model = modelo.getModel();
        DefaultTableModel modelPais = modelo.getModelPais();
        JTable taula = view.getTaula();
        JTable taulaPais = view.getTaulaPais();
        JButton insertarButton = view.getInsertarButton();
        JButton modificarButton = view.getModificarButton();
        JButton borrarButton = view.getBorrarButton();
        JTextField campUsuari = view.getCampUsuari();
        JTextField campSeguidors = view.getCampSeguidors();
        JCheckBox caixaVerificat = view.getCaixaVerificat();
        JTabbedPane pestanyes = view.getPestanyes();
        JComboBox campPais = view.getComboPaisos();
        JTextField campTop = view.getCampTop();

        //Botó insertar
        view.getInsertarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pestanyes.getSelectedIndex() == 0) { // Si estem a la pestanya de l'alumne
                    try {
                        String usuari = campUsuari.getText();
                        int seguidors = Integer.parseInt(campSeguidors.getText());
                        boolean verificat = caixaVerificat.isSelected();

                        if (usuari.isBlank() || seguidors < 0) {
                            throw new DAOException(2); // Llença una excepció si hi ha dades no vàlides
                        } else if (existeixNomUsuari(usuari)) {
                            throw new DAOException(14); // Llença una excepció si el nom d'usuari ja existeix
                        } else {
                            Usuari us = new Usuari(usuari, seguidors, verificat, new TreeSet<Pais>());
                            dadesUsuari.save(us);
                            model.addRow(new Object[]{us.getUsuari(), us.getSeguidors(), us.isVerificat(), us});

                            // Restaura els camps de text a valors per defecte
                            campUsuari.setText("Willyrex");
                            campUsuari.setSelectionStart(0);
                            campUsuari.setSelectionEnd(campUsuari.getText().length());
                            campSeguidors.setText("0");
                            caixaVerificat.setSelected(false);
                            campUsuari.requestFocus(); // Torna a posar el focus al camp de nom
                        }
                    } catch (NumberFormatException ex) {
                        setExcepcio(new DAOException(2)); // Llença una excepció si el nombre de seguidors no és un número vàlid
                    } catch (DAOException ex) {
                        setExcepcio(ex); // Llença una excepció si es produeix un error al guardar les dades
                    }
                }
            }
        });

        view.getBorrarButton().addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //Mirem si tenim una fila de la taula seleccionada
                int filaSel = taula.getSelectedRow();
                if (pestanyes.getSelectedIndex() == 0) {        //Si estem a la pestanya de l'alumne
                    if (filaSel != -1) {
                        Usuari us = (Usuari) model.getValueAt(filaSel, 3);
//                        Usuari usuariAEsborrar = (Usuari) model.getValueAt(filaSel, 4);
//                        Fitxers.esborrarContrasenya(usuariAEsborrar);
                        model.removeRow(filaSel);
                        try {
                            dadesUsuari.delete(us.getId());
                        } catch (DAOException daoException) {
                            // Manejar la excepción
                        }
                        //Posem els camps de text en blanc
                        campUsuari.setText("");
                        campSeguidors.setText("");
                        //hanFetCanvis=true;
                        //Usuari us = new Usuari(campUsuari.getText(), Integer.parseInt(campSeguidors.getText()), caixaVerificat.isSelected(), new TreeSet<Pais>());

                        if (filaSel < model.getRowCount()) taula.setRowSelectionInterval(filaSel, filaSel);
                        else if (model.getRowCount() > 0) taula.setRowSelectionInterval(filaSel - 1, filaSel - 1);

                        //desactivem pestanyes
                        view.getPestanyes().setEnabledAt(1, false);
                        view.getPestanyes().setTitleAt(1, "País de ...");

                    } else setExcepcio(new DAOException(7));
                }
            }
        });

        view.getModificarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSel = taula.getSelectedRow();
                if (filaSel != -1 && pestanyes.getSelectedIndex() == 0) { // Comprova si hi ha una fila seleccionada
                    if (campUsuari != null && campSeguidors != null && caixaVerificat != null) { // Verifica que els components estiguin inicialitzats
                        try {
                            Usuari us = (Usuari) model.getValueAt(filaSel, 3);
                            if (us != null) { // Verifica si l'objecte Usuari és vàlid
                                String usuariText = campUsuari.getText();
                                String seguidorsText = campSeguidors.getText();
                                // Verifica que els camps de text no estiguin buits
                                if (!usuariText.isBlank() && !seguidorsText.isBlank() && usuariText.length() >= 4) {
                                    us.setUsuari(usuariText);
                                    us.setSeguidors(Integer.parseInt(seguidorsText));
                                    us.setVerificat(caixaVerificat.isSelected());
                                    try {
                                        dadesUsuari.update(us);
                                        // Actualitza les dades de la taula
                                        model.setValueAt(us.getUsuari(), filaSel, 0);
                                        model.setValueAt(us.getSeguidors(), filaSel, 1);
                                        model.setValueAt(us.isVerificat(), filaSel, 2);
                                    } catch (DAOException daoException) {
                                        setExcepcio(new DAOException(12));
                                    }
                                } else {
                                    setExcepcio(new DAOException(2));
                                }
                            } else {
                                setExcepcio(new DAOException(10));
                            }
                        } catch (NumberFormatException ex) {
                            setExcepcio(new DAOException(2));
                        }
                    } else {
                        setExcepcio(new DAOException(12));
                    }
                }
            }
        });


        taula.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //Obtenim el número de la fila seleccionada
                int filaSel = taula.getSelectedRow();

                if (filaSel != -1) {        //Tenim una fila seleccionada
                    //Posem els valors de la fila seleccionada als camps respectius
                    campUsuari.setText(model.getValueAt(filaSel, 0).toString());
                    campSeguidors.setText(model.getValueAt(filaSel, 1).toString().replaceAll("\\.", ","));
                    caixaVerificat.setSelected((Boolean) model.getValueAt(filaSel, 2));

                    //Activem la pestanya de la matrícula de l'alumne seleccionat
                    view.getPestanyes().setEnabledAt(1, false);
                    view.getPestanyes().setTitleAt(1, "Pais de " + campUsuari.getText());

                    //Posem valor a el combo d'MPs
                    //view.getComboPaisos().setModel(modelo.getComboBoxModel());
//                    ompliPaisos((Usuari) model.getValueAt(filaSel, 3), modelPais);
                } else {                  //Hem deseleccionat una fila
                    //Posem els camps de text en blanc
                    campUsuari.setText("");
                    campSeguidors.setText("");

                    //Desactivem pestanyes
                    view.getPestanyes().setEnabledAt(1, false);
                    view.getPestanyes().setTitleAt(1, "Pais de ...");
                }
            }
        });

        campUsuari.addFocusListener(new FocusAdapter() {
            /**
             * Invoked when a component loses the keyboard focus.
             *
             * @param e
             */
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);

                //Comprovem que el valor introduït és el d'un ingrés correcte a més a més no ha de ser repetit
                if(campUsuari.getText().length()<4){
                    setExcepcio(new DAOException(6));
                    //JOptionPane.showMessageDialog(null, "Has d'introduir un nom d'usuari amb més de 4 caràcters i no ha de ser repetit!!");
                    campUsuari.setSelectionStart(0);
                    campUsuari.setSelectionEnd(campUsuari.getText().length());
                    campUsuari.requestFocus();
                }
            }
        });
        //throw new LaMeuaExcepcio(1,"Ha petat la base de dades");

        campSeguidors.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String text = campSeguidors.getText();
                Pattern pattern = Pattern.compile("^-?\\d+$"); // Includes negative integers
                Matcher matcher = pattern.matcher(text);

                // Check if the input is a valid integer
                if (matcher.matches()) {
                    try {
                        int seguidors = Integer.parseInt(campSeguidors.getText());
                        if (seguidors < 0) throw new NumberFormatException();
                    } catch (NumberFormatException ex) {
                        setExcepcio(new DAOException(4));
                        campSeguidors.setSelectionStart(0);
                        campSeguidors.setSelectionEnd(campSeguidors.getText().length());
                        campSeguidors.requestFocus();
                    }
                } else {
                    // If the input is not a valid integer, show an error message
                    setExcepcio(new DAOException(4));
                    campSeguidors.setSelectionStart(0);
                    campSeguidors.setSelectionEnd(campSeguidors.getText().length());
                    campSeguidors.requestFocus();
                }
            }
        });

    }

    //TRACTAMENT D'EXCEPCIONS

    //2. Propietat lligada per controlar quan genero una excepció
    public static final String PROP_EXCEPCIO="excepcio";
    private DAOException excepcio;

    public DAOException getExcepcio() {
        return excepcio;
    }

    public void setExcepcio(DAOException excepcio) {
        DAOException valorVell=this.excepcio;
        this.excepcio = excepcio;
        canvis.firePropertyChange(PROP_EXCEPCIO, valorVell,excepcio);
    }


    //3. Propietat PropertyChangesupport necessària per poder controlar les propietats lligades
    PropertyChangeSupport canvis=new PropertyChangeSupport(this);


    //4. Mètode on posarem el codi de tractament de les excepcions --> generat per la interfície PropertyChangeListener
    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        DAOException rebuda=(DAOException)evt.getNewValue();

        try {
            throw rebuda;
        } catch (DAOException e) {
            //Aquí farem ele tractament de les excepcions de l'aplicació
            switch(evt.getPropertyName()){
                case PROP_EXCEPCIO:

                    switch(rebuda.getTipo()){
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            break;
                    }


            }
        }
    }

    private boolean existeixNomUsuari(String nomUsuari) {
        // Comprova si el nom d'usuari ja existeix a la taula
        DefaultTableModel model = (DefaultTableModel) view.getTaula().getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String usuariActual = (String) model.getValueAt(i, 0);
            if (usuariActual.equals(nomUsuari)) {
                return true; // Nom d'usuari ja existeix
            }
        }
        return false; // Nom d'usuari no existeix
    }

}
