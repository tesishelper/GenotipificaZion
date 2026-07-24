/*
GenotipificaZion: Program designed to calculate the gentrification of qPCR results.
Copyright (C) <2026>  <Adolfo Zurita and Maximiliano Ayub>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

 */

package ventana;

// Para compilar en AndroidStudio usar en terminal ./gradlew clean jar

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

import java.io.BufferedReader;
import java.io.File;
import java.io.*;
import java.nio.channels.Selector;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Ventana extends JFrame {
	
	//Elementos de la ventana
	
	private Metodos m;
		
	private JButton btn1, btn2, btn3, btn4,bCalcular, bRestaurar, bGuardarConfiguracion,bExportarResultados, bReiniciar, bBorrarTablas;
	private JLabel  jln0, jln1, jln2, jln3, jln4;
	private JTextArea jta_Resultado;
	private JTextField jtf_nombre, jtf_alelo1,jtf_alelo2, jtf_alelo3, jtf_alelo4;
	private JTextField jtf_primerCiclo,jtf_segundoCiclo, jtf_promedioSuperior, jtf_umbral, jtf_EndRFU;
	private JTextField jtf_Media_NN_Fam,jtf_Media_NP_Fam,jtf_Media_PP_Fam;
	private JTextField jtf_SD_NN_Fam,jtf_SD_NP_Fam,jtf_SD_PP_Fam;
	private JTextField jtf_Media_NN_HEX,jtf_Media_NP_HEX,jtf_Media_PP_HEX;
	private JTextField jtf_SD_NN_HEX,jtf_SD_NP_HEX,jtf_SD_PP_HEX;

    // Agrega estas líneas arriba con tus otros JButtons, JTextFields, etc.
    private JTabbedPane tabsResultados;
    private JScrollPane scroll1;
    private JScrollPane scroll2;
    private JScrollPane scroll3;

    private JTable tabla1, tabla2;
	private DefaultTableModel model1;
	private DefaultTableModel model2;
    private DefaultTableModel model3;
    private JTable tabla3;


    //Variable para trabajar con los datos
	private File archivoS1, archivoS2,  archivoS3,archivoS4;
	private List<String> FamHeaders, TexasRedHeaders, HexHeaders,Cy5Headers;
	private List<List<Double>> FamList, TexasRedList, HexList, Cy5List;

	
	private JScrollPane scroll;
	
	//valores por defecto
	private final int umbralVacio = 9000;
    private final int endRFU = 150;
	private final int primerCiclo = 10;
	private final int segundoCiclo = 20;
	private final int ultimosCiclos = 5;
	private final double Media_NN_Fam = -3.14f;
	private final double Media_NP_Fam = 1.2f;
	private final double Media_PP_Fam = 5.6f;
	private final double SD_NN_Fam = 0.31f;
	private final double SD_NP_Fam = 0.72f;
	private final double SD_PP_Fam = 0.13f;
	private final double Media_NN_HEX = -2.45f;
	private final double Media_NP_HEX = 0.51f;
	private final double Media_PP_HEX = 4.91f;
	private final double SD_NN_HEX = 0.51f;
	private final double SD_NP_HEX = 0.45f;
	private final double SD_PP_HEX = 0.46F;

	private JLabel jln;

    // Definir las opciones
    String[] opcionesSondas = {"FAM","HEX", "VIC", "JOE", "Cy3", "Texas Red", "ROX", "Cy5", "Cy5.5"};

    // Crear el JComboBox
    JComboBox<String> comboSonda1 = new JComboBox<>(opcionesSondas);

    JComboBox<String> comboSonda2 = new JComboBox<>(opcionesSondas);
    JComboBox<String> comboSonda3 = new JComboBox<>(opcionesSondas);
    JComboBox<String> comboSonda4 = new JComboBox<>(opcionesSondas);
    private JComboBox<String> comboModoMarcadores;

    private String version = "v= 1.1";

// Aplicar el estilo para que combine con tu interfaz


    public Ventana() {
    	
    	
    	
        super("GenotipificaZión v= 1.1");
        
        m = new Metodos();
        
        FamHeaders = new ArrayList<>();
        TexasRedHeaders = new ArrayList<>();
        HexHeaders = new ArrayList<>();
        Cy5Headers = new ArrayList<>();
        
        FamList = new ArrayList<>();
        TexasRedList = new ArrayList<>();
        HexList = new ArrayList<>();
        Cy5List = new ArrayList<>();
        
        this.setSize(1100, 800);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        definirVentana();
        this.pack(); // O setSize
        this.setVisible(true);
        
        //inetenta cargar el archivo de configuracion guardado por el usuario
        cargarConfiguracion();
        
    }
    
   

    
    private void definirVentana() {
    	
    	// 1. Crear el menú bar
        JMenuBar menuBar = new JMenuBar();

        // 2. Crear los menús principales
        JMenu menuArchivo = new JMenu("Archivo");
        JMenu menuConfigurar = new JMenu("Configurar");
        JMenu menuAyuda = new JMenu("Ayuda");

        // 3. Crear los items del menú Archivo

        JMenuItem itemCalcular = new JMenuItem("Calcular");
        JMenuItem itemExportar = new JMenuItem("Exportar Resultado");
        JMenuItem itemSalir = new JMenuItem("Salir");
        

        
        
        //Crear items al menu Configurar
         JMenuItem itemRestaurar = new JMenuItem("Restaurar Configuración");
        JMenuItem itemGuardarConf = new JMenuItem("Guardar Configuración");
        
        //Agregar items al menu Configurar
        
        menuConfigurar.add(itemGuardarConf);
        menuConfigurar.add(itemRestaurar);
        
        

        // Agregar items al menú Archivo

        menuArchivo.addSeparator(); // Línea divisoria opcional antes de Salir
        menuArchivo.add(itemCalcular);
        menuArchivo.add(itemExportar);
        menuArchivo.addSeparator(); // Línea divisoria opcional antes de Salir
        menuArchivo.add(itemSalir);

        // 4. Crear los items del menú Ayuda
        JMenuItem itemObtenerAyuda = new JMenuItem("Ver ayuda");
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de este programa");

        // Agregar items al menú Ayuda
        menuAyuda.add(itemObtenerAyuda);
        menuAyuda.add(itemAcercaDe);

        // 5. Agregar los menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuConfigurar);
        menuBar.add(menuAyuda);

        // 6. Asignar la barra de menú a la ventana
        this.setJMenuBar(menuBar);
    	
        JPanel panelPrincipal = new JPanel(new BorderLayout(5, 5));
        this.add(panelPrincipal);

        // ====================== PANEL SUPERIOR CON PESTAÑAS ======================
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // ====================== PESTAÑA 1: CARGA DE DATOS ======================
        JPanel panelCarga = new JPanel(new GridBagLayout());
        panelCarga.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 6, 2, 5);
// Quitamos el fill horizontal global para controlarlo uno a uno
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST; // Alinea todo a la izquierda

// --- Título (Fila 0) ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4; // CAMBIO: Ahora son 3 columnas
        gbc.weightx = 1.0;
        JLabel jln0 = new JLabel("Utilizar los botones para cargar los archivos csv");
        jln0.setFont(new Font("Arial", Font.PLAIN, 18));
        panelCarga.add(jln0, gbc);




// --- Nombre del proyecto (Fila 1) ---
        // --- Nombre del proyecto (Fila 1) ---
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        jtf_nombre = new JTextField("Escriba el nombre aquí..."); // Este es el texto inicial (Hint)
        jtf_nombre.setForeground(Color.GRAY); // Color gris para que parezca un hint
        jtf_nombre.setPreferredSize(new Dimension(170, 30));
        jtf_nombre.setFont(new Font("Arial", Font.ITALIC, 14)); // Itálica para el hint
        jtf_nombre.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_nombre.setHorizontalAlignment(SwingConstants.CENTER);

// Lógica para que el texto desaparezca al hacer clic
        jtf_nombre.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jtf_nombre.getText().equals("Escriba el nombre aquí...")) {
                    jtf_nombre.setText("");
                    jtf_nombre.setForeground(Color.BLACK);
                    jtf_nombre.setFont(new Font("Arial", Font.BOLD, 16));
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jtf_nombre.getText().isEmpty()) {
                    jtf_nombre.setForeground(Color.GRAY);
                    jtf_nombre.setFont(new Font("Arial", Font.ITALIC, 14));
                    jtf_nombre.setText("Escriba el nombre aquí...");
                }
            }
        });

        panelCarga.add(jtf_nombre, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2; // Ocupa el resto de las columnas
        gbc.weightx = 0;
        jln = new JLabel("Nombre del proyecto (opcional)");
        jln.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCarga.add(jln, gbc);

// === FILA DE SONDA (Ejemplo Botón 1 - FAM) ===
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 0; // Columna 0: Tamaño fijo
        gbc.fill = GridBagConstraints.NONE;
        btn1 = new JButton("Cargar Sonda 1");
        btn1.setPreferredSize(new Dimension(170, 30));
        panelCarga.add(btn1, gbc);

        gbc.gridx = 1;

        jtf_alelo1 = new JTextField("Alelo 1"); // Este es el texto inicial (Hint)
        jtf_alelo1.setForeground(Color.GRAY); // Color gris para que parezca un hint
        jtf_alelo1.setPreferredSize(new Dimension(170, 30));
        jtf_alelo1.setFont(new Font("Arial", Font.ITALIC, 14)); // Itálica para el hint
        jtf_alelo1.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_alelo1.setHorizontalAlignment(SwingConstants.CENTER);

        // Lógica para que el texto desaparezca al hacer clic
        jtf_alelo1.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jtf_alelo1.getText().equals("Alelo 1")) {
                    jtf_alelo1.setText("");
                    jtf_alelo1.setForeground(Color.BLACK);
                    jtf_alelo1.setFont(new Font("Arial", Font.BOLD, 16));
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jtf_alelo1.getText().isEmpty()) {
                    jtf_alelo1.setForeground(Color.GRAY);
                    jtf_alelo1.setFont(new Font("Arial", Font.ITALIC, 14));
                    jtf_alelo1.setText("Alelo 1");
                }
            }
        });
        panelCarga.add(jtf_alelo1, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0; // Columna 1: Tamaño fijo para el combo
        comboSonda1.setPreferredSize(new Dimension(120, 30)); // Un poco más estrecho
        comboSonda1.setFont(new Font("Arial", Font.PLAIN, 14));
        comboSonda1.setSelectedIndex(0);//Fam
        panelCarga.add(comboSonda1, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Cambiar a NONE para que no se estire
        gbc.anchor = GridBagConstraints.WEST; // Asegura que se pegue a la izquie
        jln1 = new JLabel("Archivo 1: Ninguno");
        jln1.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCarga.add(jln1, gbc);

        // === Botón 2 - Texas Red ===
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        btn2 = new JButton("Cargar Sonda 2");
        btn2.setPreferredSize(new Dimension(170, 30));
        panelCarga.add(btn2, gbc);

        gbc.gridx = 1;

        jtf_alelo2 = new JTextField("Control (*X)"); // Este es el texto inicial (Hint)
        jtf_alelo2.setForeground(Color.black); // Color gris para que parezca un hint
        jtf_alelo2.setPreferredSize(new Dimension(170, 30));
        jtf_alelo2.setFont(new Font("Arial", Font.ITALIC, 14)); // Itálica para el hint
        jtf_alelo2.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_alelo2.setHorizontalAlignment(SwingConstants.CENTER);
        jtf_alelo2.setEnabled(false);
        // Lógica para que el texto desaparezca al hacer clic
        /*
        jtf_alelo2.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jtf_alelo2.getText().equals("Control (*X)")) {
                    jtf_alelo2.setText("");
                    jtf_alelo2.setForeground(Color.BLACK);
                    jtf_alelo2.setFont(new Font("Arial", Font.BOLD, 16));
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jtf_alelo2.getText().isEmpty()) {
                    jtf_alelo2.setForeground(Color.GRAY);
                    jtf_alelo2.setFont(new Font("Arial", Font.ITALIC, 14));
                    jtf_alelo2.setText("Control (*X)");
                }
            }
        });

         */
        panelCarga.add(jtf_alelo2, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0; // Cambiado a 0 para que no se estire
        comboSonda2.setPreferredSize(new Dimension(120, 30));
        comboSonda2.setFont(new Font("Arial", Font.PLAIN, 14));
        comboSonda2.setSelectedIndex(5);//TexasRed
        panelCarga.add(comboSonda2, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Cambiar a NONE para que no se estire
        gbc.anchor = GridBagConstraints.WEST; // Asegura que se pegue a la izquie
        jln2 = new JLabel("Archivo 2: Ninguno");
        jln2.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCarga.add(jln2, gbc);

        // === Botón 3 - HEX ===
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        btn3 = new JButton("Cargar Sonda 3");
        btn3.setPreferredSize(new Dimension(170, 30));
        panelCarga.add(btn3, gbc);

        gbc.gridx = 1;
        jtf_alelo3 = new JTextField("Alelo 2"); // Este es el texto inicial (Hint)
        jtf_alelo3.setForeground(Color.GRAY); // Color gris para que parezca un hint
        jtf_alelo3.setPreferredSize(new Dimension(170, 30));
        jtf_alelo3.setFont(new Font("Arial", Font.ITALIC, 14)); // Itálica para el hint
        jtf_alelo3.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_alelo3.setHorizontalAlignment(SwingConstants.CENTER);

        // Lógica para que el texto desaparezca al hacer clic
        jtf_alelo3.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jtf_alelo3.getText().equals("Alelo 2")) {
                    jtf_alelo3.setText("");
                    jtf_alelo3.setForeground(Color.BLACK);
                    jtf_alelo3.setFont(new Font("Arial", Font.BOLD, 16));
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jtf_alelo3.getText().isEmpty()) {
                    jtf_alelo3.setForeground(Color.GRAY);
                    jtf_alelo3.setFont(new Font("Arial", Font.ITALIC, 14));
                    jtf_alelo3.setText("Alelo 2");
                }
            }
        });
        panelCarga.add(jtf_alelo3, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0; // Cambiado a 0
        comboSonda3.setPreferredSize(new Dimension(120, 30));
        comboSonda3.setFont(new Font("Arial", Font.PLAIN, 14));
        comboSonda3.setSelectedIndex(1);//Hex
        panelCarga.add(comboSonda3, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Cambiar a NONE para que no se estire
        gbc.anchor = GridBagConstraints.WEST; // Asegura que se pegue a la izquie
        jln3 = new JLabel("Archivo 3: Ninguno");
        jln3.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCarga.add(jln3, gbc);

        // === Botón 4 - Cy5 ===
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        btn4 = new JButton("Cargar Sonda 4");
        btn4.setPreferredSize(new Dimension(170, 30));
        panelCarga.add(btn4, gbc);

        gbc.gridx = 1;
        jtf_alelo4 = new JTextField("Control (*X)"); // Este es el texto inicial (Hint)
        jtf_alelo4.setForeground(Color.black); // Color gris para que parezca un hint
        jtf_alelo4.setPreferredSize(new Dimension(170, 30));
        jtf_alelo4.setFont(new Font("Arial", Font.ITALIC, 14)); // Itálica para el hint
        jtf_alelo4.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_alelo4.setHorizontalAlignment(SwingConstants.CENTER);
        jtf_alelo4.setEnabled(false);
        /*
        // Lógica para que el texto desaparezca al hacer clic
        jtf_alelo4.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jtf_alelo4.getText().equals("Alelo 4")) {
                    jtf_alelo4.setText("");
                    jtf_alelo4.setForeground(Color.BLACK);
                    jtf_alelo4.setFont(new Font("Arial", Font.BOLD, 16));
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jtf_alelo4.getText().isEmpty()) {
                    jtf_alelo4.setForeground(Color.GRAY);
                    jtf_alelo4.setFont(new Font("Arial", Font.ITALIC, 14));
                    jtf_alelo4.setText("Alelo 4");
                }
            }
        });

         */
        panelCarga.add(jtf_alelo4, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0; // Cambiado a 0
        comboSonda4.setPreferredSize(new Dimension(120, 30));
        comboSonda4.setFont(new Font("Arial", Font.PLAIN, 14));
        comboSonda4.setSelectedIndex(7); // Cy5
        panelCarga.add(comboSonda4, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Cambiar a NONE para que no se estire
        gbc.anchor = GridBagConstraints.WEST; // Asegura que se pegue a la izquie
        jln4 = new JLabel("Archivo 4: Ninguno");
        jln4.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCarga.add(jln4, gbc);

        // === FILA FINAL DE BOTONES ACCIÓN (Fila 6) ===
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 6, 5, 5); // Un poco más de espacio superior

        // Botón Calcular
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        bCalcular = new JButton("Calcular");
        bCalcular.setPreferredSize(new Dimension(170, 30));
        panelCarga.add(bCalcular, gbc);

        // Botón Borrar Resultados
        gbc.gridx = 1;
        gbc.weightx = 0;
        bReiniciar = new JButton("Borrar Todo");
        bReiniciar.setPreferredSize(new Dimension(170, 30)); // Mismo ancho que los demás
        panelCarga.add(bReiniciar, gbc);

        // Botón Exportar
        gbc.gridx = 2;
        gbc.weightx = 0; // No queremos que el botón se estire hasta el final
        gbc.anchor = GridBagConstraints.WEST; // Pegado a la izquierda de su columna
        bExportarResultados = new JButton("Exportar Resultados");
        bExportarResultados.setPreferredSize(new Dimension(170, 30));
        panelCarga.add(bExportarResultados, gbc);

        // Agregamos el Selector de Marcadores en la Columna 3
        gbc.gridx = 3;
        gbc.gridwidth = 1; // Solo ocupa una celda
        gbc.weightx = 1.0; // Mantiene el peso para que el resto de la ventana se alinee aquí
        gbc.fill = GridBagConstraints.NONE; // Cambiar a NONE para que no se estire
        gbc.anchor = GridBagConstraints.WEST; // Asegura que se pegue a la izquie

        String[] modos = {" Dos Sondas", " Cuatro Sondas"};
        comboModoMarcadores = new JComboBox<>(modos);
        comboModoMarcadores.setSelectedIndex(1);
        comboModoMarcadores.setPreferredSize(new Dimension(160, 30));
        comboModoMarcadores.setFont(new Font("Arial", Font.BOLD, 13));

        panelCarga.add(comboModoMarcadores, gbc);

        comboModoMarcadores.addActionListener(e -> {
            actualizarEstadoFilasSondas();
        });

        tabbedPane.addTab("Carga de datos", panelCarga);



     // ====================== PESTAÑA 2: SETTINGS ======================
        JPanel panelSettings = new JPanel(new GridLayout(1,3));

        panelSettings.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);     // Espaciado más cómodo
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;



        //PANEL PARA SETTINGS GENERALES

        JPanel panelSettingGeneral = new JPanel(new GridBagLayout());
        	TitledBorder titledBorder = BorderFactory.createTitledBorder(" Configuración General ");
        				titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));

        panelSettingGeneral.setBorder(titledBorder);
        panelSettings.add(panelSettingGeneral);

        // ==================== PRIMERA FILA - Basal Inferior ====================
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx= 1;
        jtf_primerCiclo = new JTextField("10");
        jtf_primerCiclo.setPreferredSize(new Dimension(60, 28));
        jtf_primerCiclo.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_primerCiclo.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2)); // Borde azul bonito
        jtf_primerCiclo.setHorizontalAlignment(SwingConstants.CENTER);// Centrado el número
        panelSettingGeneral.add(jtf_primerCiclo, gbc);

        permitirSoloNumerosEnteros(jtf_primerCiclo, 2);

        gbc.gridx = 1;
        JLabel jl2 = new JLabel("Primeros ciclos de la reacción");
        jl2.setFont(new Font("Arial", Font.PLAIN, 16));
        panelSettingGeneral.add(jl2, gbc);

        gbc.gridy = 0;
        gbc.gridx = 2;
        jtf_segundoCiclo = new JTextField("");
        jtf_segundoCiclo.setPreferredSize(new Dimension(60, 28));
        jtf_segundoCiclo.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_segundoCiclo.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2)); // Borde azul bonito
        jtf_segundoCiclo.setHorizontalAlignment(SwingConstants.CENTER);// Centrado el número
        panelSettingGeneral.add(jtf_segundoCiclo, gbc);

        permitirSoloNumerosEnteros(jtf_segundoCiclo, 2);



        // ==================== SEGUNDA FILA - Basal Superior ====================
        gbc.gridy = 1;

        gbc.gridx = 0;
        jtf_promedioSuperior = new JTextField("5");
        jtf_promedioSuperior.setPreferredSize(new Dimension(60, 28));
        jtf_promedioSuperior.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_promedioSuperior.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_promedioSuperior.setHorizontalAlignment(SwingConstants.CENTER);
        panelSettingGeneral.add(jtf_promedioSuperior, gbc);

        gbc.gridx = 1;
        JLabel jl4 = new JLabel("Ultimos ciclos de la reacción");
        jl4.setFont(new Font("Arial", Font.PLAIN, 16));
        panelSettingGeneral.add(jl4, gbc);

        permitirSoloNumerosEnteros(jtf_promedioSuperior, 2);


     // ==================== TERCERA FILA - UMBRAL POCILLO BASIO ====================

        gbc.gridy = 2;
        gbc.gridx = 0;
        jtf_umbral = new JTextField("900");
        jtf_umbral.setPreferredSize(new Dimension(60, 28));
        jtf_umbral.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_umbral.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_umbral.setHorizontalAlignment(SwingConstants.CENTER);
        panelSettingGeneral.add(jtf_umbral, gbc);

        gbc.gridx = 1;
        JLabel jl5 = new JLabel("Umbral pocillos vacíos");
        jl5.setFont(new Font("Arial", Font.PLAIN, 16));
        panelSettingGeneral.add(jl5, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        jtf_EndRFU = new JTextField("150");
        jtf_EndRFU.setPreferredSize(new Dimension(60, 28));
        jtf_EndRFU.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_EndRFU.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_EndRFU.setHorizontalAlignment(SwingConstants.CENTER);
        panelSettingGeneral.add(jtf_EndRFU, gbc);

        gbc.gridx = 1;
        JLabel jlEndRFU = new JLabel("EndRFU");
        jlEndRFU.setFont(new Font("Arial", Font.PLAIN, 16));
        panelSettingGeneral.add(jlEndRFU, gbc);




        permitirSoloNumerosEnteros(jtf_umbral, 6);
        permitirSoloNumerosEnteros(jtf_EndRFU, 6);


        //PANEL PARA POBLACION FAM/TEXASRED

        JPanel panelPoblacionFamTexasRed = new JPanel(new GridBagLayout());
    	TitledBorder titledBorder2 = BorderFactory.createTitledBorder(" Población Sonda 1/Sonda 2 ");
    				titledBorder2.setTitleFont(new Font("Arial", Font.BOLD, 16));

    				panelPoblacionFamTexasRed.setBorder(titledBorder2);
    	panelSettings.add(panelPoblacionFamTexasRed);

    	gbc.gridx = 1;
    	gbc.gridy = 0;

    	 JLabel media = new JLabel("Media");
         media.setFont(new Font("Arial", Font.PLAIN, 16));
         panelPoblacionFamTexasRed.add(media, gbc);

         gbc.gridx = 2;
         JLabel sd = new JLabel("SD");
         sd.setFont(new Font("Arial", Font.PLAIN, 16));
         panelPoblacionFamTexasRed.add(sd, gbc);

        gbc.gridx = 0;
     	gbc.gridy = 1;
        JLabel jl6 = new JLabel("NEG/NEG");
        jl6.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPoblacionFamTexasRed.add(jl6, gbc);

        gbc.gridx = 1;
        jtf_Media_NN_Fam = new JTextField(String.valueOf(Media_NN_Fam));
        jtf_Media_NN_Fam.setPreferredSize(new Dimension(60, 28));
        jtf_Media_NN_Fam.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_Media_NN_Fam.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_Media_NN_Fam.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionFamTexasRed.add(jtf_Media_NN_Fam, gbc);

        permitirSoloNumerosDecimales(jtf_Media_NN_Fam);

        gbc.gridx = 2;
        jtf_SD_NN_Fam = new JTextField(String.valueOf(SD_NN_Fam));
        jtf_SD_NN_Fam.setPreferredSize(new Dimension(60, 28));
        jtf_SD_NN_Fam.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_SD_NN_Fam.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_SD_NN_Fam.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionFamTexasRed.add(jtf_SD_NN_Fam, gbc);

        permitirSoloNumerosDecimales(jtf_SD_NN_Fam);

        gbc.gridx = 0;
    	gbc.gridy = 2;
        JLabel jl7 = new JLabel("POS/NEG");
        jl7.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPoblacionFamTexasRed.add(jl7, gbc);

        gbc.gridx = 1;
        jtf_Media_NP_Fam = new JTextField(String.valueOf(Media_NP_Fam));
        jtf_Media_NP_Fam.setPreferredSize(new Dimension(60, 28));
        jtf_Media_NP_Fam.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_Media_NP_Fam.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_Media_NP_Fam.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionFamTexasRed.add(jtf_Media_NP_Fam, gbc);

        permitirSoloNumerosDecimales(jtf_Media_NP_Fam);

        gbc.gridx = 2;
        jtf_SD_NP_Fam = new JTextField(String.valueOf(SD_NP_Fam));
        jtf_SD_NP_Fam.setPreferredSize(new Dimension(60, 28));
        jtf_SD_NP_Fam.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_SD_NP_Fam.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_SD_NP_Fam.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionFamTexasRed.add(jtf_SD_NP_Fam, gbc);

        permitirSoloNumerosDecimales(jtf_SD_NP_Fam);


        gbc.gridx = 0;
    	gbc.gridy = 3;
        JLabel jl8 = new JLabel("POS/POS");
        jl8.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPoblacionFamTexasRed.add(jl8, gbc);

        gbc.gridx = 1;
        jtf_Media_PP_Fam = new JTextField(String.valueOf(Media_PP_Fam));
        jtf_Media_PP_Fam.setPreferredSize(new Dimension(60, 28));
        jtf_Media_PP_Fam.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_Media_PP_Fam.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_Media_PP_Fam.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionFamTexasRed.add(jtf_Media_PP_Fam, gbc);

        permitirSoloNumerosDecimales(jtf_Media_PP_Fam);

        gbc.gridx = 2;
        jtf_SD_PP_Fam = new JTextField(String.valueOf(SD_PP_Fam));
        jtf_SD_PP_Fam.setPreferredSize(new Dimension(60, 28));
        jtf_SD_PP_Fam.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_SD_PP_Fam.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_SD_PP_Fam.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionFamTexasRed.add(jtf_SD_PP_Fam, gbc);

        permitirSoloNumerosDecimales(jtf_SD_PP_Fam);


      //PANEL PARA POBLACION HEX/Cy5


        JPanel panelPoblacionHexCy5 = new JPanel(new GridBagLayout());
    	TitledBorder titledBorder3 = BorderFactory.createTitledBorder(" Población Sonda 3/Sonda 4 ");
    				titledBorder3.setTitleFont(new Font("Arial", Font.BOLD, 16));

    				panelPoblacionHexCy5.setBorder(titledBorder3);
    	panelSettings.add(panelPoblacionHexCy5);

    	gbc.gridx = 1;
    	gbc.gridy = 0;

    	 JLabel media2 = new JLabel("Media");
         media2.setFont(new Font("Arial", Font.PLAIN, 16));
         panelPoblacionHexCy5.add(media2, gbc);

         gbc.gridx = 2;
         JLabel sd2 = new JLabel("SD");
         sd2.setFont(new Font("Arial", Font.PLAIN, 16));
         panelPoblacionHexCy5.add(sd2, gbc);

        gbc.gridx = 0;
     	gbc.gridy = 1;
        JLabel jl9 = new JLabel("NEG/NEG");
        jl9.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPoblacionHexCy5.add(jl9, gbc);

        gbc.gridx = 1;
        jtf_Media_NN_HEX = new JTextField(String.valueOf(Media_NN_HEX));
        jtf_Media_NN_HEX.setPreferredSize(new Dimension(60, 28));
        jtf_Media_NN_HEX.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_Media_NN_HEX.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_Media_NN_HEX.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionHexCy5.add(jtf_Media_NN_HEX, gbc);

        permitirSoloNumerosDecimales(jtf_Media_NN_HEX);

        gbc.gridx = 2;
        jtf_SD_NN_HEX = new JTextField(String.valueOf(SD_NN_HEX));
        jtf_SD_NN_HEX.setPreferredSize(new Dimension(60, 28));
        jtf_SD_NN_HEX.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_SD_NN_HEX.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_SD_NN_HEX.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionHexCy5.add(jtf_SD_NN_HEX, gbc);

        permitirSoloNumerosDecimales(jtf_SD_NN_HEX);

        gbc.gridx = 0;
    	gbc.gridy = 2;
        JLabel jl10 = new JLabel("POS/NEG");
        jl10.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPoblacionHexCy5.add(jl10, gbc);

        gbc.gridx = 1;
        jtf_Media_NP_HEX = new JTextField(String.valueOf(Media_NP_HEX));
        jtf_Media_NP_HEX.setPreferredSize(new Dimension(60, 28));
        jtf_Media_NP_HEX.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_Media_NP_HEX.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_Media_NP_HEX.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionHexCy5.add(jtf_Media_NP_HEX, gbc);

        permitirSoloNumerosDecimales(jtf_Media_NP_HEX);

        gbc.gridx = 2;
        jtf_SD_NP_HEX = new JTextField(String.valueOf(SD_NP_HEX));
        jtf_SD_NP_HEX.setPreferredSize(new Dimension(60, 28));
        jtf_SD_NP_HEX.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_SD_NP_HEX.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_SD_NP_HEX.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionHexCy5.add(jtf_SD_NP_HEX, gbc);

        permitirSoloNumerosDecimales(jtf_SD_NP_HEX);


        gbc.gridx = 0;
    	gbc.gridy = 3;
        JLabel jl11 = new JLabel("POS/POS");
        jl11.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPoblacionHexCy5.add(jl11, gbc);

        gbc.gridx = 1;
        jtf_Media_PP_HEX = new JTextField(String.valueOf(Media_PP_HEX));
        jtf_Media_PP_HEX.setPreferredSize(new Dimension(60, 28));
        jtf_Media_PP_HEX.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_Media_PP_HEX.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_Media_PP_HEX.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionHexCy5.add(jtf_Media_PP_HEX, gbc);

        permitirSoloNumerosDecimales(jtf_Media_PP_HEX);

        gbc.gridx = 2;
        jtf_SD_PP_HEX = new JTextField(String.valueOf(SD_PP_HEX));
        jtf_SD_PP_HEX.setPreferredSize(new Dimension(60, 28));
        jtf_SD_PP_HEX.setFont(new Font("Arial", Font.BOLD, 16));
        jtf_SD_PP_HEX.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jtf_SD_PP_HEX.setHorizontalAlignment(SwingConstants.CENTER);
        panelPoblacionHexCy5.add(jtf_SD_PP_HEX, gbc);

        permitirSoloNumerosDecimales(jtf_SD_PP_HEX);




        // ==================== ESPACIADOR ====================
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        //panelSettings.add(new JLabel(""));

     // Botón Restaurar
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        bGuardarConfiguracion = new JButton("Guardar Configuración");
        bGuardarConfiguracion.setPreferredSize(new Dimension(170, 30));
        //panelSettings.add(bGuardarConfiguracion, gbc);


        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 0;
        bRestaurar = new JButton("Restaurar Configuración");
        bRestaurar.setPreferredSize(new Dimension(170, 30));
       // panelSettings.add(bRestaurar, gbc);



        tabbedPane.addTab("Configuración", panelSettings);
        // Añadir las pestañas al panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(tabbedPane, BorderLayout.CENTER);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ====================== PANEL INFERIOR (Resultado) ======================
        JPanel panelInferior = new JPanel(new BorderLayout());
        TitledBorder borde = BorderFactory.createTitledBorder(" Resultado ");
        borde.setTitleFont(new Font("Arial", Font.BOLD, 16));
        panelInferior.setBorder(borde);

// 1. Creamos el JTabbedPane para las pestañas de resultados
        tabsResultados = new JTabbedPane();
        tabsResultados.setFont(new Font("Arial", Font.PLAIN, 14));

// Inicializamos modelos (añadimos model3)
        model1 = new DefaultTableModel(9, 13);
        model2 = new DefaultTableModel(9, 13);
        model3 = new DefaultTableModel(9, 13); // Asegúrate de declarar model3 arriba en la clase

        tabla1 = new JTable(model1);
        tabla2 = new JTable(model2);
        tabla3 = new JTable(model3); // Asegúrate de declarar tabla3 arriba en la clase

// Configuración común para las tablas
        tabla1.setRowHeight(25);
        tabla2.setRowHeight(25);
        tabla3.setRowHeight(25);

// 2. Creamos los ScrollPanes individuales para cada tabla
         scroll1 = new JScrollPane(tabla1);
         scroll2 = new JScrollPane(tabla2);
         scroll3 = new JScrollPane(tabla3);

// 3. Añadimos cada ScrollPane al TabbedPane con su título correspondiente
        tabsResultados.addTab("Resultado Sonda 1/Sonda 2", scroll1);
        tabsResultados.addTab("Resultado Sonda 3/Sonda 4", scroll2);
        tabsResultados.addTab("Resultado GENOTIPOS", scroll3); // Título para la tercera pestaña

// 4. Añadimos el TabbedPane al panelInferior
        panelInferior.add(tabsResultados, BorderLayout.CENTER);

        // ====================== NUEVO: BOTÓN DE BORRADO ABAJO ======================
        JPanel panelBotonAbajo = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Alineado a la derecha
        bBorrarTablas = new JButton("Borrar Resultados");
        bBorrarTablas.setPreferredSize(new Dimension(220, 30));
        bBorrarTablas.setFont(new Font("Arial", Font.BOLD, 13));
        bBorrarTablas.setForeground(new Color(150, 0, 0)); // Color de texto rojizo para indicar acción de borrar

        panelBotonAbajo.add(bBorrarTablas);
        panelInferior.add(panelBotonAbajo, BorderLayout.SOUTH); // Se coloca debajo de las tablas
// ==================================


// Finalmente añadimos el panelInferior al panelPrincipal
        panelPrincipal.add(panelInferior, BorderLayout.CENTER);

        // Al final de panelSettings
        gbc.gridx = 0;
        gbc.gridy = 10; // Una fila bien abajo
        gbc.weighty = 1.0; // Este se lleva el espacio sobrante verticalmente
       // panelSettings.add(new JLabel(""), gbc);
    

        // ==================== LISTENER DE LOS BOTONES ====================
        btn1.addActionListener(e -> {
            cargarArchivoSonda1(comboSonda1);
        });
        
        btn2.addActionListener(e -> {
            cargarArchivoSonda2(comboSonda2);
        });
        
        btn3.addActionListener(e -> {
            cargarArchivoSonda3(comboSonda3);
        });
        
        btn4.addActionListener(e -> {
            cargarArchivoSonda4(comboSonda4);
            
        });
        
        bCalcular.addActionListener(e -> {calcular();
            
        });
        
        bRestaurar.addActionListener(e -> {
            
        	restaurar();
        		  });
        // Listener para el botón Borrar Todo
        bReiniciar.addActionListener(e -> {
            // 1. Ponemos los archivos en null
            archivoS1 = null;
            archivoS2 = null;
            archivoS3 = null;
            archivoS4 = null;

            // 2. Restauramos el texto de los JLabels
            jln1.setText("Archivo 1: Ninguno");
            jln2.setText("Archivo 2: Ninguno");
            jln3.setText("Archivo 3: Ninguno");
            jln4.setText("Archivo 4: Ninguno");

            // 3. Opcional: Limpiar también el nombre del proyecto
            jtf_nombre.setText("");

            // 4. Mostrar mensaje de confirmación al usuario
            JOptionPane.showMessageDialog(this, "Se han limpiado todas las cargas de archivos.");
        });
        
        bGuardarConfiguracion.addActionListener(e -> {guardarConfiguracion();});
        
        bExportarResultados.addActionListener(e -> {  m.exportarResultadosDesdeVentana(this);});
        bBorrarTablas.addActionListener(e -> {  m.limpiarTabla(model1, model2, model3);});
        
     // AGREGAR ACTION LIESTENR A LOS MENUE
        
       
         itemCalcular.addActionListener(e -> {calcular();           
        });
         itemRestaurar.addActionListener(e ->{restaurar();});
         itemExportar.addActionListener(e -> {   m.exportarResultadosDesdeVentana(this); });
         itemGuardarConf.addActionListener(e -> {guardarConfiguracion();});   
         
         itemAcercaDe.addActionListener(e -> {
        	    VentanaAcercaDe acercaDe = new VentanaAcercaDe(this);
        	    acercaDe.setVisible(true);
        	});
         
         itemObtenerAyuda.addActionListener(e -> {
        	    new Ventana_ayuda();
        	});

        itemSalir.addActionListener(e -> {
            System.exit(0);
        });
        
        
        
    }
    
    
    //METODOS
    
    public void restaurar() {
		int respuesta = JOptionPane.showConfirmDialog(
		        null,                                     // Componente padre (null para centrado)
		        "¿Estás seguro de que deseas cargar los valores originales?", // Mensaje
		        "Confirmar carga",                        // Título de la ventana
		        JOptionPane.YES_NO_OPTION                 // Tipo de botones
		    );

		    // 2. Verificar la respuesta
		    // JOptionPane.YES_OPTION es una constante que equivale a 0
		    if (respuesta == JOptionPane.YES_OPTION) {
		        
		    	cargarValoresPorDefecto();
		    	guardarConfiguracion();
		        
		    } else {
		        // Opcional: acción si el usuario selecciona "No" o cierra la ventana
		        System.out.println("Carga cancelada por el usuario.");
		    }
		  
    	
    }
    
    public void calcular() {

        if (comboModoMarcadores.getSelectedIndex() == 0) {



            if(archivoS1 != null && archivoS2 != null ) {

                System.out.println(" calculando dos marcadores");

                m.calcularDosMarcadores(this,FamHeaders, FamList, TexasRedList,jta_Resultado);

            }

            else {

                JOptionPane.showMessageDialog(this,
                        "Por favor cargue todos los archivos con extensión .csv",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);

            }

        }
    	
    	else if (comboModoMarcadores.getSelectedIndex() == 1) {

            System.out.println(" calculando cuatro marcadores");

            if(archivoS1 != null && archivoS2 != null && archivoS3 != null && archivoS4 != null ) {


                m.calcularCuatroMarcadores(this, FamHeaders, FamList, TexasRedList, HexList, Cy5List, jta_Resultado);

            }
    	
    	    else {
    		
    		 JOptionPane.showMessageDialog(this,
 	                "Por favor cargue todos los archivos con extensión .csv",
 	                "Error",
 	                JOptionPane.ERROR_MESSAGE);
 	            
    	}

    }


    }

    public void cargarArchivoSonda1(JComboBox jcb_sonda) {


                cargarArchivo(archivoS1, Objects.requireNonNull(jcb_sonda.getSelectedItem()).toString(),1);

    }

    public void cargarArchivoSonda2(JComboBox jcb_sonda) {
        cargarArchivo(archivoS2, Objects.requireNonNull(jcb_sonda.getSelectedItem()).toString(),2);
    }
    public void cargarArchivoSonda3(JComboBox jcb_sonda) {
        cargarArchivo(archivoS3, Objects.requireNonNull(jcb_sonda.getSelectedItem()).toString(),3);
    }

    public void cargarArchivoSonda4(JComboBox jcb_sonda) {
        cargarArchivo(archivoS4, Objects.requireNonNull(jcb_sonda.getSelectedItem()).toString(),4);
    }
    public void cargarArchivo(File archivo, String string, int pos) {


    	archivo = m.cargarArchivo("Seleccionar archivo "+ string,string, archivo, this);
    	 if (archivo != null) {
    		 //System.out.println("Ejecutando cargar archivo fam2");
         	String nombre = archivo.getName();
         	int limite = 60;
         	// Lógica para obtener los últimos 50 o el nombre completo si es más corto
         	String nombreAcortado = (nombre.length() > limite) 
         	                        ? "..." + nombre.substring(nombre.length() - limite) 
         	                        : nombre;
             switch (pos) {
                 case 1:
                     archivoS1 = archivo;
                     jln1.setText("Archivo 1: " + nombreAcortado);
                     m.leerCSV(FamHeaders, FamList, archivo,this);
                     break;
                 case 2:
                     archivoS2 = archivo;
                     jln2.setText("Archivo 2: " + nombreAcortado);
                     m.leerCSV(TexasRedHeaders, TexasRedList, archivo,this);
                     break;
                 case 3:
                     archivoS3 = archivo;
                     jln3.setText("Archivo 3: " + nombreAcortado);
                     m.leerCSV(HexHeaders, HexList, archivo,this);
                     break;
                 case 4:
                     archivoS4 = archivo;
                     jln4.setText("Archivo 4: " + nombreAcortado);
                     m.leerCSV(Cy5Headers, Cy5List, archivo,this);
                     break;
             }

             
           
             
                            
         }
    	 
    	
    }
  /*
    public void cargarArchivoS2(String string){ archivoS2 = m.cargarArchivo("Seleccionar archivo TexasRed","Texas Red",archivoTexasRed, this);

    if (archivoTexasRed != null) { 
    	String nombre = archivoTexasRed.getName();
    	int limite = 60;
    	// Lógica para obtener los últimos 50 o el nombre completo si es más corto
    	String nombreAcortado = (nombre.length() > limite) 
    	                        ? "..." + nombre.substring(nombre.length() - limite) 
    	                        : nombre;

    	jln2.setText("Archivo 1: " + nombreAcortado);
        
        m.leerCSV(TexasRedHeaders, TexasRedList, archivoTexasRed,this);
        
                   }
    
     	
    	
    	
    }
      public void cargarArchivoCy5() {
    	  
    	  archivoCy5 = m.cargarArchivo("Seleccionar archivo Cy5","Cy5", archivoCy5,this);

          if (archivoCy5 != null) {
          	String nombre = archivoCy5.getName();
          	int limite = 60;
          	// Lógica para obtener los últimos 50 o el nombre completo si es más corto
          	String nombreAcortado = (nombre.length() > limite) 
          	                        ? "..." + nombre.substring(nombre.length() - limite) 
          	                        : nombre;

          	jln4.setText("Archivo 1: " + nombreAcortado);
              m.leerCSV(Cy5Headers, Cy5List, archivoCy5, this);
              
             
          }
    	
    	
    }
    
    public void cargarArchivoHex() {
    	
    	archivoHEX = m.cargarArchivo("Seleccionar archivo HEX","HEX", archivoHEX,this);

        if (archivoHEX != null) {
        	
        	String nombre = archivoHEX.getName();
        	int limite = 60;
        	// Lógica para obtener los últimos n o el nombre completo si es más corto
        	String nombreAcortado = (nombre.length() > limite) 
        	                        ? "..." + nombre.substring(nombre.length() - limite) 
        	                        : nombre;

        	jln3.setText("Archivo 1: " + nombreAcortado);
            m.leerCSV(HexHeaders, HexList, archivoHEX,this);
        }
    }

   */
    
    public void cargarConfiguracion() {
        Properties props = new Properties();

        try (FileInputStream in = new FileInputStream("configuracion.properties")) {
            props.load(in);

            // Asignamos los valores (si la clave no existe, ponemos un string vacío)
            jtf_primerCiclo.setText(props.getProperty("primerciclo", ""));
            jtf_segundoCiclo.setText(props.getProperty("segundociclo", ""));
            jtf_promedioSuperior.setText(props.getProperty("promedioSuperior", ""));
            jtf_umbral.setText(props.getProperty("umbral", ""));
            jtf_EndRFU.setText(props.getProperty("endRFU", ""));
            jtf_Media_NN_Fam.setText(props.getProperty("mediaNNFam", ""));
            jtf_Media_NP_Fam.setText(props.getProperty("mediaNPFam", ""));
            jtf_Media_PP_Fam.setText(props.getProperty("mediaPPFam", ""));

            jtf_SD_NN_Fam.setText(props.getProperty("sdNNFam", ""));
            jtf_SD_NP_Fam.setText(props.getProperty("sdNPFam", ""));
            jtf_SD_PP_Fam.setText(props.getProperty("sdPPFam", ""));

            jtf_Media_NN_HEX.setText(props.getProperty("mediaNNHEX", ""));
            jtf_Media_NP_HEX.setText(props.getProperty("mediaNPHEX", ""));
            jtf_Media_PP_HEX.setText(props.getProperty("mediaPPHEX", ""));

            jtf_SD_NN_HEX.setText(props.getProperty("sdNNHEX", ""));
            jtf_SD_NP_HEX.setText(props.getProperty("sdNPHEX", ""));
            jtf_SD_PP_HEX.setText(props.getProperty("sdPPHEX", ""));

            // --- 3. Alelos (con manejo de formato/hints) ---
            restaurarCampoAlelo(jtf_alelo1, props.getProperty("alelo.1"), "Alelo 1");
           // restaurarCampoAlelo(jtf_alelo2, props.getProperty("alelo.2"), "Alelo 2");
            restaurarCampoAlelo(jtf_alelo3, props.getProperty("alelo.3"), "Alelo 2"); //Este en realidad es el alelo 2
            //restaurarCampoAlelo(jtf_alelo4, props.getProperty("alelo.4"), "Alelo 4");

            // --- 4. Selectores de Color (Combos) ---
            comboSonda1.setSelectedIndex(Integer.parseInt(props.getProperty("combo.1", "0")));
            comboSonda2.setSelectedIndex(Integer.parseInt(props.getProperty("combo.2", "1")));
            comboSonda3.setSelectedIndex(Integer.parseInt(props.getProperty("combo.3", "2")));
            comboSonda4.setSelectedIndex(Integer.parseInt(props.getProperty("combo.4", "3")));

            // --- 5. Modo de Marcadores (2 o 4) ---
            int modo = Integer.parseInt(props.getProperty("modo.marcadores", "1"));
            comboModoMarcadores.setSelectedIndex(modo);

            // Sincronizar la interfaz (habilitar/deshabilitar filas y pestañas)
            actualizarEstadoFilasSondas();

            System.out.println("Configuración cargada exitosamente.");
        } catch (IOException e) {


            JOptionPane.showMessageDialog(this,
                    "No se encontró archivo de configuración, se usarán valores por defecto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            cargarValoresPorDefecto();
            guardarConfiguracion();

            //  System.out.println("No se encontró archivo de configuración, se usarán valores por defecto.");


        }
    }

    // Método auxiliar indispensable para manejar los alelos correctamente
    private void restaurarCampoAlelo(JTextField campo, String valor, String hint) {
        if (valor != null && !valor.isEmpty() && !valor.equals(hint)) {
            campo.setText(valor);
            campo.setForeground(Color.BLACK);
            campo.setFont(new Font("Arial", Font.BOLD, 16));
        } else {
            campo.setText(hint);
            campo.setForeground(Color.GRAY);
            campo.setFont(new Font("Arial", Font.ITALIC, 14));
        }
    }




    public void guardarConfiguracion() {
        Properties props = new Properties();

        // Guardamos los valores en el objeto Properties
        props.setProperty("primerciclo", jtf_primerCiclo.getText());
        props.setProperty("segundociclo", jtf_segundoCiclo.getText());
        props.setProperty("promedioSuperior", jtf_promedioSuperior.getText());
        props.setProperty("umbral", jtf_umbral.getText());
        props.setProperty("endRFU", jtf_EndRFU.getText());
        props.setProperty("mediaNNFam", jtf_Media_NN_Fam.getText());
        props.setProperty("mediaNPFam", jtf_Media_NP_Fam.getText());
        props.setProperty("mediaPPFam", jtf_Media_PP_Fam.getText());

        props.setProperty("sdNNFam", jtf_SD_NN_Fam.getText());
        props.setProperty("sdNPFam", jtf_SD_NP_Fam.getText());
        props.setProperty("sdPPFam", jtf_SD_PP_Fam.getText());

        props.setProperty("mediaNNHEX", jtf_Media_NN_HEX.getText());
        props.setProperty("mediaNPHEX", jtf_Media_NP_HEX.getText());
        props.setProperty("mediaPPHEX", jtf_Media_PP_HEX.getText());

        props.setProperty("sdNNHEX", jtf_SD_NN_HEX.getText());
        props.setProperty("sdNPHEX", jtf_SD_NP_HEX.getText());
        props.setProperty("sdPPHEX", jtf_SD_PP_HEX.getText());

        // --- 3. Alelos (Evitar guardar los Hints "Alelo 1", "Alelo 2", etc.) ---
        props.setProperty("alelo.1", jtf_alelo1.getText().equals("Alelo 1") ? "" : jtf_alelo1.getText());
        //props.setProperty("Control (*X)", jtf_alelo2.getText().equals("Alelo 2") ? "" : jtf_alelo2.getText());
        props.setProperty("alelo.3", jtf_alelo3.getText().equals("Alelo 2") ? "" : jtf_alelo3.getText());
        //props.setProperty("alelo.4", jtf_alelo4.getText().equals("Alelo 4") ? "" : jtf_alelo4.getText());

        // --- 4. Selectores de Color (Combos de las Sondas) ---
        props.setProperty("combo.1", String.valueOf(comboSonda1.getSelectedIndex()));
        props.setProperty("combo.2", String.valueOf(comboSonda2.getSelectedIndex()));
        props.setProperty("combo.3", String.valueOf(comboSonda3.getSelectedIndex()));
        props.setProperty("combo.4", String.valueOf(comboSonda4.getSelectedIndex()));

        // --- 5. Modo de Marcadores (2 o 4) ---
        props.setProperty("modo.marcadores", String.valueOf(comboModoMarcadores.getSelectedIndex()));


        // Escribimos al archivo
        try (FileOutputStream out = new FileOutputStream("configuracion.properties")) {
            props.store(out, "Configuracion de la aplicacion");


            JOptionPane.showMessageDialog(this,
                    "Configuración guardada exitosamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            //System.out.println("Configuración guardada exitosamente.");
        } catch (IOException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al guardar la configuracion!!!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void actualizarEstadoFilasSondas() {
        if (comboModoMarcadores == null || tabsResultados == null) return;

        // USAMOS EL ÍNDICE (0 para Dos, 1 para Cuatro)
        int seleccion = comboModoMarcadores.getSelectedIndex();
        boolean modoCuatro = (seleccion == 1);

        System.out.println("DEBUG: Selección index = " + seleccion + " | Modo Cuatro = " + modoCuatro);

        // Habilitar/Deshabilitar controles
        btn3.setEnabled(modoCuatro);
        jtf_alelo3.setEnabled(modoCuatro);
        comboSonda3.setEnabled(modoCuatro);
        jln3.setEnabled(modoCuatro);
        btn4.setEnabled(modoCuatro);
        //jtf_alelo4.setEnabled(modoCuatro);
        comboSonda4.setEnabled(modoCuatro);
        jln4.setEnabled(modoCuatro);

        if (modoCuatro) {
            if (scroll2 == null) {
                System.out.println("ERROR CRÍTICO: scroll2 es NULL");
            } else {
                int indexActual = tabsResultados.indexOfComponent(scroll2);
                if (indexActual == -1) {
                    System.out.println("Insertando pestaña en posición 1...");
                    // Forzamos la inserción en la posición 1
                    tabsResultados.insertTab("Resultado Sonda 3/Sonda 4", null, scroll2, "Resultados para HEX y Cy5", 1);
                    tabsResultados.setSelectedIndex(0); // Mantenemos el foco en la primera pestaña para que no salte
                }
            }
        } else {
            int index = tabsResultados.indexOfComponent(scroll2);
            if (index != -1) {
                System.out.println("Quitando pestaña en índice: " + index);
                tabsResultados.removeTabAt(index);
            }
        }

        tabsResultados.revalidate();
        tabsResultados.repaint();
    }



    public void cargarValoresPorDefecto() {
        // --- Bloque original (No tocar) ---
        // General (Números enteros, se mantienen igual)
        jtf_umbral.setText(String.valueOf(umbralVacio));
        jtf_EndRFU.setText(String.valueOf(endRFU));
        jtf_primerCiclo.setText(String.valueOf(primerCiclo));
        jtf_segundoCiclo.setText(String.valueOf(segundoCiclo));
        jtf_promedioSuperior.setText(String.valueOf(ultimosCiclos));

        // Valores (Fam) - Formateados a 2 decimales
        jtf_Media_NN_Fam.setText(String.format(Locale.US, "%.2f", Media_NN_Fam));
        jtf_Media_NP_Fam.setText(String.format(Locale.US, "%.2f", Media_NP_Fam));
        jtf_Media_PP_Fam.setText(String.format(Locale.US, "%.2f", Media_PP_Fam));

        jtf_SD_NN_Fam.setText(String.format(Locale.US, "%.2f", SD_NN_Fam));
        jtf_SD_NP_Fam.setText(String.format(Locale.US, "%.2f", SD_NP_Fam));
        jtf_SD_PP_Fam.setText(String.format(Locale.US, "%.2f", SD_PP_Fam));

        // Valores HEX - Formateados a 2 decimales
        jtf_Media_NN_HEX.setText(String.format(Locale.US, "%.2f", Media_NN_HEX));
        jtf_Media_NP_HEX.setText(String.format(Locale.US, "%.2f", Media_NP_HEX));
        jtf_Media_PP_HEX.setText(String.format(Locale.US, "%.2f", Media_PP_HEX));

        jtf_SD_NN_HEX.setText(String.format(Locale.US, "%.2f", SD_NN_HEX));
        jtf_SD_NP_HEX.setText(String.format(Locale.US, "%.2f", SD_NP_HEX));
        jtf_SD_PP_HEX.setText(String.format(Locale.US, "%.2f", SD_PP_HEX));

        // --- Nuevos elementos a restaurar ---

        // 1. Restaurar Nombre del Proyecto (con formato de Hint)
        jtf_nombre.setText("Escriba el nombre aquí...");
        jtf_nombre.setForeground(Color.GRAY);
        jtf_nombre.setFont(new Font("Arial", Font.ITALIC, 14));

        // 2. Restaurar Alelos (usando el método auxiliar que ya creamos)
        restaurarCampoAlelo(jtf_alelo1, null, "Alelo 1");
        restaurarCampoAlelo(jtf_alelo2, null, "Control (*X)");
        restaurarCampoAlelo(jtf_alelo3, null, "Alelo 2");
        restaurarCampoAlelo(jtf_alelo4, null, "Control (*X)");

        // 3. Restaurar Selectores de Color (Sondas) a sus posiciones originales
        comboSonda1.setSelectedIndex(0); // FAM
        comboSonda2.setSelectedIndex(5); // Texas Red
        comboSonda3.setSelectedIndex(1); // HEX
        comboSonda4.setSelectedIndex(7); // Cy5

        // 4. Restaurar Modo de Marcadores (Por defecto Cuatro Marcadores)
        if (comboModoMarcadores != null) {
            comboModoMarcadores.setSelectedIndex(1); // 1 = Cuatro Marcadores
            actualizarEstadoFilasSondas(); // Sincroniza habilitación de filas y pestañas
        }

        // 5. Limpiar etiquetas de archivos cargados
        jln1.setText("Archivo 1: Ninguno");
        jln2.setText("Archivo 2: Ninguno");
        jln3.setText("Archivo 3: Ninguno");
        jln4.setText("Archivo 4: Ninguno");

        // Limpiar referencias a archivos internos (opcional pero recomendado)
        archivoS1 = null;
        archivoS2 = null;
        archivoS3 = null;
        archivoS4 = null;
    }
    
    
    private void permitirSoloNumerosEnteros(JTextField textField, int maxDigits) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                
                // 1. Obtener el texto actual del documento
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                
                // 2. Construir el texto resultante de la operación
                StringBuilder sb = new StringBuilder(currentText);
                sb.replace(offset, offset + length, text); // Simulamos el cambio
                String resultingText = sb.toString();

                // 3. Validar: ¿Es un número y cumple con la longitud máxima?
                // Nota: Permitimos texto vacío (para borrar) o números
                if (resultingText.isEmpty() || resultingText.matches("\\d{0," + maxDigits + "}")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }
    
 /// Permite números negativos y decimales con máximo 3 decimales
    private void permitirSoloNumerosDecimales(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {

                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                // Expresión regular: permite signo negativo solo al inicio, un punto y máximo 3 decimales
                if (isValidDecimalInput(newText)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) 
                    throws BadLocationException {

                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset);

                if (isValidDecimalInput(newText)) {
                    super.insertString(fb, offset, text, attrs);
                }
            }

            // Método auxiliar para validar el formato
            private boolean isValidDecimalInput(String text) {
                // Permitir vacío o solo el signo negativo
                if (text.isEmpty() || text.equals("-")) {
                    return true;
                }

                // Expresión regular: signo negativo opcional + dígitos + punto opcional + hasta 3 decimales
                return text.matches("-?\\d*\\.?\\d{0,3}") && 
                       (text.split("\\.").length <= 2); // máximo un punto
            }
        });
    }


    //GETTERS Y SETTERS

	public double getMedia_NN_Fam() {
		return Media_NN_Fam;
	}




	




	public double getMedia_NP_Fam() {
		return Media_NP_Fam;
	}




	




	public double getMedia_PP_Fam() {
		return Media_PP_Fam;
	}




	



	public double getSD_NN_Fam() {
		return SD_NN_Fam;
	}




	




	public double getSD_NP_Fam() {
		return SD_NP_Fam;
	}




	



	public double getSD_PP_Fam() {
		return SD_PP_Fam;
	}




	



	public double getMedia_NN_HEX() {
		return Media_NN_HEX;
	}




	



	public double getMedia_NP_HEX() {
		return Media_NP_HEX;
	}




	




	public double getMedia_PP_HEX() {
		return Media_PP_HEX;
	}




	




	public double getSD_NN_HEX() {
		return SD_NN_HEX;
	}




	



	public double getSD_NP_HEX() {
		return SD_NP_HEX;
	}




	




	public double getSD_PP_HEX() {
		return SD_PP_HEX;
	}


	public JTextField getJtf_primerCiclo() {
		return jtf_primerCiclo;
	}




	public void setJtf_primerCiclo(JTextField jta_promedioInferior) {
		this.jtf_primerCiclo = jta_promedioInferior;
	}

	
	public JTextField getJtf_segundoCiclo() {
		return jtf_segundoCiclo;
	}




	public void setJtf_segundoCiclo(JTextField jta_promedioInferior) {
		this.jtf_segundoCiclo = jta_promedioInferior;
	}



	public JTextField getJta_promedioSuperior() {
		return jtf_promedioSuperior;
	}




	public void setJta_promedioSuperior(JTextField jta_promedioSuperior) {
		this.jtf_promedioSuperior = jta_promedioSuperior;
	}




	public JTextField getJtf_umbral() {
		return jtf_umbral;
	}




	public void setJtf_umbral(JTextField jtf_umbral) {
		this.jtf_umbral = jtf_umbral;
	}




	public JTextField getJtf_Media_NN_Fam() {
		return jtf_Media_NN_Fam;
	}




	public void setJtf_Media_NN_Fam(JTextField jtf_Media_NN_Fam) {
		this.jtf_Media_NN_Fam = jtf_Media_NN_Fam;
	}




	public JTextField getJtf_Media_NP_Fam() {
		return jtf_Media_NP_Fam;
	}




	public void setJtf_Media_NP_Fam(JTextField jtf_Media_NP_Fam) {
		this.jtf_Media_NP_Fam = jtf_Media_NP_Fam;
	}




	public JTextField getJtf_Media_PP_Fam() {
		return jtf_Media_PP_Fam;
	}




	public void setJtf_Media_PP_Fam(JTextField jtf_Media_PP_Fam) {
		this.jtf_Media_PP_Fam = jtf_Media_PP_Fam;
	}




	public JTextField getJtf_SD_NN_Fam() {
		return jtf_SD_NN_Fam;
	}




	public void setJtf_SD_NN_Fam(JTextField jtf_SD_NN_Fam) {
		this.jtf_SD_NN_Fam = jtf_SD_NN_Fam;
	}




	public JTextField getJtf_SD_NP_Fam() {
		return jtf_SD_NP_Fam;
	}




	public void setJtf_SD_NP_Fam(JTextField jtf_SD_NP_Fam) {
		this.jtf_SD_NP_Fam = jtf_SD_NP_Fam;
	}




	public JTextField getJtf_SD_PP_Fam() {
		return jtf_SD_PP_Fam;
	}




	public void setJtf_SD_PP_Fam(JTextField jtf_SD_PP_Fam) {
		this.jtf_SD_PP_Fam = jtf_SD_PP_Fam;
	}




	public JTextField getJtf_Media_NN_HEX() {
		return jtf_Media_NN_HEX;
	}




	public void setJtf_Media_NN_HEX(JTextField jtf_Media_NN_HEX) {
		this.jtf_Media_NN_HEX = jtf_Media_NN_HEX;
	}




	public JTextField getJtf_Media_NP_HEX() {
		return jtf_Media_NP_HEX;
	}




	public void setJtf_Media_NP_HEX(JTextField jtf_Media_NP_HEX) {
		this.jtf_Media_NP_HEX = jtf_Media_NP_HEX;
	}




	public JTextField getJtf_Media_PP_HEX() {
		return jtf_Media_PP_HEX;
	}




	public void setJtf_Media_PP_HEX(JTextField jtf_Media_PP_HEX) {
		this.jtf_Media_PP_HEX = jtf_Media_PP_HEX;
	}




	public JTextField getJtf_SD_NN_HEX() {
		return jtf_SD_NN_HEX;
	}




	public void setJtf_SD_NN_HEX(JTextField jtf_SD_NN_HEX) {
		this.jtf_SD_NN_HEX = jtf_SD_NN_HEX;
	}




	public JTextField getJtf_SD_NP_HEX() {
		return jtf_SD_NP_HEX;
	}




	public void setJtf_SD_NP_HEX(JTextField jtf_SD_NP_HEX) {
		this.jtf_SD_NP_HEX = jtf_SD_NP_HEX;
	}




	public JTextField getJtf_SD_PP_HEX() {
		return jtf_SD_PP_HEX;
	}




	public void setJtf_SD_PP_HEX(JTextField jtf_SD_PP_HEX) {
		this.jtf_SD_PP_HEX = jtf_SD_PP_HEX;
	}




	




	public JTable getTabla1() {
		return tabla1;
	}




	public void setTabla1(JTable tabla1) {
		this.tabla1 = tabla1;
	}




	public JTable getTabla2() {
		return tabla2;
	}




	public void setTabla2(JTable tabla2) {
		this.tabla2 = tabla2;
	}

    public JTable getTabla3() {
        return tabla3;
    }




    public void setTabla3(JTable tabla3) {
        this.tabla3 = tabla3;
    }


	public DefaultTableModel getModel1() {
		return model1;
	}
	public void setModel1(DefaultTableModel model1) {
		this.model1 = model1;
	}




	public DefaultTableModel getModel2() {
		return model2;
	}




	public void setModel2(DefaultTableModel model2) {
		this.model2 = model2;
	}

    public DefaultTableModel getModel3() {
        return model3;
    }
    public void setModel3(DefaultTableModel model3) {
        this.model3 = model3;
    }


	public JTextField getJtf_promedioSuperior() {
		return jtf_promedioSuperior;
	}




	public JTextField getJtf_nombre() {
		return jtf_nombre;
	}

    public JTextField getJtf_alelo1() {
        return jtf_alelo1;
    }

    public JTextField getJtf_alelo2() {
        return jtf_alelo2;
    }

    public JTextField getJtf_alelo3() {
        return jtf_alelo3;
    }

    public JTextField getJtf_alelo4() {
        return jtf_alelo4;
    }

    public JComboBox<String> getComboSonda1() {
        return comboSonda1;
    }

    public JComboBox<String> getComboSonda2() {
        return comboSonda2;
    }

    public JComboBox<String> getComboSonda3() {
        return comboSonda3;
    }

    public JComboBox<String> getComboSonda4() {
        return comboSonda4;
    }

    public JComboBox<String> getComboModoMarcadores() {
        return comboModoMarcadores;
    }

    public String getVersion() {
        return version;
    }

    public JTextField getJtf_EndRFU() {
        return jtf_EndRFU;
    }
}