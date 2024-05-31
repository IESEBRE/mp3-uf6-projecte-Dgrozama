package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Usuari;
import org.example.model.exceptions.DAOException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

public class UsuariDAOJDBCOracleImpl implements DAO<Usuari> {

    private String url;
    private String user;
    private String password;

    public UsuariDAOJDBCOracleImpl() {
        Properties properties = new Properties();
        try {
            InputStream input = Files.newInputStream(Paths.get("src/main/resources/database.properties"));
            properties.load(input);
            this.url = properties.getProperty("URL").replace("\"", "");
            this.user = properties.getProperty("USR").replace("\"", "");
            this.password = properties.getProperty("PWD").replace("\"", "");
        } catch (IOException e) {
            throw new RuntimeException("No es troba el system.properties", e);
        }
    }


    @Override
    public Usuari get(Long id) throws DAOException {
        //Declaració de variables del mètode
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Usuari usuari = null;

        //Accés a la BD usant l'API JDBC
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM USUARIS;");
            if (rs.next()) {
                usuari = new Usuari(Long.valueOf(rs.getString(1)), rs.getString(2));
            }
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        }
        return usuari;
    }

    @Override
    public List<Usuari> getAll() throws DAOException {
        List<Usuari> usuaris = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement st = con.prepareStatement("SELECT * FROM USUARIS");
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                boolean verificat = rs.getInt("VERIFICAT") == 1;
                usuaris.add(new Usuari(rs.getLong("ID"), rs.getString("USUARI"), rs.getInt("SEGUIDORS"),
                        verificat,
                        new TreeSet<Usuari.Pais>()));
            }
        } catch (SQLException throwables) {
            throw new DAOException(1);
        }
        return usuaris;
    }

    @Override
    public void save(Usuari obj) throws DAOException {
        if (obj == null) {
            throw new IllegalArgumentException("L'objecte usuari no pot ser null");
        }
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO USUARIS (USUARI, SEGUIDORS, VERIFICAT) VALUES (?, ?, ?)";
            PreparedStatement st = con.prepareStatement(sql, new String[]{"ID"});
            st.setString(1, obj.getUsuari());
            st.setInt(2, obj.getSeguidors());
            st.setInt(3, obj.isVerificat() ? 1 : 0);
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    obj.setId(id);
                }
            }
        } catch (SQLException throwables) {
            throw new DAOException(1);
        }
    }

    @Override
    public void update(Usuari obj) throws DAOException {
        if (obj == null) {
            throw new IllegalArgumentException("L'objecte usuari no pot ser null");
        }
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE USUARIS SET USUARI = ?, SEGUIDORS = ?, VERIFICAT = ? WHERE ID = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, obj.getUsuari());
            st.setInt(2, obj.getSeguidors());
            st.setInt(3, obj.isVerificat() ? 1 : 0);
            st.setLong(4, obj.getId());
            st.executeUpdate();
        } catch (SQLException throwables) {
            throw new DAOException(1);
        }
    }

    @Override
    public void delete(Long obj) throws DAOException {
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement st = con.prepareStatement("DELETE FROM USUARIS WHERE ID = ?")) {
            st.setLong(1, obj);
            st.executeUpdate();
        } catch (SQLException throwables) {
            throw new DAOException(1);
        }
    }

    @Override
    public void crearTaulaBD() throws DAOException {
        String checkTableExists = "BEGIN " +
                "   DECLARE " +
                "       t_count NUMBER; " +
                "   BEGIN " +
                "       SELECT COUNT(*) INTO t_count FROM user_tables WHERE table_name = 'USUARIS'; " +
                "       IF t_count = 0 THEN " +
                "           EXECUTE IMMEDIATE 'CREATE TABLE USUARIS ( " +
                "               ID NUMBER(10) NOT NULL, " +
                "               USUARI VARCHAR2(50) NOT NULL, " +
                "               SEGUIDORS NUMBER(10) NOT NULL, " +
                "               VERIFICAT NUMBER(1) NOT NULL, " +
                "               PRIMARY KEY (ID) " +
                "           )'; " +
                "       END IF; " +
                "   END; " +
                "END;";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement st = con.prepareStatement(checkTableExists)) {
            st.execute();
        } catch (SQLException throwables) {
            throw new DAOException(1);
        }
    }

    public void executaArxiuSQL(String sqlFilePath) throws DAOException {
        try {
            String sql = new String(Files.readAllBytes(Paths.get(sqlFilePath)));
            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement()) {
                st.execute(sql);
            }
        } catch (SQLException e) {
            throw new DAOException(1);
        } catch (Exception e) {
            throw new DAOException(1);
        }
    }
}