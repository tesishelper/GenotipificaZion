package ventana;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class Ventana_ayuda extends JFrame {




    public Ventana_ayuda() {
        super("GenotipificaZión v= 1.0");

       

        this.setSize(1100, 800);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        // IMPORTANTE: DISPOSE_ON_CLOSE cierra solo esta ventana, no el programa entero
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        definirVentana();
        this.setVisible(true);
    }

    private void definirVentana() {
        // Creamos el panel de pestañas
        JTabbedPane pestañas = new JTabbedPane();

        // 1. Pestaña de Instrucciones
        pestañas.addTab("Instrucciones", crearPanelInstrucciones());
        
        // 2. Pestaña de Modelo Matematico
        pestañas.addTab("Modelo Matemático", crearPanelModeloMatematico());

        // 2. Pestaña de imagen ayuda.jpg
        pestañas.addTab("Guía Visual 1", crearPanelImagen("ayuda1.jpg"));

        // 3. Pestaña de imagen ayuda 2
        pestañas.addTab("Guía Visual 2", crearPanelImagen("ayuda2.jpg"));

        // 4. Pestaña de imagen ayuda 3
        pestañas.addTab("Dos Sondas", crearPanelImagen("ayuda3.jpg"));

        // 4. Pestaña de imagen ayuda 4
        pestañas.addTab("Cuatro Sondas", crearPanelImagen("ayuda4.jpg"));

        // Agregamos el JTabbedPane a la ventana
        this.add(pestañas);
    }

    private JScrollPane crearPanelInstrucciones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(Color.WHITE); // Mantenemos el fondo limpio

        // Usamos las mismas fuentes que en el modelo matemático
        Font fuenteTitulo = new Font("Arial", Font.BOLD, 24);
        Font fuenteTexto = new Font("Arial", Font.PLAIN, 15);

        // Título principal
        JLabel titulo = new JLabel("Cómo usar el programa");
        titulo.setFont(fuenteTitulo);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Textos con formato HTML para resaltar elementos clave
        String[] instrucciones = {
            "<html><b>1. Ingreso de datos:</b><br>" +
            "Ingrese los datos en la tabla principal con los botones correspondientes.<br>" +
            "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4'><i>Nota: El programa carga archivos <b>.csv</b> con etiquetas \"Fam\", \"Texas Red\", \"HEX\" y \"Cy5\".</i></font></html>",

            "<html><b>2. Procesamiento:</b><br>" +
            "Presione el botón <font color='green'><b>\"Calcular\"</b></font> o use el menú <i>Archivo > Calcular</i> para procesar los resultados analíticos.</html>",

            "<html><b>3. Exportación:</b><br>" +
            "Exporte sus resultados a formato CSV usando el menú <font color='#c0392b'><i>\"Archivo > Exportar\"</i></font> o el botón dedicado en la barra de herramientas.</html>",

            "<html><b>4. Configuración:</b><br>" +
            "Puede personalizar los umbrales y preferencias de cálculo en la pestaña <font color='#1a5fb4'><b>\"Ajustes\"</b></font>.</html>"
        };

        for (String texto : instrucciones) {
            JLabel etiqueta = new JLabel(texto);
            etiqueta.setFont(fuenteTexto);
            etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(etiqueta);
            // Espaciado generoso entre pasos para mejorar la legibilidad
            panel.add(Box.createRigidArea(new Dimension(0, 22))); 
        }

        // Configuración del Scroll para que sea igual al otro panel
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll fluido
        scrollPane.setBorder(null);

        return scrollPane;
    }
    
    private JScrollPane crearPanelModeloMatematico() { // Cambiamos el retorno a JScrollPane
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(Color.WHITE); // Opcional: fondo blanco para mayor claridad

        Font fuenteTitulo = new Font("Arial", Font.BOLD, 24);
        Font fuenteTexto = new Font("Arial", Font.PLAIN, 15);

        // Encabezado previo
       
        
        //panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel titulo = new JLabel("Metodología de Procesamiento de Datos");
        titulo.setFont(fuenteTitulo);
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        String[] secciones = {
            "<html><b>1. Cálculo de fluorescencia neta (<i>EndRFU</i>)</b><br>" +
            "Para cada fluoróforo (FAM, HEX, Texas Red y Cy5) y well, empleando las lecturas de fluorescencia crudas " +
            "(sin restar baseline) se calculan las <i>EndRFU</i> con la siguiente fórmula:<br><br>" +
            "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4' size='5'><i>EndRFU = F&#772;<sub>últimos 5</sub> - F&#772;<sub>línea base</sub></i></font><br><br>" +
            "esto es, últimos 5 ciclos y primeros 10 ciclos (baseline).</html>",

            "<html><b>2. Normalización de valores negativos</b><br>" +
            "Luego se eliminan valores negativos de <i>EndRFU</i>," +
            "de manera que si el valor es &lt; 1 se fija en 1:<br><br>" +
            "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4' size='5'><i>Si EndRFU &lt; 1 => EndRFU = 1 </i></font></html>",

            "<html><b>3. Cálculo de la relación alélica (<i>Log<sub>2</sub></i>)</b><br>" +
            "Se calcula el <i>Log<sub>2</sub></i> del cociente de <i>EndRFU</i> entre sondas que reconocen diferentes alelos " +
            "con la siguiente fórmula:<br><br>" +
            "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4' size='5'><i>Relación = log<sub>2</sub> ( EndRFU<sub>Sonda A</sub> / EndRFU<sub>Sonda B</sub> )</i></font></html>",

            "<html><b>4. Filtrado de calidad y amplificación</b><br>" +
            "Se filtran datos que no haya amplificación o los valores de <i>EndRFU</i> sean demasiado bajos " +
            "(menores a 150 entre ambas sondas) con la siguiente fórmula:<br><br>" +
            "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4' size='5'><i>Resultado = EndRFU<sub>A</sub> + EndRFU<sub>B</sub> &lt; 150; Resultado = \"ND\" </i></font></html>",

            "<html><b>5. Cálculo de Verosimilitud (Likelihood)</b><br>" +
            "Para cada valor obtenido antes se calcula la verosimilitud de los tres posibles genotipos " +
            "(ambos homocigotas y heterocigota) con la siguiente fórmula:<br><br>" +
            "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4' size='6'><i>L(x | &#956;, &#963;) = [ 1 / (&#963;&radic;2&#960;) ] &sdot; e<sup>-&frac12; [ (x - &#956;) / &#963; ]&sup2;</sup></i></font><br><br>" +
            "Esta emplea los valores de Media (&#956;) y SD (&#963;) obtenidos en muestras de referencia.<br>" +
            "<b><i>x</i>:</b> Es el valor de la relación logarítmica calculada.<br>" +
            "<b><i>&#956;</i> y <i>&#963;</i>:</b> Son la media y la desviación estándar obtenidas de las muestras de referencia para cada genotipo.</html>",

            "<html><b>6. Asignación de Genotipo</b><br>" +
            "Finalmente, los tres valores de verosimilitud son ajustados a 100% total y el valor más alto " +
            "es el genotipo asignado con su correspondiente valor de confianza.</html>"
        };

        for (String contenido : secciones) {
            JLabel etiqueta = new JLabel(contenido);
            etiqueta.setFont(fuenteTexto);
            etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(etiqueta);
            panel.add(Box.createRigidArea(new Dimension(0, 30))); 
        }

        // --- AQUÍ CREAMOS EL SCROLL ---
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Hace que el scroll sea suave
        scrollPane.setBorder(null); // Quita el borde doble si es necesario

        return scrollPane;
    }
    private JPanel crearPanelImagen(String nombreImagen) {
        // Eclipse buscará en todas las "Source Folders"
        // El "/" inicial es vital para buscar desde la raíz del classpath
        URL urlImagen = getClass().getResource("/" + nombreImagen);

        if (urlImagen != null) {
            Image imagenOriginal = new ImageIcon(urlImagen).getImage();

            return new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (imagenOriginal != null) {
                        Graphics2D g2d = (Graphics2D) g;
                        // Suavizado para que al achicar la imagen no se vea "dentada"
                        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        
                        g.drawImage(imagenOriginal, 0, 0, getWidth(), getHeight(), this);
                    }
                }
            };
        } else {
            JPanel panelError = new JPanel(new BorderLayout());
            panelError.add(new JLabel("No se encontró: " + nombreImagen), BorderLayout.CENTER);
            return panelError;
        }
    }
}