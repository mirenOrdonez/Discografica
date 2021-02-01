
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mirenordonezdearce
 */
public class VentanaPrincipal extends javax.swing.JFrame{

    //Abrimos la conexión con la BBDD, llamando al constructor.
    GestionConexion conexion = new GestionConexion();
    
    
    
    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents(); 
        mostrarTablaAlbum();
        mostrarTablaCanciones();
        editar();
        
        //Rellenamos el jComboBox para filtrar búsquedas
        //Creamos un arrayList para que muestre en el jComboBox los álbumes
        ArrayList<String> listaAlbumes = new ArrayList<String>();
        listaAlbumes = conexion.rellenarComboBox();
        for (int i=0; i < listaAlbumes.size(); i++) {
            jComboBoxFiltrarCanciones.addItem(listaAlbumes.get(i));
        }
        
        //Para desconectarse de la BBDD cuando se cierre la interfaz de la Ventana Principal
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                conexion.desconectar();
                
                System.exit(0);
            }
        });
    }

    //PARA MOSTRAR LAS TABLAS
    private void mostrarTablaAlbum() {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        //Hacemos la consulta
        ResultSet rs = (ResultSet) conexion.mostrarTabla("SELECT * FROM album ORDER BY idAlbum");
        
        try {
            //Obtenemos los nombres de las columnas
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               modelo.setColumnIdentifiers(etiquetas);
            }
            while (rs.next()) {
                //Obtenemos los datos de las filas
                modelo.addRow(new Object[]{rs.getInt("idAlbum"), rs.getString("nombre"),
                rs.getString("artista"), rs.getString("publicado_en")});
            }
            //Rellenamos la tabla con los datos.
            jTableAlbum.setModel(modelo);
            rs.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
    }
    
    private void mostrarTablaCanciones() {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        try {
            //Hacemos la consulta
            ResultSet rs = (ResultSet) conexion.mostrarTabla("SELECT canciones.*, album.nombre "
                    + "FROM canciones, album WHERE canciones.album = album.idAlbum "
                    + "ORDER BY idCancion;");

            //Obtenemos los nombres de las columnas
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               modelo.setColumnIdentifiers(etiquetas);
            }
            while (rs.next()) {
                //Obtenemos los datos de las filas
                modelo.addRow(new Object[]{rs.getInt("idCancion"), rs.getString("titulo"),
                rs.getString("duracion"), rs.getInt("album") ,rs.getString("album.nombre")});
            }
            //Rellenamos la tabla con los datos.
            jTableCanciones.setModel(modelo);
            rs.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
    }
    
    private void editar() {
        
        //Para que aparezcan en los TextFields los datos de la fila seleccionada.
        //Para la tabla álbum.
        jTableAlbum.addMouseListener(new MouseAdapter()  {
            @Override
		public void mousePressed(MouseEvent e) { 
                    int filaAlbum = jTableAlbum.getSelectedRow();
                    if (filaAlbum == -1){
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila.");
                    } else {
                        String nombreAlbum = (String)jTableAlbum.getValueAt(filaAlbum, 1);
                        String artistaAlbum = (String)jTableAlbum.getValueAt(filaAlbum, 2);
                        String annoAlbum = (String)jTableAlbum.getValueAt(filaAlbum, 3);
                        jTextFieldNombreAlbum.setText(nombreAlbum);
                        jTextFieldArtistaAlbum.setText(artistaAlbum);
                        jTextFieldAnnoAlbum.setText(annoAlbum);
                    }
                }
        });
        //Para la tabla canciones.
        jTableCanciones.addMouseListener(new MouseAdapter()  {
            @Override
		public void mousePressed(MouseEvent ev) { 
                    int filaCanciones = jTableCanciones.getSelectedRow();
                    if (filaCanciones == -1){
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila.");
                    } else {
                        String tituloCancion = (String)jTableCanciones.getValueAt(filaCanciones, 1);
                        String duracionCancion = (String)jTableCanciones.getValueAt(filaCanciones, 2);
                        jTextFieldTituloCancion.setText(tituloCancion);
                        jTextFieldDuracionCancion.setText(duracionCancion);
                        //Creamos un arrayList para que muestre en el jComboBox los álbumes
                        ArrayList<String> listaAlbumes = new ArrayList<String>();
                        listaAlbumes = conexion.rellenarComboBox();
                        for (int i=0; i < listaAlbumes.size(); i++) {
                        jComboBoxAlbumCancion.addItem(listaAlbumes.get(i));
                        }
                        //Que ponga el álbum que es según la fila seleccionada.
                        jComboBoxAlbumCancion.setSelectedIndex(((int) jTableCanciones.getValueAt(filaCanciones, 3)) - 1);
                    }
                }
        });
        
        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogColumnaNuevaAlbum = new javax.swing.JDialog();
        lblColumnaCampo = new javax.swing.JLabel();
        jTextFieldColumnaAlbum = new javax.swing.JTextField();
        lblColumnaTipo = new javax.swing.JLabel();
        jComboBoxTipoAlbum = new javax.swing.JComboBox<>();
        jButtonAddUpdateAlbum = new javax.swing.JButton();
        lblTituloColumnaAlbum = new javax.swing.JLabel();
        jDialogColumnaNuevaCanciones = new javax.swing.JDialog();
        lblColumnaCampoCancion = new javax.swing.JLabel();
        jTextFieldColumnaCancion = new javax.swing.JTextField();
        lblColumnaTipo1 = new javax.swing.JLabel();
        jComboBoxTipoCancion = new javax.swing.JComboBox<>();
        jButtonAddUpdateCancion = new javax.swing.JButton();
        lblTituloColumnaAlbum1 = new javax.swing.JLabel();
        jDialogBorrarColumnaAlbum = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxColumnasTablaAlbum = new javax.swing.JComboBox<>();
        btnEliminarColumnaAlbum = new javax.swing.JButton();
        jDialogBorrarColumnaCanciones = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxColumnasTablaCanciones = new javax.swing.JComboBox<>();
        btnEliminarColumnaCanciones = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        tabAlbum = new javax.swing.JPanel();
        jPanelTablaAlbum = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAlbum = new javax.swing.JTable();
        jPanelEstructuraAlbum = new javax.swing.JPanel();
        btnColumnaNuevaAlbum = new javax.swing.JButton();
        btnDeleteColumnaAlbum = new javax.swing.JButton();
        jPanelDatosAlbum = new javax.swing.JPanel();
        lblNombreAlbum = new javax.swing.JLabel();
        jTextFieldNombreAlbum = new javax.swing.JTextField();
        lblArtistaAlbum = new javax.swing.JLabel();
        jTextFieldArtistaAlbum = new javax.swing.JTextField();
        lblAnnoAlbum = new javax.swing.JLabel();
        jTextFieldAnnoAlbum = new javax.swing.JTextField();
        btnInsertarAlbum = new javax.swing.JButton();
        btnModifAlbum = new javax.swing.JButton();
        btnDeleteAlbum = new javax.swing.JButton();
        lblInfoAlbum = new javax.swing.JLabel();
        tabCanciones = new javax.swing.JPanel();
        jPanelTablaCanciones = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableCanciones = new javax.swing.JTable();
        jComboBoxFiltrarCanciones = new javax.swing.JComboBox<>();
        lblBuscarCanciones = new javax.swing.JLabel();
        btnFiltrarCanciones = new javax.swing.JButton();
        jPanelEstructuraCanciones = new javax.swing.JPanel();
        btnColumnaNuevaCancion = new javax.swing.JButton();
        btnDeleteColumnaCancion = new javax.swing.JButton();
        jPanelDatosAlbum1 = new javax.swing.JPanel();
        lblTituloCancion = new javax.swing.JLabel();
        jTextFieldTituloCancion = new javax.swing.JTextField();
        lblDuracionCancion = new javax.swing.JLabel();
        jTextFieldDuracionCancion = new javax.swing.JTextField();
        btnInsertarCancion = new javax.swing.JButton();
        btnModifCancion = new javax.swing.JButton();
        btnDeleteCancion = new javax.swing.JButton();
        lblInfoCanciones = new javax.swing.JLabel();
        lblAlbumCancion = new javax.swing.JLabel();
        jComboBoxAlbumCancion = new javax.swing.JComboBox<>();

        jDialogColumnaNuevaAlbum.setBounds(new java.awt.Rectangle(0, 25, 285, 240));

        lblColumnaCampo.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lblColumnaCampo.setText("Escribe el nombre del nuevo campo a añadir.");

        lblColumnaTipo.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lblColumnaTipo.setText("Seleccione el tipo de dato que se va a añadir.");

        jComboBoxTipoAlbum.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Numérico", "Alfanumérico", "Alfabético" }));

        jButtonAddUpdateAlbum.setText("Añadir y actualizar tabla");
        jButtonAddUpdateAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddUpdateAlbumActionPerformed(evt);
            }
        });

        lblTituloColumnaAlbum.setText("Insertar una nueva columna a la tabla Álbum");

        javax.swing.GroupLayout jDialogColumnaNuevaAlbumLayout = new javax.swing.GroupLayout(jDialogColumnaNuevaAlbum.getContentPane());
        jDialogColumnaNuevaAlbum.getContentPane().setLayout(jDialogColumnaNuevaAlbumLayout);
        jDialogColumnaNuevaAlbumLayout.setHorizontalGroup(
            jDialogColumnaNuevaAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogColumnaNuevaAlbumLayout.createSequentialGroup()
                .addGroup(jDialogColumnaNuevaAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTituloColumnaAlbum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jDialogColumnaNuevaAlbumLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblColumnaCampo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jTextFieldColumnaAlbum, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDialogColumnaNuevaAlbumLayout.createSequentialGroup()
                        .addGroup(jDialogColumnaNuevaAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialogColumnaNuevaAlbumLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblColumnaTipo))
                            .addComponent(jComboBoxTipoAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jDialogColumnaNuevaAlbumLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jButtonAddUpdateAlbum)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialogColumnaNuevaAlbumLayout.setVerticalGroup(
            jDialogColumnaNuevaAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogColumnaNuevaAlbumLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTituloColumnaAlbum)
                .addGap(21, 21, 21)
                .addComponent(lblColumnaCampo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldColumnaAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblColumnaTipo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxTipoAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jButtonAddUpdateAlbum)
                .addGap(33, 33, 33))
        );

        jDialogColumnaNuevaCanciones.setBackground(new java.awt.Color(0, 153, 153));
        jDialogColumnaNuevaCanciones.setBounds(new java.awt.Rectangle(0, 25, 310, 240));

        lblColumnaCampoCancion.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lblColumnaCampoCancion.setText("Escribe el nombre del nuevo campo a añadir.");

        lblColumnaTipo1.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lblColumnaTipo1.setText("Seleccione el tipo de dato que se va a añadir.");

        jComboBoxTipoCancion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Numérico", "Alfanumérico", "Alfabético" }));

        jButtonAddUpdateCancion.setText("Añadir y actualizar tabla");
        jButtonAddUpdateCancion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddUpdateCancionActionPerformed(evt);
            }
        });

        lblTituloColumnaAlbum1.setText("Insertar una nueva columna a la tabla Canciones");

        javax.swing.GroupLayout jDialogColumnaNuevaCancionesLayout = new javax.swing.GroupLayout(jDialogColumnaNuevaCanciones.getContentPane());
        jDialogColumnaNuevaCanciones.getContentPane().setLayout(jDialogColumnaNuevaCancionesLayout);
        jDialogColumnaNuevaCancionesLayout.setHorizontalGroup(
            jDialogColumnaNuevaCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogColumnaNuevaCancionesLayout.createSequentialGroup()
                .addGroup(jDialogColumnaNuevaCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTituloColumnaAlbum1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jDialogColumnaNuevaCancionesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblColumnaCampoCancion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jTextFieldColumnaCancion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDialogColumnaNuevaCancionesLayout.createSequentialGroup()
                        .addGroup(jDialogColumnaNuevaCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialogColumnaNuevaCancionesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblColumnaTipo1))
                            .addComponent(jComboBoxTipoCancion, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jDialogColumnaNuevaCancionesLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jButtonAddUpdateCancion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialogColumnaNuevaCancionesLayout.setVerticalGroup(
            jDialogColumnaNuevaCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogColumnaNuevaCancionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTituloColumnaAlbum1)
                .addGap(21, 21, 21)
                .addComponent(lblColumnaCampoCancion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldColumnaCancion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblColumnaTipo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxTipoCancion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jButtonAddUpdateCancion)
                .addGap(33, 33, 33))
        );

        jDialogBorrarColumnaAlbum.setBackground(new java.awt.Color(0, 153, 153));
        jDialogBorrarColumnaAlbum.setBounds(new java.awt.Rectangle(10, 25, 190, 213));

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setBounds(new java.awt.Rectangle(10, 25, 190, 235));
        jPanel1.setMinimumSize(new java.awt.Dimension(190, 213));

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Selecciona la columna que deseas ");

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("borrar de la Base de Datos");

        jComboBoxColumnasTablaAlbum.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona", "Artista", "Año de publicación", "Imagen" }));

        btnEliminarColumnaAlbum.setText("Eliminar");
        btnEliminarColumnaAlbum.setBounds(new java.awt.Rectangle(10, 25, 190, 235));
        btnEliminarColumnaAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarColumnaAlbumActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jComboBoxColumnasTablaAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnEliminarColumnaAlbum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxColumnasTablaAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addComponent(btnEliminarColumnaAlbum)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout jDialogBorrarColumnaAlbumLayout = new javax.swing.GroupLayout(jDialogBorrarColumnaAlbum.getContentPane());
        jDialogBorrarColumnaAlbum.getContentPane().setLayout(jDialogBorrarColumnaAlbumLayout);
        jDialogBorrarColumnaAlbumLayout.setHorizontalGroup(
            jDialogBorrarColumnaAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogBorrarColumnaAlbumLayout.setVerticalGroup(
            jDialogBorrarColumnaAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogBorrarColumnaCanciones.setBackground(new java.awt.Color(0, 153, 153));
        jDialogBorrarColumnaCanciones.setBounds(new java.awt.Rectangle(10, 25, 190, 235));
        jDialogBorrarColumnaCanciones.setLocation(new java.awt.Point(10, 25));
        jDialogBorrarColumnaCanciones.setMinimumSize(new java.awt.Dimension(190, 235));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setBounds(new java.awt.Rectangle(10, 25, 190, 235));
        jPanel2.setMinimumSize(new java.awt.Dimension(190, 235));

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Selecciona la columna que deseas ");

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("borrar de la Base de Datos");

        jComboBoxColumnasTablaCanciones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona", "Compositor" }));

        btnEliminarColumnaCanciones.setText("Eliminar");
        btnEliminarColumnaCanciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarColumnaCancionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminarColumnaCanciones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jComboBoxColumnasTablaCanciones, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxColumnasTablaCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                .addComponent(btnEliminarColumnaCanciones)
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogBorrarColumnaCancionesLayout = new javax.swing.GroupLayout(jDialogBorrarColumnaCanciones.getContentPane());
        jDialogBorrarColumnaCanciones.getContentPane().setLayout(jDialogBorrarColumnaCancionesLayout);
        jDialogBorrarColumnaCancionesLayout.setHorizontalGroup(
            jDialogBorrarColumnaCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogBorrarColumnaCancionesLayout.setVerticalGroup(
            jDialogBorrarColumnaCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(221, 238, 244));

        jTabbedPane1.setBackground(new java.awt.Color(219, 238, 248));

        tabAlbum.setBackground(new java.awt.Color(0, 153, 153));

        jPanelTablaAlbum.setBackground(new java.awt.Color(0, 153, 153));

        jTableAlbum.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableAlbum);

        javax.swing.GroupLayout jPanelTablaAlbumLayout = new javax.swing.GroupLayout(jPanelTablaAlbum);
        jPanelTablaAlbum.setLayout(jPanelTablaAlbumLayout);
        jPanelTablaAlbumLayout.setHorizontalGroup(
            jPanelTablaAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaAlbumLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelTablaAlbumLayout.setVerticalGroup(
            jPanelTablaAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaAlbumLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelEstructuraAlbum.setBackground(new java.awt.Color(0, 153, 153));
        jPanelEstructuraAlbum.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), null));
        jPanelEstructuraAlbum.setBounds(new java.awt.Rectangle(0, 0, 247, 178));
        jPanelEstructuraAlbum.setMaximumSize(new java.awt.Dimension(247, 178));

        btnColumnaNuevaAlbum.setText("Añadir columna nueva");
        btnColumnaNuevaAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColumnaNuevaAlbumActionPerformed(evt);
            }
        });

        btnDeleteColumnaAlbum.setText("Eliminar columna existente");
        btnDeleteColumnaAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteColumnaAlbumActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEstructuraAlbumLayout = new javax.swing.GroupLayout(jPanelEstructuraAlbum);
        jPanelEstructuraAlbum.setLayout(jPanelEstructuraAlbumLayout);
        jPanelEstructuraAlbumLayout.setHorizontalGroup(
            jPanelEstructuraAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEstructuraAlbumLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEstructuraAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnColumnaNuevaAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDeleteColumnaAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelEstructuraAlbumLayout.setVerticalGroup(
            jPanelEstructuraAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEstructuraAlbumLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnColumnaNuevaAlbum)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDeleteColumnaAlbum)
                .addContainerGap())
        );

        jPanelDatosAlbum.setBackground(new java.awt.Color(0, 153, 153));
        jPanelDatosAlbum.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), null));
        jPanelDatosAlbum.setBounds(new java.awt.Rectangle(0, 0, 626, 178));
        jPanelDatosAlbum.setMaximumSize(new java.awt.Dimension(626, 32767));
        jPanelDatosAlbum.setMinimumSize(new java.awt.Dimension(626, 178));

        lblNombreAlbum.setText("NOMBRE");

        lblArtistaAlbum.setText("ARTISTA");

        lblAnnoAlbum.setText("AÑO PUBLICACIÓN");

        btnInsertarAlbum.setText("Insertar");
        btnInsertarAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarAlbumActionPerformed(evt);
            }
        });

        btnModifAlbum.setText("Modificar");
        btnModifAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifAlbumActionPerformed(evt);
            }
        });

        btnDeleteAlbum.setText("Eliminar");
        btnDeleteAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAlbumActionPerformed(evt);
            }
        });

        lblInfoAlbum.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lblInfoAlbum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanelDatosAlbumLayout = new javax.swing.GroupLayout(jPanelDatosAlbum);
        jPanelDatosAlbum.setLayout(jPanelDatosAlbumLayout);
        jPanelDatosAlbumLayout.setHorizontalGroup(
            jPanelDatosAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosAlbumLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatosAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblInfoAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelDatosAlbumLayout.createSequentialGroup()
                        .addGroup(jPanelDatosAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblAnnoAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblArtistaAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldArtistaAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                            .addComponent(jTextFieldAnnoAlbum)
                            .addComponent(jTextFieldNombreAlbum)))
                    .addGroup(jPanelDatosAlbumLayout.createSequentialGroup()
                        .addComponent(btnInsertarAlbum)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModifAlbum)
                        .addGap(110, 110, 110)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDeleteAlbum))
        );
        jPanelDatosAlbumLayout.setVerticalGroup(
            jPanelDatosAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosAlbumLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatosAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreAlbum)
                    .addComponent(jTextFieldNombreAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatosAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblArtistaAlbum)
                    .addComponent(jTextFieldArtistaAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatosAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAnnoAlbum)
                    .addComponent(jTextFieldAnnoAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(lblInfoAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatosAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsertarAlbum)
                    .addComponent(btnModifAlbum)
                    .addComponent(btnDeleteAlbum))
                .addContainerGap())
        );

        javax.swing.GroupLayout tabAlbumLayout = new javax.swing.GroupLayout(tabAlbum);
        tabAlbum.setLayout(tabAlbumLayout);
        tabAlbumLayout.setHorizontalGroup(
            tabAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTablaAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabAlbumLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelEstructuraAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDatosAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabAlbumLayout.setVerticalGroup(
            tabAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabAlbumLayout.createSequentialGroup()
                .addComponent(jPanelTablaAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelEstructuraAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelDatosAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Álbumes", tabAlbum);

        tabCanciones.setBackground(new java.awt.Color(0, 153, 153));

        jPanelTablaCanciones.setBackground(new java.awt.Color(0, 153, 153));

        jTableCanciones.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTableCanciones);

        jComboBoxFiltrarCanciones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        lblBuscarCanciones.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lblBuscarCanciones.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblBuscarCanciones.setText("Seleccione el álbum que desee buscar:");

        btnFiltrarCanciones.setText("Filtrar búsqueda");
        btnFiltrarCanciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarCancionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTablaCancionesLayout = new javax.swing.GroupLayout(jPanelTablaCanciones);
        jPanelTablaCanciones.setLayout(jPanelTablaCancionesLayout);
        jPanelTablaCancionesLayout.setHorizontalGroup(
            jPanelTablaCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaCancionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTablaCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanelTablaCancionesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblBuscarCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxFiltrarCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFiltrarCanciones)))
                .addContainerGap())
        );
        jPanelTablaCancionesLayout.setVerticalGroup(
            jPanelTablaCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaCancionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTablaCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxFiltrarCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBuscarCanciones)
                    .addComponent(btnFiltrarCanciones))
                .addGap(6, 6, 6)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelEstructuraCanciones.setBackground(new java.awt.Color(0, 153, 153));
        jPanelEstructuraCanciones.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), null));
        jPanelEstructuraCanciones.setBounds(new java.awt.Rectangle(0, 0, 247, 178));
        jPanelEstructuraCanciones.setMaximumSize(new java.awt.Dimension(247, 178));

        btnColumnaNuevaCancion.setText("Añadir columna nueva");
        btnColumnaNuevaCancion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColumnaNuevaCancionActionPerformed(evt);
            }
        });

        btnDeleteColumnaCancion.setText("Eliminar columna existente");
        btnDeleteColumnaCancion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteColumnaCancionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEstructuraCancionesLayout = new javax.swing.GroupLayout(jPanelEstructuraCanciones);
        jPanelEstructuraCanciones.setLayout(jPanelEstructuraCancionesLayout);
        jPanelEstructuraCancionesLayout.setHorizontalGroup(
            jPanelEstructuraCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEstructuraCancionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEstructuraCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnColumnaNuevaCancion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDeleteColumnaCancion, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelEstructuraCancionesLayout.setVerticalGroup(
            jPanelEstructuraCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEstructuraCancionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnColumnaNuevaCancion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addComponent(btnDeleteColumnaCancion)
                .addContainerGap())
        );

        jPanelDatosAlbum1.setBackground(new java.awt.Color(0, 153, 153));
        jPanelDatosAlbum1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), null));
        jPanelDatosAlbum1.setBounds(new java.awt.Rectangle(0, 0, 626, 178));
        jPanelDatosAlbum1.setMaximumSize(new java.awt.Dimension(626, 178));

        lblTituloCancion.setText("TÍTULO");

        lblDuracionCancion.setText("DURACIÓN");

        btnInsertarCancion.setText("Insertar");
        btnInsertarCancion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarCancionActionPerformed(evt);
            }
        });

        btnModifCancion.setText("Modificar");
        btnModifCancion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifCancionActionPerformed(evt);
            }
        });

        btnDeleteCancion.setText("Eliminar");
        btnDeleteCancion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteCancionActionPerformed(evt);
            }
        });

        lblInfoCanciones.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lblInfoCanciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfoCanciones.setBounds(new java.awt.Rectangle(0, 0, 45, 16));
        lblInfoCanciones.setMinimumSize(new java.awt.Dimension(45, 16));

        lblAlbumCancion.setText("ALBUM");

        javax.swing.GroupLayout jPanelDatosAlbum1Layout = new javax.swing.GroupLayout(jPanelDatosAlbum1);
        jPanelDatosAlbum1.setLayout(jPanelDatosAlbum1Layout);
        jPanelDatosAlbum1Layout.setHorizontalGroup(
            jPanelDatosAlbum1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosAlbum1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatosAlbum1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblInfoCanciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelDatosAlbum1Layout.createSequentialGroup()
                        .addGroup(jPanelDatosAlbum1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblDuracionCancion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                            .addComponent(lblTituloCancion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblAlbumCancion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosAlbum1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldTituloCancion)
                            .addComponent(jTextFieldDuracionCancion)
                            .addComponent(jComboBoxAlbumCancion, 0, 332, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelDatosAlbum1Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(btnInsertarCancion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModifCancion)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(btnDeleteCancion)
                .addContainerGap())
        );
        jPanelDatosAlbum1Layout.setVerticalGroup(
            jPanelDatosAlbum1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosAlbum1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatosAlbum1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTituloCancion)
                    .addComponent(jTextFieldTituloCancion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatosAlbum1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDuracionCancion)
                    .addComponent(jTextFieldDuracionCancion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatosAlbum1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxAlbumCancion)
                    .addComponent(lblAlbumCancion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addComponent(lblInfoCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(jPanelDatosAlbum1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsertarCancion)
                    .addComponent(btnModifCancion)
                    .addComponent(btnDeleteCancion))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabCancionesLayout = new javax.swing.GroupLayout(tabCanciones);
        tabCanciones.setLayout(tabCancionesLayout);
        tabCancionesLayout.setHorizontalGroup(
            tabCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTablaCanciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabCancionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelEstructuraCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDatosAlbum1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        tabCancionesLayout.setVerticalGroup(
            tabCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCancionesLayout.createSequentialGroup()
                .addComponent(jPanelTablaCanciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelDatosAlbum1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelEstructuraCanciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Canciones", tabCanciones);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddUpdateAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUpdateAlbumActionPerformed
        //Ejecuta el método que añade la columna a la tabla ALBUM
        try {
            conexion.annadirColumnaAlbum(jTextFieldColumnaAlbum.getText(), jComboBoxTipoAlbum.getSelectedItem().toString());
            mostrarTablaAlbum();
            mostrarTablaCanciones();
            lblInfoAlbum.setText("Columna insertada correctamente.");
            jDialogColumnaNuevaAlbum.setVisible(false);
        } catch (Exception ex) {
            lblInfoAlbum.setText(ex.toString());
        }
        
    }//GEN-LAST:event_jButtonAddUpdateAlbumActionPerformed

    private void jButtonAddUpdateCancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUpdateCancionActionPerformed
        //Ejecuta el método que añade la columna a la tabla CANCIONES
        try {
            conexion.annadirColumnaCanciones(jTextFieldColumnaCancion.getText(), jComboBoxTipoCancion.getSelectedItem().toString());
            mostrarTablaAlbum();
            mostrarTablaCanciones();
            lblInfoCanciones.setText("Columna insertada correctamente.");
            jDialogColumnaNuevaCanciones.setVisible(false);
        } catch(Exception ex) {
            lblInfoCanciones.setText(ex.toString());
        }
        
    }//GEN-LAST:event_jButtonAddUpdateCancionActionPerformed

    private void btnInsertarAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertarAlbumActionPerformed
        //Insertar un nuevo álbum a la tabla Álbum
        try {
            conexion.insertarDatosAlbum(jTextFieldNombreAlbum.getText(), jTextFieldArtistaAlbum.getText(), jTextFieldAnnoAlbum.getText());
            mostrarTablaCanciones();
            mostrarTablaAlbum();
            lblInfoAlbum.setText("Insertado correctamente.");
            System.out.println("Insertado correctamente.");
        } catch(Exception ex) {
            lblInfoAlbum.setText(ex.toString());
            System.out.println(ex.toString());
        }
        
    }//GEN-LAST:event_btnInsertarAlbumActionPerformed

    private void btnColumnaNuevaAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColumnaNuevaAlbumActionPerformed
        //Abre el dialog para introducir una nueva columna a la tabla Álbum
        jDialogColumnaNuevaAlbum.setVisible(true);
    }//GEN-LAST:event_btnColumnaNuevaAlbumActionPerformed

    private void btnColumnaNuevaCancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColumnaNuevaCancionActionPerformed
        //Abre el dialog para introducir una nueva columna a la tabla Canciones
        jDialogColumnaNuevaCanciones.setVisible(true);
    }//GEN-LAST:event_btnColumnaNuevaCancionActionPerformed

    private void btnInsertarCancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertarCancionActionPerformed
        //Para insertar una nueva canción a la tabla Canciones.
        try {
            conexion.insertarDatosCancion(jTextFieldTituloCancion.getText(), 
                    jTextFieldDuracionCancion.getText(), jComboBoxAlbumCancion.getSelectedItem().toString());
            mostrarTablaCanciones();
            lblInfoCanciones.setText("Canción insertada correctamente.");
        } catch(Exception ex) {
            lblInfoCanciones.setText("Erro al insertar canción.");
        }
        
    }//GEN-LAST:event_btnInsertarCancionActionPerformed

    private void btnDeleteColumnaAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteColumnaAlbumActionPerformed
        //Abre el dialog que elimina una columna de la tabla Álbum.
        jDialogBorrarColumnaAlbum.setVisible(true);
    }//GEN-LAST:event_btnDeleteColumnaAlbumActionPerformed

    private void btnEliminarColumnaAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarColumnaAlbumActionPerformed
        //Para eliminar la columna seleccionada de la tabla álbum
        //Primero hay que confirmar que deseas borrarla
        int confirmed = JOptionPane.showConfirmDialog(null, 
        "¿Estás seguro de borrar la columa?", "Confirma",
        JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
          try {
            conexion.borrarColumna(jComboBoxColumnasTablaAlbum.getSelectedItem().toString());
            mostrarTablaAlbum();
            lblInfoAlbum.setText("Columna borrada correctamente.");
            jDialogBorrarColumnaAlbum.setVisible(false);
            } catch(Exception ex) {
                lblInfoAlbum.setText(ex.toString());
            }
        }
        else {
            jDialogBorrarColumnaAlbum.setVisible(false);
        }
    }//GEN-LAST:event_btnEliminarColumnaAlbumActionPerformed

    private void btnEliminarColumnaCancionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarColumnaCancionesActionPerformed
        //Para eliminar la columna seleccionada de la tabla álbum
        //Primero hay que confirmar que deseas borrarla
        int confirmed = JOptionPane.showConfirmDialog(null, 
        "¿Estás seguro de borrar la columa?", "Confirma",
        JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
          try {
            conexion.borrarColumna(jComboBoxColumnasTablaCanciones.getSelectedItem().toString());
            mostrarTablaCanciones();
            lblInfoAlbum.setText("Columna borrada correctamente.");
            jDialogBorrarColumnaCanciones.setVisible(false);
            } catch(Exception ex) {
                lblInfoAlbum.setText(ex.toString());
            }
        }
        else {
            jDialogBorrarColumnaCanciones.setVisible(false);
        }
    }//GEN-LAST:event_btnEliminarColumnaCancionesActionPerformed

    private void btnModifAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifAlbumActionPerformed
        //Para modificar un álbum
        try {
            int filaAlbum = jTableAlbum.getSelectedRow();
            conexion.modifAlbum(jTableAlbum.getValueAt(filaAlbum, 0).toString(), jTextFieldNombreAlbum.getText(),
                    jTextFieldArtistaAlbum.getText(), jTextFieldAnnoAlbum.getText());
            mostrarTablaAlbum();
            lblInfoAlbum.setText("Álbum modificado correctamente.");
        } catch (Exception ex) {
            lblInfoAlbum.setText("Error al modificar el álbum.");
        }
    }//GEN-LAST:event_btnModifAlbumActionPerformed

    private void btnModifCancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifCancionActionPerformed
        //Para modificar una canción
        try {
            int filaCancion = jTableCanciones.getSelectedRow();
            conexion.modifCancion(jTableCanciones.getValueAt(filaCancion, 0).toString(), 
                    jTextFieldTituloCancion.getText(), jTextFieldDuracionCancion.getText(), 
                    jComboBoxAlbumCancion.getSelectedItem().toString());
            mostrarTablaCanciones();
            lblInfoCanciones.setText("Canción modificada correctamente.");
        } catch (Exception ex) {
            lblInfoCanciones.setText("Error al modificar la canción.");
        }
    }//GEN-LAST:event_btnModifCancionActionPerformed

    private void btnDeleteAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAlbumActionPerformed
        //Para eliminar un registro de la tabla Álbum
        try {
            int filaAlbum = jTableAlbum.getSelectedRow();
            conexion.deleteAlbum(jTableAlbum.getValueAt(filaAlbum, 0).toString());
            mostrarTablaAlbum();
            lblInfoAlbum.setText("Álbum eliminado correctamente.");
        } catch(Exception ex) {
            lblInfoAlbum.setText("Ha sucedido un error al eliminar.");
        }
    }//GEN-LAST:event_btnDeleteAlbumActionPerformed

    private void btnDeleteCancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCancionActionPerformed
        //Para eliminar un registro de la tabla Canciones
        try {
            int filaCancion = jTableCanciones.getSelectedRow();
            conexion.deleteCancion(jTableCanciones.getValueAt(filaCancion, 0).toString());
            mostrarTablaCanciones();
            lblInfoAlbum.setText("Canción eliminado correctamente.");
        } catch(Exception ex) {
            lblInfoAlbum.setText("Ha sucedido un error al eliminar.");
        }
    }//GEN-LAST:event_btnDeleteCancionActionPerformed

    private void btnDeleteColumnaCancionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteColumnaCancionActionPerformed
        jDialogBorrarColumnaCanciones.setVisible(true);
    }//GEN-LAST:event_btnDeleteColumnaCancionActionPerformed

    private void btnFiltrarCancionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarCancionesActionPerformed
        //Filtra las canciones según el álbum seleccionado
        try {
            conexion.filtrarCancion(jComboBoxFiltrarCanciones.getSelectedItem().toString());
            mostrarTablaCanciones();
        } catch(Exception ex) {
            lblInfoAlbum.setText("Ha sucedido un error al filtrar.");
        }
    }//GEN-LAST:event_btnFiltrarCancionesActionPerformed

    private void mostrarTablaFiltroCanciones(String album) {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        try {
            //Hacemos la consulta
            ResultSet rs = (ResultSet) conexion.mostrarTabla("SELECT canciones.*, album.nombre "
                    + "FROM canciones, album WHERE canciones.album = album.idAlbum && album = " + album 
                    + "ORDER BY idCancion;");

            //Obtenemos los nombres de las columnas
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               modelo.setColumnIdentifiers(etiquetas);
            }
            while (rs.next()) {
                //Obtenemos los datos de las filas
                modelo.addRow(new Object[]{rs.getInt("idCancion"), rs.getString("titulo"),
                rs.getString("duracion"), rs.getInt("album") ,rs.getString("album.nombre")});
            }
            //Rellenamos la tabla con los datos.
            jTableCanciones.setModel(modelo);
            rs.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
    }
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
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnColumnaNuevaAlbum;
    private javax.swing.JButton btnColumnaNuevaCancion;
    private javax.swing.JButton btnDeleteAlbum;
    private javax.swing.JButton btnDeleteCancion;
    private javax.swing.JButton btnDeleteColumnaAlbum;
    private javax.swing.JButton btnDeleteColumnaCancion;
    private javax.swing.JButton btnEliminarColumnaAlbum;
    private javax.swing.JButton btnEliminarColumnaCanciones;
    private javax.swing.JButton btnFiltrarCanciones;
    private javax.swing.JButton btnInsertarAlbum;
    private javax.swing.JButton btnInsertarCancion;
    private javax.swing.JButton btnModifAlbum;
    private javax.swing.JButton btnModifCancion;
    private javax.swing.JButton jButtonAddUpdateAlbum;
    private javax.swing.JButton jButtonAddUpdateCancion;
    private javax.swing.JComboBox<String> jComboBoxAlbumCancion;
    private javax.swing.JComboBox<String> jComboBoxColumnasTablaAlbum;
    private javax.swing.JComboBox<String> jComboBoxColumnasTablaCanciones;
    private javax.swing.JComboBox<String> jComboBoxFiltrarCanciones;
    private javax.swing.JComboBox<String> jComboBoxTipoAlbum;
    private javax.swing.JComboBox<String> jComboBoxTipoCancion;
    private javax.swing.JDialog jDialogBorrarColumnaAlbum;
    private javax.swing.JDialog jDialogBorrarColumnaCanciones;
    private javax.swing.JDialog jDialogColumnaNuevaAlbum;
    private javax.swing.JDialog jDialogColumnaNuevaCanciones;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelDatosAlbum;
    private javax.swing.JPanel jPanelDatosAlbum1;
    private javax.swing.JPanel jPanelEstructuraAlbum;
    private javax.swing.JPanel jPanelEstructuraCanciones;
    private javax.swing.JPanel jPanelTablaAlbum;
    private javax.swing.JPanel jPanelTablaCanciones;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableAlbum;
    private javax.swing.JTable jTableCanciones;
    private javax.swing.JTextField jTextFieldAnnoAlbum;
    private javax.swing.JTextField jTextFieldArtistaAlbum;
    private javax.swing.JTextField jTextFieldColumnaAlbum;
    private javax.swing.JTextField jTextFieldColumnaCancion;
    private javax.swing.JTextField jTextFieldDuracionCancion;
    private javax.swing.JTextField jTextFieldNombreAlbum;
    private javax.swing.JTextField jTextFieldTituloCancion;
    private javax.swing.JLabel lblAlbumCancion;
    private javax.swing.JLabel lblAnnoAlbum;
    private javax.swing.JLabel lblArtistaAlbum;
    private javax.swing.JLabel lblBuscarCanciones;
    private javax.swing.JLabel lblColumnaCampo;
    private javax.swing.JLabel lblColumnaCampoCancion;
    private javax.swing.JLabel lblColumnaTipo;
    private javax.swing.JLabel lblColumnaTipo1;
    private javax.swing.JLabel lblDuracionCancion;
    private javax.swing.JLabel lblInfoAlbum;
    private javax.swing.JLabel lblInfoCanciones;
    private javax.swing.JLabel lblNombreAlbum;
    private javax.swing.JLabel lblTituloCancion;
    private javax.swing.JLabel lblTituloColumnaAlbum;
    private javax.swing.JLabel lblTituloColumnaAlbum1;
    private javax.swing.JPanel tabAlbum;
    private javax.swing.JPanel tabCanciones;
    // End of variables declaration//GEN-END:variables
}
