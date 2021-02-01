import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mirenordonezdearce
 */
public class GestionConexion {
    Connection conexion = null;
    
    public GestionConexion() {
        //Creamos constructor para que al ejecutar, conecte con la BBDD.
        try {
            String urll = "jdbc:mysql://localhost:3306/discografica?serverTimezone=UTC";
            String user = "root";
            String password = "root1234";
            
            conexion = DriverManager.getConnection(urll, user, password);
            
            if (conexion != null) {
               System.out.println("Conectado correctamente a la BBDD discográfica."); 
            }
            else {
                System.out.println("Error al conectar a la BBDD.");
            }
            
        } catch (Exception ex) {
            System.out.println("Error en la conexión." + ex.toString());
        }
    }
    
    
    public void desconectar() {
        try {
            conexion.close();
            System.out.println("Desconectado correctamente de la BBDD discográfica.");
        } catch (Exception ex) {
            System.out.println("Error al desconectar de la BBDD." + ex.toString());
        }
    }
    
    //--------------------------------------------------
    
    //PARA MOSTRAR LAS TABLAS
    public ResultSet mostrarTabla(String consulta) {
        Statement sta;
        ResultSet datos = null;
        try {
            sta = conexion.createStatement();
            datos =  sta.executeQuery(consulta);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        } 
        return datos;
    }
    
    //--------------------------------------------------
    
    //PARA AÑADIR NUEVAS COLUMNAS A LAS TABLAS
    public void annadirColumnaAlbum(String nombreColumna, String dato) {
        Statement sta;
        
        //Ponemos los datos en minúscula, para que no haya error al añadirlo.
        nombreColumna = nombreColumna.toLowerCase();
        //Adjudicamos tipo de datos, según lo seleccionado.
        if (dato.equals("Numérico")) {
            dato = "INT";
        }
        else {
            dato = "VARCHAR(50)";
        }
        
        //Añadimos la columna nueva.
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("ALTER TABLE album ADD " + nombreColumna + " " + dato + ";");
            sta.close();
        } catch (Exception ex) {
            System.out.println("Error al añadir una columna a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
       
    }
    
    public void annadirColumnaCanciones(String nombreColumna, String dato) {
        Statement sta;
        
        //Ponemos los datos en minúscula, para que no haya error al añadirlo.
        nombreColumna = nombreColumna.toLowerCase();
        //Adjudicamos tipo de datos, según lo seleccionado.
        if (dato.equals("Numérico")) {
            dato = "INT";
        }
        else {
            dato = "VARCHAR(50)";
        }
        
        //Añadimos la columna nueva.
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("ALTER TABLE canciones ADD " + nombreColumna + " " + dato + ";");
            sta.close();
        } catch (Exception ex) {
            System.out.println("Error al añadir una columna a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
       
    }
    
    //--------------------------------------------------
    
    //PARA BORRAR UNA COLUMNA
    public void borrarColumna(String campo) {
        Statement sta;
        if (campo.equals("Artista")) {
            campo = "publicado_en";
        }
        else {
            campo = "imagen";
        }
        
        //Borramos la columna 
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("ALTER TABLE album DROP " + campo + ";");
            sta.close();
        } catch (Exception ex) {
            System.out.println("Error al eliminar la columna a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
    }
    
    //--------------------------------------------------
    
    
    //PARA INSERTAR UN ÁLBUM NUEVO
    public void insertarDatosAlbum(String campo1, String campo2, String campo3) {
        Statement sta;
        
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("INSERT INTO album(idAlbum, nombre, artista, publicado_en) "
                    + "VALUES (0, '" + campo1 + "', '" + campo2 + "', '" + campo3 + "');");
            sta.close();
        } catch (Exception ex) {
            System.out.println("Error al insertar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
    }
    
    
    //PARA INSERTAR UNA CANCIÓN NUEVA
    public void insertarDatosCancion(String campo1, String campo2, String nombreAlbum) {
        //Guardo el número del álbum para tener el idAlbum.
        int aux = 0;
        String album = "";
        while (nombreAlbum.charAt(aux) != '-') {
            album = album + nombreAlbum.charAt(aux);
            aux++;
        }
        
        Statement sta;
        
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("INSERT INTO canciones(idCancion, titulo, duracion, album) "
                    + "VALUES (0, '" + campo1 + "', '" + campo2 + "', " + album + ");");
            sta.close();
        } catch (Exception ex) {
            System.out.println("Error al insertar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
    }
    
    //--------------------------------------------------
    
    
    //Para llenar el comboBox en la Tab Canciones, con los álbumes
    public ArrayList<String> rellenarComboBox() {
        ArrayList<String> listaAlbumes = new ArrayList<String>();
        Statement sta;
        try {
            sta = conexion.createStatement();
            String query = "SELECT idAlbum, nombre FROM album;";
            ResultSet rs = sta.executeQuery(query);
            
            while (rs.next()) {
                listaAlbumes.add(rs.getInt("idAlbum") + "- " + rs.getString("nombre"));
            }
            
            rs.close();
            sta.close();
        } catch(Exception ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        
        return listaAlbumes;
    }
    
    //--------------------------------------------------
    
    //PARA MODIFICAR DATOS
    public void modifAlbum(String idAlbum, String nombre, String artista, String anno) {
        Statement sta;
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("UPDATE album SET nombre = '" + nombre + "', artista = '" + artista + 
                    "', publicado_en = '" + anno + "' WHERE idAlbum = " + idAlbum + ";");
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        
    }
    
    public void modifCancion(String idCancion, String titulo, String duracion, String album) {
        Statement sta;
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("UPDATE canciones SET titulo = '" + titulo + "', duracion = '" + duracion + 
                    "', album = '" + album + "' WHERE idCancion = " + idCancion + ";");
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        
    }
    
    //--------------------------------------------------
    
    //PARA ELIMINAR REGISTROS
    public void deleteAlbum(String idAlbum) {
        Statement sta;
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("DELETE FROM album WHERE idAlbum = " + idAlbum + ";");
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.toString());
        }
    }
    
    public void deleteCancion(String idCancion) {
        Statement sta;
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("DELETE FROM canciones WHERE idCancion = " + idCancion + ";");
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.toString());
        }
    }
    
    //--------------------------------------------------
    
    //PARA FILTRAR BÚSQUEDAS
    public void filtrarCancion(String nombreAlbum) {
        //Guardo el número del álbum para tener el idAlbum.
        int aux = 0;
        String album = "";
        while (nombreAlbum.charAt(aux) != '-') {
            album = album + nombreAlbum.charAt(aux);
            aux++;
        }
        
        //El usuario selecciona el álbum
        String query = "SELECT * FROM canciones WHERE album = ?;";
        
        try {
            PreparedStatement psta = conexion.prepareStatement(query);
            psta.setString(1, album);
            ResultSet rs = psta.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("idCancion") + ", TITULO: " + 
                        rs.getString("titulo") + ", DURACION: " + rs.getString("duracion") + 
                        ", ALBUM: " + rs.getInt("album"));
            }
            rs.close();
            psta.close();
        
        } catch(Exception ex) {
            System.out.println("ERROR: " + ex.toString());
        }
    }
    
   
}
