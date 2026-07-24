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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Desktop;
import java.net.URI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class VentanaAcercaDe extends JDialog {

    public VentanaAcercaDe(Frame parent) {
        super(parent, "Acerca de", true);
        setSize(400, 400); // Aumenté un poco el tamaño para que quepa todo
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título e info (usando HTML)
        JLabel titulo = new JLabel("<html><h2 style='color:#2E86C1;'>GenotipificaZión v= 1.1</h2></html>");
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel info = new JLabel("<html><p>Desarrollado por:</p>" +
                "<ul><li>Dr. Maximiliano Juri Ayub (Modelo Matemático)</li>" +
                "<li>Dr. Adolfo R. Zurita (programador)</li></ul>" +
                "<p>Software de código abierto.</p>" +
                "<p>Licencia: <a href='https://www.gnu.org/licenses/gpl-3.0.html'>GPL-3.0</a> 2026</p></html>");
        info.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelContenido.add(titulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        panelContenido.add(info);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- SECCIÓN DE CONTACTO ALINEADA ---
        JLabel lblTituloContacto = new JLabel("Contacto:");
        lblTituloContacto.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelContenido.add(lblTituloContacto);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 5)));

        // Agregamos ambos contactos usando el método auxiliar
        panelContenido.add(crearPanelContacto("Juri Ayub:", "mjuriayub@hotmail.com"));
        panelContenido.add(Box.createRigidArea(new Dimension(0, 5)));
        panelContenido.add(crearPanelContacto("Zurita:", "azurita1974@gmail.com"));

        add(panelContenido, BorderLayout.CENTER);
    }

    // Método auxiliar para crear las filas de contacto de forma uniforme
    private JPanel crearPanelContacto(String nombre, String email) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT); // Importante para que no se desplace a la izquierda
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Evita que se estire demasiado

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setPreferredSize(new Dimension(80, 25)); // Alineación fija para los nombres

        JTextField txtEmail = new JTextField(email);
        txtEmail.setEditable(false);
        txtEmail.setBorder(null);
        txtEmail.setBackground(new Color(238, 238, 238)); // O el color de fondo de tu ventana
        txtEmail.setColumns(15);

        JButton btnCopiar = new JButton("Copiar");
        btnCopiar.setMargin(new Insets(0, 5, 0, 5));
        btnCopiar.addActionListener(e -> {
            StringSelection selection = new StringSelection(email);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog(this, "Correo de " + nombre.replace(":", "") + " copiado.");
        });

        panel.add(lblNombre);
        panel.add(txtEmail);
        panel.add(btnCopiar);
        
        return panel;
    }
}