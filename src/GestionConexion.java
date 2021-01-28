import java.sql.*;

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
            
        } catch (Exception ex) {
            System.out.println("Error al añadir una columna a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
       
    }
    
    //PARA INSERTAR UN ÁLBUM NUEVO
    public void insertarDatosAlbum( String campo1, String campo2, String campo3) {
        Statement sta;
        
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("INSERT INTO album(idAlbum, nombre, artista, publicado_en) VALUES (0, '" + campo1 + "', '" + campo2 + "', '" + campo3 + "');");
        } catch (Exception ex) {
            System.out.println("Error al insertar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
    }
    
    
    //PARA INSERTAR UNA CANCIÓN NUEVA
    public void insertarDatosCancion(String campo1, String campo2, String nombreAlbum) {
        if (nombreAlbum.equals("Salvavida")) {
            nombreAlbum = "1";
        }
        else {
            nombreAlbum = "2";
        }
        
        Statement sta;
        
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("INSERT INTO canciones(idCancion, titulo, letra, album) VALUES (0, '" + campo1 + "', '" + campo2 + "', " + nombreAlbum + ");");
        } catch (Exception ex) {
            System.out.println("Error al insertar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
    }
    
    public String[] consultaTablaAlbum() {
        Statement sta;
        
        try {
            sta = conexion.createStatement();
            
            String query = "SELECT * FROM album;";
            ResultSet rs = sta.executeQuery(query);
            
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               System.out.println(etiquetas[i]);
            }
            
            
            while (rs.next()) {
                
            }
            
            
            rs.close();
            sta.close();
            return etiquetas;
                    
        } catch (Exception ex) {
            System.out.println("Error al insertar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
        
        return null;
    }
    
    public String consultaTablaCanciones() {
        Statement sta;
        String datos = "";
        
        try {
            sta = conexion.createStatement();
            
            String query = "SELECT * FROM canciones;";
            ResultSet rs = sta.executeQuery(query);
            
            while (rs.next()) {
                datos = datos + "ID: " + rs.getInt("idCancion") +  
                        ", TÍTULO: " + rs.getString("titulo") + 
                        ", DURACIÓN: " + rs.getString("duracion") + 
                        ", ALBUM: " + rs.getInt("album") 
                        + "\n" + "--------------------------------------" + "\n"; 
                        
            }
            rs.close();
            sta.close();
        } catch (Exception ex) {
            System.out.println("Error al insertar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
        return datos;
    }
}
