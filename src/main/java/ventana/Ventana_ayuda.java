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
        pestañas.addTab("Instructions", crearPanelInstrucciones());
        
        // 2. Pestaña de Modelo Matematico
        pestañas.addTab("Mathematical Model", crearPanelModeloMatematico());

        // 2. Pestaña de imagen ayuda.jpg
        pestañas.addTab("Visual Guide 1", crearPanelImagen("ayuda1_eng.jpg"));

        // 3. Pestaña de imagen ayuda 2
        pestañas.addTab("Visual Guide 2", crearPanelImagen("ayuda2_eng.jpg"));

        // 4. Pestaña de imagen ayuda 3
        pestañas.addTab("Two Probes", crearPanelImagen("ayuda3.jpg"));

        // 4. Pestaña de imagen ayuda 4
        pestañas.addTab("Four Probes", crearPanelImagen("ayuda4_eng.jpg"));

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
        JLabel titulo = new JLabel("How to use the program");
        titulo.setFont(fuenteTitulo);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Textos con formato HTML para resaltar elementos clave
        String[] instrucciones = {
                "<html><b>1. Data Entry:</b><br>" +
                        "Enter the data into the main table using the corresponding buttons.<br>" +
                        "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4'><i>Note: The program loads <b>.csv</b> files with labels \"Fam\", \"Texas Red\", \"HEX\", and \"Cy5\".</i></font></html>",

                "<html><b>2. Processing:</b><br>" +
                        "Click the <font color='green'><b>\"Calculate\"</b></font> button or use the menu <i>File > Calculate</i> to process the analytical results.</html>",

                "<html><b>3. Export:</b><br>" +
                        "Export your results to CSV format using the menu <font color='#c0392b'><i>\"File > Export\"</i></font> or the dedicated button in the toolbar.</html>",

                "<html><b>4. Settings:</b><br>" +
                        "You can customize the calculation thresholds and preferences in the <font color='#1a5fb4'><b>\"Settings\"</b></font> tab.</html>"        };

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

        JLabel titulo = new JLabel("Data Processing Methodology");
        titulo.setFont(fuenteTitulo);
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        String[] secciones = {
                "<html><b>1. Net fluorescence calculation (<i>EndRFU</i>)</b><br>" +
                        "For each fluorophore (FAM, HEX, Texas Red, and Cy5) and well, using raw fluorescence readings " +
                        "(without baseline subtraction), <i>EndRFU</i> are calculated with the following formula:<br><br>" +
                        "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4' size='5'><i>EndRFU = F&#772;<sub>last 5</sub> - F&#772;<sub>baseline</sub></i></font><br><br>" +
                        "that is, last 5 cycles and first 10 cycles (baseline).</html>",

                "<html><b>2. Negative value normalization</b><br>" +
                        "Negative <i>EndRFU</i> values are then removed," +
                        "so that if the value is &lt; 1, it is set to 1:<br><br>" +
                        "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4' size='5'><i>If EndRFU &lt; 1 => EndRFU = 1 </i></font></html>",

                "<html><b>3. Allelic ratio calculation (<i>Log<sub>2</sub></i>)</b><br>" +
                        "The <i>Log<sub>2</sub></i> of the <i>EndRFU</i> ratio between probes recognizing different alleles " +
                        "is calculated using the following formula:<br><br>" +
                        "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4' size='5'><i>Ratio = log<sub>2</sub> ( EndRFU<sub>Probe A</sub> / EndRFU<sub>Probe B</sub> )</i></font></html>",

                "<html><b>4. Quality and amplification filtering</b><br>" +
                        "Data with no amplification or with <i>EndRFU</i> values that are too low " +
                        "(less than 150 between both probes) are filtered out using the following formula:<br><br>" +
                        "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4' size='5'><i>Result = EndRFU<sub>A</sub> + EndRFU<sub>B</sub> &lt; 150; Result = \"ND\" </i></font></html>",

                "<html><b>5. Likelihood Calculation</b><br>" +
                        "For each value obtained above, the likelihood of the three possible genotypes " +
                        "(both homozygotes and heterozygote) is calculated using the following formula:<br><br>" +
                        "&nbsp;&nbsp;&nbsp;<font color='#1a5fb4' size='6'><i>L(x | &#956;, &#963;) = [ 1 / (&#963;&radic;2&#960;) ] &sdot; e<sup>-&frac12; [ (x - &#956;) / &#963; ]&sup2;</sup></i></font><br><br>" +
                        "This uses the Mean (&#956;) and SD (&#963;) values obtained from reference samples.<br>" +
                        "<b><i>x</i>:</b> Is the calculated logarithmic ratio value.<br>" +
                        "<b><i>&#956;</i> and <i>&#963;</i>:</b> Are the mean and standard deviation obtained from the reference samples for each genotype.</html>",

                "<html><b>6. Genotype Assignment</b><br>" +
                        "Finally, the three likelihood values are adjusted to 100% total, and the highest value " +
                        "corresponds to the assigned genotype with its confidence value.</html>"
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