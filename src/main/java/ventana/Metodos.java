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

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class Metodos {
	
	private static final String LAST_DIRECTORY_KEY = "lastDirectory";
	private static final String CONFIG_FILE = "config.properties";
	List<String>  ListaResultado1 = new ArrayList<>(); //Lista resultado final 1 y 2
	List<String>  ListaResultado2 = new ArrayList<>(); //Lista Resultado final 3 y 4
	List<String>  ListaResultado3 = new ArrayList<>(); //Lista Resultado final
	
	
	public Metodos() {
		
		
	}
	
	
	

	public File cargarArchivo(String titulo, String letras, File file, Component parent) {
	    
	    JFileChooser fileChooser = new JFileChooser();
	    
	    // === Cargar la última carpeta guardada ===
	    File lastDirectory = cargarUltimaCarpeta();
	    if (lastDirectory != null && lastDirectory.exists()) {
	        fileChooser.setCurrentDirectory(lastDirectory);
	    }

	    // Título de la ventana
	    fileChooser.setDialogTitle(titulo);

	    // Filtrar solo archivos .csv
	    FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv");
	    fileChooser.setFileFilter(filtro);

	    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    fileChooser.setMultiSelectionEnabled(false);

	    int resultado = fileChooser.showOpenDialog(parent);

	    if (resultado == JFileChooser.APPROVE_OPTION) {
	        file = fileChooser.getSelectedFile();

	        if (file == null) {
	            return null;
	        }

	        // === GUARDAR la carpeta seleccionada como última ===
	        guardarUltimaCarpeta(file.getParentFile());

	        String nombreArchivo = file.getName();
	        String letrasLower = letras.toLowerCase().trim();
	        String nombreLower = nombreArchivo.toLowerCase();

	        if (!nombreLower.contains(letrasLower)) {
	            JOptionPane.showMessageDialog(parent,
	                "El archivo debe contener '" + letras + "' en su nombre.\n\n" +
	                "Archivo seleccionado: " + file.getName(),
	                "Archivo no válido",
	                JOptionPane.WARNING_MESSAGE);
	            return null;
	        }

	        if (file.getName().toLowerCase().endsWith(".csv")) {
	            //System.out.println("Archivo seleccionado correctamente: " + file.getAbsolutePath());
	            return file;
	        } else {
	            JOptionPane.showMessageDialog(parent,
	                "Por favor seleccione un archivo con extensión .csv",
	                "Error",
	                JOptionPane.ERROR_MESSAGE);
	            return null;
	        }
	    } else {
	        System.out.println("El usuario canceló la selección");
	        return null;
	    }
	}
	
	// Guardar la última carpeta
	private void guardarUltimaCarpeta(File directory) {
	    if (directory == null || !directory.exists()) return;

	    Properties props = new Properties();
	    props.setProperty(LAST_DIRECTORY_KEY, directory.getAbsolutePath());

	    try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
	        props.store(fos, "Última carpeta usada por el FileChooser");
	        //System.out.println("Carpeta guardada: " + directory.getAbsolutePath());
	    } catch (IOException e) {
	        System.err.println("No se pudo guardar la última carpeta: " + e.getMessage());
	    }
	}

	// Cargar la última carpeta
	private File cargarUltimaCarpeta() {
	    File configFile = new File(CONFIG_FILE);
	    if (!configFile.exists()) {
	        return null;
	    }

	    Properties props = new Properties();
	    try (FileInputStream fis = new FileInputStream(configFile)) {
	        props.load(fis);
	        String path = props.getProperty(LAST_DIRECTORY_KEY);
	        if (path != null) {
	            return new File(path);
	        }
	    } catch (IOException e) {
	        System.err.println("No se pudo leer la última carpeta: " + e.getMessage());
	    }
	    return null;
	}
	 public void leerCSV(List<String> headers, List<List<Double>> datos, File archivo, Component parent) {
	        
	        headers.clear();
	        datos.clear();

	        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
	            String linea;
	            boolean primeraLinea = true;

	            while ((linea = br.readLine()) != null) {
	                linea = linea.trim();
	                if (linea.isEmpty()) continue;

	                String[] valores = linea.split(";");

	                if (primeraLinea) {
	                    // Guardamos los encabezados (A1, A2, ..., H12)
	                    for (int i = 1; i < valores.length; i++) {   // saltamos "Cycle"
	                        String header = valores[i].trim();
	                        headers.add(header);
	                        datos.add(new ArrayList<>());   // creamos lista vacía para esta columna
	                    }
	                    primeraLinea = false;
	                    continue;
	                }

	                // Llenamos los datos
	                for (int i = 1; i < valores.length && (i-1) < headers.size(); i++) {
	                    String valorStr = valores[i].trim().replace(",", ".");
	                    
	                    try {
	                        double valor = Double.parseDouble(valorStr);
	                        datos.get(i - 1).add(valor);
	                    } catch (Exception e) {
	                        datos.get(i - 1).add(Double.NaN); // valor inválido o vacío
	                    }
	                }
	            }

	          //  System.out.println("✅ Archivo procesado correctamente.");
	          //  System.out.println("   Cantidad de columnas: " + headers.size());
	           // System.out.println("   Cantidad de ciclos: " + 
	           //                   (datos.isEmpty() ? 0 : datos.get(0).size()));

	        } catch (IOException e) {
	            JOptionPane.showMessageDialog(parent, 
	                "Error al leer el archivo:\n" + e.getMessage(), 
	                "Error", JOptionPane.ERROR_MESSAGE);
	            e.printStackTrace();
	        }
	    }
	public void calcularDosMarcadores(Ventana v, List<String> headers1, List<List<Double>> datos1, List<List<Double>> datos2, JTextArea jta_resultado) {

		// 1. Obtener los textos seleccionados de cada combo
		String s1 = (String) v.getComboSonda1().getSelectedItem();
		String s2 = (String) v.getComboSonda2().getSelectedItem();

		int endRFU = Integer.parseInt(v.getJtf_EndRFU().getText().trim());


		// 2. Verificar que todos sean diferentes
		boolean todosDiferentes = java.util.stream.Stream.of(s1, s2)
				.distinct()
				.count() == 2;

		if (!todosDiferentes) {
			JOptionPane.showMessageDialog(null,
					"Error: Se han detectado sondas duplicadas. Cada canal debe tener una sonda distinta (FAM, HEX, etc.)",
					"Configuración Incorrecta",
					JOptionPane.ERROR_MESSAGE);
			return; // Detiene la ejecución del cálculo
		}

		else{



			double Media_NN_Fam = extraerFloatDeTextField(v.getJtf_Media_NN_Fam());
		double Media_NP_Fam = extraerFloatDeTextField(v.getJtf_Media_NP_Fam());
		double Media_PP_Fam = extraerFloatDeTextField(v.getJtf_Media_PP_Fam());
		double SD_NN_Fam = extraerFloatDeTextField(v.getJtf_SD_NN_Fam());
		double SD_NP_Fam = extraerFloatDeTextField(v.getJtf_SD_NP_Fam());
		double SD_PP_Fam = extraerFloatDeTextField(v.getJtf_SD_PP_Fam());
		double Media_NN_HEX = extraerFloatDeTextField(v.getJtf_Media_NN_HEX());
		double Media_NP_HEX = extraerFloatDeTextField(v.getJtf_Media_NP_HEX());
		double Media_PP_HEX = extraerFloatDeTextField(v.getJtf_Media_PP_HEX());
		double SD_NN_HEX = extraerFloatDeTextField(v.getJtf_SD_NN_HEX());
		double SD_NP_HEX = extraerFloatDeTextField(v.getJtf_SD_NP_HEX());
		double SD_PP_HEX = extraerFloatDeTextField(v.getJtf_SD_PP_HEX());
		int  umbralVacio = (int) extraerFloatDeTextField(v.getJtf_umbral());
		int  primerCiclo = (int) extraerFloatDeTextField(v.getJtf_primerCiclo());
		int  segundoCiclo = (int) extraerFloatDeTextField(v.getJtf_segundoCiclo());
		int  ultimosCiclos = (int) extraerFloatDeTextField(v.getJtf_promedioSuperior());

		// jta_resultado.setText("");
		List<Double>  ListaDeltas1 = new ArrayList<>(); //deltas datos 1 Fem
		List<Double>  ListaDeltas2 = new ArrayList<>(); //deltas datos 2 TexasRed



		List<Boolean> ListaVaciosLlenos = new ArrayList<>(); //me dice si hay o no muestra en el pocillo

		List<Double>  DatosValidados1 = new ArrayList<>(); //deltas datos 1 y 2


		ListaResultado1.clear();


		//Indice del primer y segundo ciclo



		int primerIndice = 0;
		int segundoIndice = 0;

		for(int pc = 0; pc < datos1.get(0).size(); pc++ ) {

			if(datos1.get(0).get(pc)== primerCiclo) {primerIndice = pc;};
			if(datos1.get(0).get(pc)== segundoCiclo){segundoIndice = pc;};
		}



		if(datos1.get(0).size() < primerCiclo || datos1.get(0).size() < segundoCiclo ) {

			JOptionPane.showMessageDialog(v,
					"Los números ce ciclos utilizados para calcular el promedio basal, no pueden ser mayores a la cantidad total de ciclos."+
							"\nVer pestaña de configuración",
					"Error",
					JOptionPane.ERROR_MESSAGE);

		}




		// System.out.println("Indice del primer ciclo: " + primerIndice + " Primer ciclo: "+ datos1.get(0).get(primerIndice));

		// System.out.println("Indice del segundo ciclo: " + segundoIndice + " Segundo ciclo: "+ datos1.get(0).get(segundoIndice));;

		else if (segundoIndice<primerIndice) {

			JOptionPane.showMessageDialog(v,
					"El primer ciclo no puede ser mayor al segundo ciclo \nVer pestaña de configuración",
					"Error",
					JOptionPane.ERROR_MESSAGE);

		}

		else if(datos1.size() != datos2.size()) {

			JOptionPane.showMessageDialog(v,
					"Las listas suministradas no tienen la misma longitud. \nPor favor revisar su origen. ",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}



		else {

			//System.out.println(datos1.get(0));
			//System.out.println(datos1.get(1));

			//CALCULAR LOS PROMEDIOS Y LOS DELTAS
			for(int i = 0; i < headers1.size(); i++ ) { //todas las listan tienen mismo tamaño

				//obtener las listas dentro de datos1 a 2
				List<Double> list  = datos1.get(i);  //datos1 Fem
				List<Double> list2 = datos2.get(i); //datos2 TexasRed



				double promedioInferior1 = 0;
				double promedioInferior2 = 0;

				double promedioSuperior =  0;//los promedios Superiores los puedo reciclar pero al promedio
				// inferior los voy a usar para validar si hay muestra o no



				//Calcular el promedio de los 10 primero resultado de la lista de datos 1
				promedioInferior1 =  list.stream()
						.limit(segundoIndice - primerIndice + 1) // Toma solo los 10 primeros elementos //Ciclos entre el primer y cegundo ciclo
						.mapToDouble(Double::doubleValue)   // Convertimos Float a double
						.average()
						.orElse(0.0);

				// System.out.println(i+") Promedio inferior Fam: "+ promedioInferior1);
				//Calcular el promedio de los ultimos 5 resultados de la lista1
				long saltar = Math.max(0, list.size() - ultimosCiclos);
				promedioSuperior =  list.stream()
						.skip(saltar)      // salta los elementos para dejar solo los últimos n
						.mapToDouble(Double::doubleValue)
						.average()
						.orElse(0.0);

				// System.out.println(i+") Promedio Superior Fam: "+ promedioSuperior);
				double delta = promedioSuperior-promedioInferior1; //calcular la diferencia entre los promedios
				// System.out.println(i+") Delta Fam: "+ delta);

				if(delta<1) {delta=1;} //ajustar valores menores a 1

				ListaDeltas1.add(delta);//cargar resultados la lista


				//Calcular el promedio de los 10 primero resultado de la lista2
				promedioInferior2 =  list2.stream()
						.limit(segundoIndice - primerIndice + 1) //Ciclos entre el primer y cegundo ciclo
						.mapToDouble(Double::doubleValue)   // Convertimos Float a double
						.average()
						.orElse(0.0);
				//System.out.println("Promedio inferior TexasRed: "+ promedioInferior2);
				//Calcular el promedio de los ultimos 5 resultados de la lista2
				saltar = Math.max(0, list2.size() - ultimosCiclos);
				promedioSuperior =  list2.stream()
						.skip(saltar)      // salta los elementos para dejar solo los últimos n
						.mapToDouble(Double::doubleValue)
						.average()
						.orElse(0.0);
				delta = promedioSuperior-promedioInferior2;//calcular la diferencia entre los promedios
				// System.out.println("Delta TexasRed: "+ delta);

				if(delta<1) {delta=1;} //ajustar valores menores a 1

				ListaDeltas2.add(delta);	 //cargar resultados a la lista



				//VALIDAR SI HAY UN POCILLO VACIO O UNO LLENO

				double suma = promedioInferior1*2 + promedioInferior2*2;

				if(suma <= umbralVacio ) {

					ListaVaciosLlenos.add(false); //no hay muestra en este lugar
					//System.out.println(" Pocillo vacio lectura: "+ " "+ suma);
				}
				else {ListaVaciosLlenos.add(true);

					// System.out.println(" Pocillo lleno lectura: "+ " "+ suma);
				} //no hay muestra en este lugar


			}



			//VALIDAR LOS DATOS CON LA SUMA DE DELTA1 + DELTA2

			for (int e=0; e<headers1.size(); e++) {


				if(ListaVaciosLlenos.get(e)== true) {//Si hay muestra en el pocillo

					//System.out.println(headers1.get(e)+ " "+ e+ " "+ true);

					//Dupla Fam/TexasRed
					double c1 = ListaDeltas1.get(e);
					double c2 = ListaDeltas2.get(e);
					double sumaC1C2 = c1+c2;

					if(sumaC1C2 < endRFU) { DatosValidados1.add(1000000d); }//esta es una marca de que los datos no son validos
					else {
						double log =  ((Math.log(c1) - Math.log(c2)) / Math.log(2));
						DatosValidados1.add(log);}



				}
				//esta es una marca de que el pocillo está vacio
				else { DatosValidados1.add(2000000d);}


			}




			//PROCESAR LOS DATOS DE FEM/TEXAS-RED
			//Primera linea
			for ( int i =0 ; i<13; i++) { ListaResultado1.add(""+i); }

			//LINEAS SIGUIENTES
			for (int a = 1; a < headers1.size(); a++ ) {

				//primera letra de cada una de las lineas siguienteS

				switch (a) {

					case 1:  ListaResultado1.add("A");
						break;
					case 13: ListaResultado1.add("B");
						break;
					case 25: ListaResultado1.add("C");
						break;
					case 37: ListaResultado1.add("D");
						break;
					case 49: ListaResultado1.add("E");
						break;
					case 61: ListaResultado1.add("F");
						break;
					case 73: ListaResultado1.add("G");
						break;
					case 85: ListaResultado1.add("H");
						break;

				}

				//Agregar las LETRAS siguientes

				//Si el posillo esta vacio
				if(DatosValidados1.get(a)== 2000000f){ ListaResultado1.add(" ");  }
				//Si hay muestra en el pocillo
				else {

					double umbral = DatosValidados1.get(a);

					//Si los datos no son validos (ND)
					if (umbral == 1000000f ){ListaResultado1.add("ND");}
					//Si los datos son validos
					else {

						double maxVer;
						double ftnn = calcularDensidadGaussiana(DatosValidados1.get(a), Media_NN_Fam, SD_NN_Fam) ;
						double ftnp = calcularDensidadGaussiana(DatosValidados1.get(a), Media_NP_Fam, SD_NP_Fam) ;
						double ftpp = calcularDensidadGaussiana(DatosValidados1.get(a), Media_PP_Fam, SD_PP_Fam) ;

						if (ftnn > ftnp && ftnn > ftpp) {

							maxVer = (ftnn/(ftnn+ftnp+ftpp))*100; //Maxima verosimilitud

							ListaResultado1.add("N/N  (" + String.format("%.2f",maxVer) +" %)");
						}

						if (ftnp > ftnn && ftnp > ftpp) {

							maxVer = (ftnp/(ftnn+ftnp+ftpp))*100; //Maxima verosimilitud

							ListaResultado1.add("P/N  (" + String.format("%.2f",maxVer) +" %)");
						}

						if (ftpp > ftnn && ftpp > ftnp) {

							maxVer = (ftpp/(ftnn+ftnp+ftpp))*100; //Maxima verosimilitud

							ListaResultado1.add("P/P  (" + String.format("%.2f",maxVer) +" %)");
						}
					}}}


			//MOSTRAR LOS DATOS

			genotipoDosMarcadores(v, ListaResultado1,  ListaResultado3);

			actualizarTablasDosMarcadores(v.getModel1(),v.getModel3(),ListaResultado1, ListaResultado3 );






		}}}


	 public void calcularCuatroMarcadores(Ventana v,List<String> headers1, List<List<Double>> datos1, List<List<Double>> datos2, List<List<Double>> datos3, List<List<Double>> datos4, JTextArea jta_resultado) {

		 // 1. Obtener los textos seleccionados de cada combo
		 String s1 = (String) v.getComboSonda1().getSelectedItem();
		 String s2 = (String) v.getComboSonda2().getSelectedItem();
		 String s3 = (String) v.getComboSonda3().getSelectedItem();
		 String s4 = (String) v.getComboSonda4().getSelectedItem();

		 int endRFU = Integer.parseInt(v.getJtf_EndRFU().getText().trim());

		// 2. Verificar que todos sean diferentes
		 boolean todosDiferentes = java.util.stream.Stream.of(s1, s2, s3, s4)
				 .distinct()
				 .count() == 4;

		 if (!todosDiferentes) {
			 JOptionPane.showMessageDialog(null,
					 "Error: Se han detectado sondas duplicadas. Cada canal debe tener una sonda distinta (FAM, HEX, etc.)",
					 "Configuración Incorrecta",
					 JOptionPane.ERROR_MESSAGE);
			 return; // Detiene la ejecución del cálculo
		 }

		 else{


		 	double Media_NN_Fam = extraerFloatDeTextField(v.getJtf_Media_NN_Fam());
			double Media_NP_Fam = extraerFloatDeTextField(v.getJtf_Media_NP_Fam());
			double Media_PP_Fam = extraerFloatDeTextField(v.getJtf_Media_PP_Fam());
			double SD_NN_Fam = extraerFloatDeTextField(v.getJtf_SD_NN_Fam());
			double SD_NP_Fam = extraerFloatDeTextField(v.getJtf_SD_NP_Fam());
			double SD_PP_Fam = extraerFloatDeTextField(v.getJtf_SD_PP_Fam());
			double Media_NN_HEX = extraerFloatDeTextField(v.getJtf_Media_NN_HEX());
			double Media_NP_HEX = extraerFloatDeTextField(v.getJtf_Media_NP_HEX());
			double Media_PP_HEX = extraerFloatDeTextField(v.getJtf_Media_PP_HEX());
			double SD_NN_HEX = extraerFloatDeTextField(v.getJtf_SD_NN_HEX());
			double SD_NP_HEX = extraerFloatDeTextField(v.getJtf_SD_NP_HEX());
			double SD_PP_HEX = extraerFloatDeTextField(v.getJtf_SD_PP_HEX());
			int  umbralVacio = (int) extraerFloatDeTextField(v.getJtf_umbral());
			int  primerCiclo = (int) extraerFloatDeTextField(v.getJtf_primerCiclo());
			int  segundoCiclo = (int) extraerFloatDeTextField(v.getJtf_segundoCiclo());
			int  ultimosCiclos = (int) extraerFloatDeTextField(v.getJtf_promedioSuperior());
		 
		// jta_resultado.setText(""); 
		 List<Double>  ListaDeltas1 = new ArrayList<>(); //deltas datos 1 Fem
		 List<Double>  ListaDeltas2 = new ArrayList<>(); //deltas datos 2 TexasRed
		 List<Double>  ListaDeltas3 = new ArrayList<>(); //deltas datos 3 Hex
		 List<Double>  ListaDeltas4 = new ArrayList<>(); //deltas datos 4 Cy5
		 
		  
		 List<Boolean> ListaVaciosLlenos = new ArrayList<>(); //me dice si hay o no muestra en el pocillo
		 
		 List<Double>  DatosValidados1 = new ArrayList<>(); //deltas datos 1 y 2
		 List<Double>  DatosValidados2 = new ArrayList<>(); //deltas datos 3 y 4
		 
		 ListaResultado1.clear();
		 ListaResultado2.clear();	 
		 
		 //Indice del primer y segundo ciclo
		 
		 
		 
		 int primerIndice = 0;
		 int segundoIndice = 0;
		 
		 for(int pc = 0; pc < datos1.get(0).size(); pc++ ) {
			 
			if(datos1.get(0).get(pc)== primerCiclo) {primerIndice = pc;}; 
			if(datos1.get(0).get(pc)== segundoCiclo){segundoIndice = pc;};  
		 }
		 
		 
		 
        if(datos1.get(0).size() < primerCiclo || datos1.get(0).size() < segundoCiclo ) {
			 
			 JOptionPane.showMessageDialog(v,
		                "Los números ce ciclos utilizados para calcular el promedio basal, no pueden ser mayores a la cantidad total de ciclos."+
		                		"\nVer pestaña de configuración",
		                "Error",
		                JOptionPane.ERROR_MESSAGE);
			 
		 }
        
       
		 
		 
		// System.out.println("Indice del primer ciclo: " + primerIndice + " Primer ciclo: "+ datos1.get(0).get(primerIndice));
		 
		// System.out.println("Indice del segundo ciclo: " + segundoIndice + " Segundo ciclo: "+ datos1.get(0).get(segundoIndice));;
		 
         else if (segundoIndice<primerIndice) {
			 
			 JOptionPane.showMessageDialog(v,
		                "El primer ciclo no puede ser mayor al segundo ciclo \nVer pestaña de configuración",
		                "Error",
		                JOptionPane.ERROR_MESSAGE);
			 
		 }
		 
		 else if(datos1.size() != datos2.size() || datos1.size() != datos3.size() || datos1.size() != datos4.size()) {
			 
			 JOptionPane.showMessageDialog(v,
		                "Las listas suministradas no tienen la misma longitud. \nPor favor revisar su origen. ",
		                "Error",
		                JOptionPane.ERROR_MESSAGE);
		 }
		 
		 
		 
		 else {
		 		
			//System.out.println(datos1.get(0)); 
			//System.out.println(datos1.get(1));
			 
		//CALCULAR LOS PROMEDIOS Y LOS DELTAS		 
		 for(int i = 0; i < headers1.size(); i++ ) { //todas las listan tienen mismo tamaño
			 
			 //obtener las listas dentro de datos1 a 4
			 List<Double> list  = datos1.get(i);  //datos1 Fem
			 List<Double> list2 = datos2.get(i); //datos2 TexasRed
			 List<Double> list3 = datos3.get(i);  //datos3 Hex
			 List<Double> list4 = datos4.get(i); //datos4 Cy5
			  
			 
			 double promedioInferior1 = 0; 
			 double promedioInferior2 = 0;
			 double promedioInferior3 = 0;
			 double promedioInferior4 = 0;
			 double promedioSuperior =  0;//los promedios Superiores los puedo reciclar pero al promedio
			 							// inferior los voy a usar para validar si hay muestra o no
			 
			 
			 
		//Calcular el promedio de los 10 primero resultado de la lista de datos 1 
			    promedioInferior1 =  list.stream()
			    		                            .limit(segundoIndice - primerIndice + 1) // Toma solo los 10 primeros elementos //Ciclos entre el primer y cegundo ciclo
		                							.mapToDouble(Double::doubleValue)   // Convertimos Float a double
		                							.average()
		                							.orElse(0.0);
			    
			   // System.out.println(i+") Promedio inferior Fam: "+ promedioInferior1); 
		//Calcular el promedio de los ultimos 5 resultados de la lista1
			    long saltar = Math.max(0, list.size() - ultimosCiclos);
			    promedioSuperior =  list.stream()
					  								.skip(saltar)      // salta los elementos para dejar solo los últimos n
					  								.mapToDouble(Double::doubleValue)
					  								.average()
					  								.orElse(0.0);
			    
			   // System.out.println(i+") Promedio Superior Fam: "+ promedioSuperior);
			  double delta = promedioSuperior-promedioInferior1; //calcular la diferencia entre los promedios
			   // System.out.println(i+") Delta Fam: "+ delta);
			    
			  if(delta<1) {delta=1;} //ajustar valores menores a 1
			  
			  ListaDeltas1.add(delta);//cargar resultados la lista
			  
			 			  
			//Calcular el promedio de los 10 primero resultado de la lista2 
			   promedioInferior2 =  list2.stream()
					   								.limit(segundoIndice - primerIndice + 1) //Ciclos entre el primer y cegundo ciclo
		                							.mapToDouble(Double::doubleValue)   // Convertimos Float a double
		                							.average()
		                							.orElse(0.0);
			   //System.out.println("Promedio inferior TexasRed: "+ promedioInferior2); 
			   //Calcular el promedio de los ultimos 5 resultados de la lista2	
			   saltar = Math.max(0, list2.size() - ultimosCiclos);
			   promedioSuperior =  list2.stream()
					  								.skip(saltar)      // salta los elementos para dejar solo los últimos n
					  								.mapToDouble(Double::doubleValue)
					  								.average()
					  								.orElse(0.0);
			   delta = promedioSuperior-promedioInferior2;//calcular la diferencia entre los promedios
			  // System.out.println("Delta TexasRed: "+ delta);
			   
			   if(delta<1) {delta=1;} //ajustar valores menores a 1
			  
			   ListaDeltas2.add(delta);	 //cargar resultados a la lista
			   
			   
			 //Calcular el promedio de los 10 primero resultado de la lista 3 
			   promedioInferior3 =  list3.stream()
					                                .limit(segundoIndice - primerIndice + 1) //Ciclos entre el primer y cegundo ciclo
		                							.mapToDouble(Double::doubleValue)   // Convertimos Float a double
		                							.average()
		                							.orElse(0.0);
			   //System.out.println("Promedio inferior Hex: "+ promedioInferior3); 
			   //Calcular el promedio de los ultimos 5 resultados de la lista2	
			   saltar = Math.max(0, list3.size() - ultimosCiclos);
			   promedioSuperior =  list3.stream()
					  								.skip(saltar)      // salta los elementos para dejar solo los últimos n
					  								.mapToDouble(Double::doubleValue)
					  								.average()
					  								.orElse(0.0);
			   delta = promedioSuperior-promedioInferior3;//calcular la diferencia entre los promedios
			   //System.out.println("Delta Hex: "+ delta);
			   
			   if(delta<1) {delta=1;} //ajustar valores menores a 1
			  
			   ListaDeltas3.add(delta);	 //cargar resultados a la lista
			   
			 //Calcular el promedio de los 10 primero resultado de la lista 4 
			   promedioInferior4 =  list4.stream()
					   								.limit(segundoIndice - primerIndice + 1) //Ciclos entre el primer y cegundo ciclo
		                							.mapToDouble(Double::doubleValue)   // Convertimos Float a double
		                							.average()
		                							.orElse(0.0);
			   System.out.println(i+") Promedio inferior Cy5: "+ promedioInferior4); 
			   //Calcular el promedio de los ultimos 5 resultados de la lista4
			   saltar = Math.max(0, list4.size() - ultimosCiclos);
			   promedioSuperior =  list4.stream()
					  								.skip(saltar)      // salta los elementos para dejar solo los últimos n
					  								.mapToDouble(Double::doubleValue)
					  								.average()
					  								.orElse(0.0);
			   System.out.println(i+") Promedio superior Cy5: "+ promedioSuperior);
			   delta = promedioSuperior-promedioInferior4;//calcular la diferencia entre los promedios
			   System.out.println(i+ ") Delta Cy5: "+ delta);
			   
			   if(delta<1) {delta=1;} //ajustar valores menores a 1
			  
			   ListaDeltas4.add(delta);	 //cargar resultados a la lista
			   
			   
			   //VALIDAR SI HAY UN POCILLO VACIO O UNO LLENO
			   
			   double suma = promedioInferior1 + promedioInferior2+promedioInferior3+promedioInferior4;	
			   	
			   if(suma <= umbralVacio ) {
				   
				   ListaVaciosLlenos.add(false); //no hay muestra en este lugar
				   //System.out.println(" Pocillo vacio lectura: "+ " "+ suma);
			   }
			   else {ListaVaciosLlenos.add(true);
			   			
			       // System.out.println(" Pocillo lleno lectura: "+ " "+ suma);
			   } //no hay muestra en este lugar
			   			   
			  
		 }
		 
		
		 
		 //VALIDAR LOS DATOS CON LA SUMA DE DELTA1 + DELTA2
		 
		 for (int e=0; e<headers1.size(); e++) {
			 
			 
			 if(ListaVaciosLlenos.get(e)== true) {//Si hay muestra en el pocillo
				 
				 //System.out.println(headers1.get(e)+ " "+ e+ " "+ true);
				 
				 	//Dupla Fam/TexasRed
				    double c1 = ListaDeltas1.get(e);
			 		double c2 = ListaDeltas2.get(e);
			 		double sumaC1C2 = c1+c2;
			 
			 		if(sumaC1C2 < endRFU) { DatosValidados1.add(1000000d); }//esta es una marca de que los datos no son validos
			 		else {  
			 				double log =  ((Math.log(c1) - Math.log(c2)) / Math.log(2));
			 				DatosValidados1.add(log);}
			 		
			 		//Supla HEX/Cy5
			 			c1 = ListaDeltas3.get(e);
			 			c2 = ListaDeltas4.get(e);
			 			sumaC1C2 = c1+c2;
			 		//esta es una marca que le pongo indicando que los datos no son validos	(ND)
			 		if(sumaC1C2 < endRFU) { DatosValidados2.add(1000000d); }
			 		else {  
			 				double log =  ((Math.log(c1) - Math.log(c2)) / Math.log(2));
			 				DatosValidados2.add(log);}
			 		
			 
			 }	
			//esta es una marca de que el pocillo está vacio
			 else { DatosValidados1.add(2000000d);DatosValidados2.add(2000000d);} 
			 
			 
		 }
		 
		 
		 
			 
		 //PROCESAR LOS DATOS DE FEM/TEXAS-RED
		 //Primera linea
		 for ( int i =0 ; i<13; i++) { ListaResultado1.add(""+i); }		 
		 		 		
		 //LINEAS SIGUIENTES
		 for (int a = 1; a < headers1.size(); a++ ) {
			 
			 //primera letra de cada una de las lineas siguienteS
			 
			 switch (a) {
		        
			 	case 1:  ListaResultado1.add("A");
			 	break;
			 	case 13: ListaResultado1.add("B");
		         break;
		        case 25: ListaResultado1.add("C");
		         break;
		        case 37: ListaResultado1.add("D");
		         break;
		        case 49: ListaResultado1.add("E");
		         break;
		        case 61: ListaResultado1.add("F");
		         break;
		        case 73: ListaResultado1.add("G");
		         break;
		        case 85: ListaResultado1.add("H");
		         break;
		      			            
			 }
			 
			 //Agregar las LETRAS siguientes 
			 
			 //Si el posillo esta vacio
			 if(DatosValidados1.get(a)== 2000000f){ ListaResultado1.add(" ");  } 
			 //Si hay muestra en el pocillo
			 else { 
				 
				 double umbral = DatosValidados1.get(a);
				 
				 //Si los datos no son validos (ND)
				 if (umbral == 1000000f ){ListaResultado1.add("ND");}
				 //Si los datos son validos
				 else {	
					 
					 double maxVer;	 
					 double ftnn = calcularDensidadGaussiana(DatosValidados1.get(a), Media_NN_Fam, SD_NN_Fam) ;
					 double ftnp = calcularDensidadGaussiana(DatosValidados1.get(a), Media_NP_Fam, SD_NP_Fam) ;
					 double ftpp = calcularDensidadGaussiana(DatosValidados1.get(a), Media_PP_Fam, SD_PP_Fam) ;
				 
				 	 if (ftnn > ftnp && ftnn > ftpp) {
					 
				 		 maxVer = (ftnn/(ftnn+ftnp+ftpp))*100; //Maxima verosimilitud
					 
				 		 ListaResultado1.add("N/N  (" + String.format("%.2f",maxVer) +" %)");
				 	 	}
				 
				 	 if (ftnp > ftnn && ftnp > ftpp) {
					 
				 		 maxVer = (ftnp/(ftnn+ftnp+ftpp))*100; //Maxima verosimilitud
					 
				 		 ListaResultado1.add("P/N  (" + String.format("%.2f",maxVer) +" %)");
				 	 	}
				 
				 	 if (ftpp > ftnn && ftpp > ftnp) {
					 
				 		 maxVer = (ftpp/(ftnn+ftnp+ftpp))*100; //Maxima verosimilitud
					 
				 		 ListaResultado1.add("P/P  (" + String.format("%.2f",maxVer) +" %)");
				 	 	}
				 					
				 
			 		 }
				 
			 
			 	}
			 
			 
		 
		 
		 }
		 
		 //PROCESAR LOS DATOS DE HEX/CY5

		 //Primera linea
		 for ( int i =0 ; i<13; i++) {  ListaResultado2.add(""+i); }


		 //Lineas siguiente
		 for (int a = 1; a < headers1.size(); a++ ) {

			 //primera letra de cada una de las lineas siguiente

			 switch (a) {

			 	case 1:  ListaResultado2.add("A");
			 	break;
			 	case 13: ListaResultado2.add("B");
		         break;
		        case 25: ListaResultado2.add("C");
		         break;
		        case 37: ListaResultado2.add("D");
		         break;
		        case 49: ListaResultado2.add("E");
		         break;
		        case 61: ListaResultado2.add("F");
		         break;
		        case 73: ListaResultado2.add("G");
		         break;
		        case 85: ListaResultado2.add("H");
		         break;

			 }

			 //Agregar las lineas siguiente

			 //Si el posillo esta vacio
			 if(DatosValidados2.get(a)== 2000000f){ ListaResultado2.add(" ");  }
			 //Si hay muestra en el pocillo
			 else {

				 double umbral = DatosValidados2.get(a);

				 //Si los datos no son validos (ND)
				 if (umbral == 1000000f ){ListaResultado2.add("ND");}
				 //Si los datos son validos
				 else {

					 double maxVer;
					 double ftnn = calcularDensidadGaussiana(DatosValidados2.get(a), Media_NN_HEX, SD_NN_HEX) ;
					 double ftnp = calcularDensidadGaussiana(DatosValidados2.get(a), Media_NP_HEX, SD_NP_HEX) ;
					 double ftpp = calcularDensidadGaussiana(DatosValidados2.get(a), Media_PP_HEX, SD_PP_HEX) ;

				 	 if (ftnn > ftnp && ftnn > ftpp) {

				 		 maxVer = (ftnn/(ftnn+ftnp+ftpp))*100; //Maxima verosimilitud

				 		 ListaResultado2.add("N/N  (" + String.format("%.2f",maxVer) +" %)");
				 	 	}

				 	 if (ftnp > ftnn && ftnp > ftpp) {

				 		 maxVer = (ftnp/(ftnn+ftnp+ftpp))*100; //Maxima verosimilitud

				 		 ListaResultado2.add("P/N  (" + String.format("%.2f",maxVer) +" %)");
				 	 	}

				 	 if (ftpp > ftnn && ftpp > ftnp) {

				 		 maxVer = (ftpp/(ftnn+ftnp+ftpp))*100; //Maxima verosimilitud

				 		 ListaResultado2.add("P/P  (" + String.format("%.2f",maxVer) +" %)");
				 	 	}


			 		 }}
			 }

		 //MOSTRAR LOS DATOS

			genotipoCuatroMarcadores(v, ListaResultado1, ListaResultado2, ListaResultado3);
		 
		 actualizarTablasCuatroMarcadores(v.getModel1(),v.getModel2(), v.getModel3(),ListaResultado1, ListaResultado2,ListaResultado3 );
		 
		 

		 
		 
		 
	 }}}

	// Ayudante 1: Calcula el promedio de los primeros ciclos
	private double calcularPromedioBasal(List<Double> datos, int inicio, int fin) {
		return datos.stream().limit(fin - inicio + 1).mapToDouble(Double::doubleValue).average().orElse(0.0);
	}



	private void agregarLetraFila(List<String> resultado, int a) {switch (a) {
		case 1:  resultado.add("A"); break;
		case 13: resultado.add("B"); break;
		case 25: resultado.add("C"); break;
		case 37: resultado.add("D"); break;
		case 49: resultado.add("E"); break;
		case 61: resultado.add("F"); break;
		case 73: resultado.add("G"); break;
		case 85: resultado.add("H"); break;
	}
	}

	public void genotipoCuatroMarcadores(Ventana v, List<String> resultados1, List<String> resultados2, List<String> resultados3) {
		// Este metodo calcula los genotipos a travez de los resultados expuestos en las listas
		// de reusltados finales

		// Tomamos los nombre de los "alelos" de los texfiel

		String alelo1 = v.getJtf_alelo1().getText();
		String alelo2 = v.getJtf_alelo2().getText();
		String alelo3 = v.getJtf_alelo3().getText();
		String alelo4 = v.getJtf_alelo4().getText();

		  resultados3.clear();// limpiamos la lista 3

		// Recorremos las listas (que tienen 117 elementos: 9 filas * 13 columnas)
		for (int i = 0; i < resultados1.size(); i++) {
			String item1 = resultados1.get(i);
			String item2 = resultados2.get(i);

			// --- LÓGICA DE ESTRUCTURA (ENCABEZADOS) ---

			// 1. Si estamos en la primera fila (índices 0 a 12), copiamos los números
			if (i < 13) {
				resultados3.add(item1); // Agrega "0", "1", "2"...
				continue;
			}

			// 2. Si es la primera columna de las filas siguientes (A, B, C...)
			// Sabemos que son los índices 13, 26, 39, 52, 65, 78, 91, 104
			if (i % 13 == 0) {
				resultados3.add(item1); // Agrega "A", "B", etc.
				continue;
			}

			// 3. Si el pocillo está vacío en las tablas originales
			if (item1.equals(" ") || item2.equals(" ")) {
				resultados3.add(" ");
				continue;
			}

			// --- LÓGICA DE PROCESAMIENTO DE DATOS ---

			String etiquetaGenotipo1 = "";
			double valorNumerico1 = 0.0;
			String etiquetaGenotipo2 = "";
			double valorNumerico2 = 0.0;

			if (item1.equals("ND")) {
				etiquetaGenotipo1 = "ND";
				valorNumerico1 = -1.0;
			}
			if (item2.equals("ND")) {
				etiquetaGenotipo2 = "ND";
				valorNumerico2 = -1.0;
			}
			else if (item1.contains("(") || item2.contains("(")) {
				try {
					String[] partes1 = item1.split("\\(");
					String[] partes2 = item2.split("\\(");
					etiquetaGenotipo1 = partes1[0].trim();
					etiquetaGenotipo2 = partes2[0].trim();

					String soloNumero1 = partes1[1].replace("%)", "").replace(",", ".").trim();
					String soloNumero2 = partes2[1].replace("%)", "").replace(",", ".").trim();

					valorNumerico1 = Double.parseDouble(soloNumero1);
					valorNumerico2 = Double.parseDouble(soloNumero2);
				} catch (Exception e) {
					// En caso de error, etiqueta se mantiene vacía o ND
				}
			}

			// Calculamos la probabilidad total
			double p = (valorNumerico1 * valorNumerico2) / 100.0;
			String probStr = "("+String.format("%.2f", p) + " %)";

			// Procesar combinaciones
			if (etiquetaGenotipo1.equals("ND") || etiquetaGenotipo2.equals("ND")) {
				resultados3.add("ND");
			}
			else if (etiquetaGenotipo1.equals("P/P") && etiquetaGenotipo2.equals("N/N")) {
				resultados3.add(alelo1+"/"+alelo1 +" " + probStr);
			}
			else if (etiquetaGenotipo1.equals("P/N") && etiquetaGenotipo2.equals("N/N")) {
				resultados3.add(alelo1+"/*X " + probStr);
			}
			else if (etiquetaGenotipo1.equals("P/N") && etiquetaGenotipo2.equals("P/N")) {
				resultados3.add(alelo1+"/"+alelo3+ " " + probStr);
			}
			else if (etiquetaGenotipo1.equals("N/N") && etiquetaGenotipo2.equals("P/P")) {
				resultados3.add(alelo3+"/"+alelo3+ " " + probStr);
			}
			else if (etiquetaGenotipo1.equals("N/N") && etiquetaGenotipo2.equals("P/N")) {
				resultados3.add(alelo3+"/*X " + probStr);
			}
			else if (etiquetaGenotipo1.equals("N/N") && etiquetaGenotipo2.equals("N/N")) {
				resultados3.add("*X/*X " + probStr);
			}
			else {
				resultados3.add("S/D");
			}
		} // Fin del bucle for

		// --- CARGAR LA TABLA 3 ---

		// 1. Limpiar visualmente
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 13; j++) {
				v.getTabla3().setValueAt("", i, j);
			}
		}

		// 2. Llenar con la lista que acabamos de construir (que ahora tiene 117 elementos)
		int index = 0;
		for (int fila = 0; fila < 9; fila++) {
			for (int col = 0; col < 13; col++) {
				if (index < resultados3.size()) {
					v.getTabla3().setValueAt(resultados3.get(index), fila, col);
					index++;
				}
			}
		}
	}

	public void genotipoDosMarcadores(Ventana v, List<String> resultados1,  List<String> resultados3) {
		// Este metodo calcula los genotipos a travez de los resultados expuestos en las listas
		// de reusltados finales


		// Tomamos los nombre de los "alelos" de los texfiel

		String alelo1 = v.getJtf_alelo1().getText();
		//String alelo2 = v.getJtf_alelo2().getText();


		resultados3.clear();// limpiamos la lista 3

		// Recorremos las listas (que tienen 117 elementos: 9 filas * 13 columnas)
		for (int i = 0; i < resultados1.size(); i++) {
			String item1 = resultados1.get(i);


			// --- LÓGICA DE ESTRUCTURA (ENCABEZADOS) ---

			// 1. Si estamos en la primera fila (índices 0 a 12), copiamos los números
			if (i < 13) {
				resultados3.add(item1); // Agrega "0", "1", "2"...
				continue;
			}

			// 2. Si es la primera columna de las filas siguientes (A, B, C...)
			// Sabemos que son los índices 13, 26, 39, 52, 65, 78, 91, 104
			if (i % 13 == 0) {
				resultados3.add(item1); // Agrega "A", "B", etc.
				continue;
			}

			// 3. Si el pocillo está vacío en las tablas originales
			if (item1.equals(" ") ) {
				resultados3.add(" ");
				continue;
			}

			// --- LÓGICA DE PROCESAMIENTO DE DATOS ---

			String etiquetaGenotipo1 = "";
			double valorNumerico1 = 0.0;
			String etiquetaGenotipo2 = "";
			double valorNumerico2 = 0.0;

			if (item1.equals("ND")) {
				etiquetaGenotipo1 = "ND";
				valorNumerico1 = -1.0;
			}

			else if (item1.contains("(") ) {
				try {
					String[] partes1 = item1.split("\\(");

					etiquetaGenotipo1 = partes1[0].trim();


					String soloNumero1 = partes1[1].replace("%)", "").replace(",", ".").trim();


					valorNumerico1 = Double.parseDouble(soloNumero1);

				} catch (Exception e) {
					// En caso de error, etiqueta se mantiene vacía o ND
				}
			}

			// Calculamos la probabilidad total
			double p = (valorNumerico1 ) ;
			String probStr = "("+String.format("%.2f", p) + " %)";

			// Procesar combinaciones
			if (etiquetaGenotipo1.equals("ND") ) {
				resultados3.add("ND");
			}
			else if (etiquetaGenotipo1.equals("P/P") ) {
				resultados3.add(alelo1+ "/"+ alelo1+ " " + probStr);
			}
			else if (etiquetaGenotipo1.equals("P/N") ) {
				resultados3.add(alelo1+ "/*X " + probStr);
			}
			else if (etiquetaGenotipo1.equals("N/N") ) {
				resultados3.add("*X/*X " + probStr);
			}

			else {
				resultados3.add("S/D");
			}
		} // Fin del bucle for

		// --- CARGAR LA TABLA 3 ---

		// 1. Limpiar visualmente
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 13; j++) {
				v.getTabla3().setValueAt("", i, j);
			}
		}

		// 2. Llenar con la lista que acabamos de construir (que ahora tiene 117 elementos)
		int index = 0;
		for (int fila = 0; fila < 9; fila++) {
			for (int col = 0; col < 13; col++) {
				if (index < resultados3.size()) {
					v.getTabla3().setValueAt(resultados3.get(index), fila, col);
					index++;
				}
			}
		}
	}
	 public void exportarResultadosDesdeVentana(Ventana v) {
		 
		 String str = v.getJtf_nombre().getText();
		 String lista1 = "_resultado_"+ v.getComboSonda1().getSelectedItem().toString()+"_"+v.getComboSonda2().getSelectedItem().toString()+"_";
		 String lista2 = "_resultado_"+ v.getComboSonda3().getSelectedItem().toString()+"_"+v.getComboSonda4().getSelectedItem().toString()+"_";

		 exportarListaACsv(v.getComboModoMarcadores(), ListaResultado1,str+lista1, ListaResultado2,str+lista2, ListaResultado3,str+"_resultado_Genotipos_");
		 
	 }
	 
	 public void limpiarTabla(DefaultTableModel model1,DefaultTableModel model2, DefaultTableModel model3) {
		 
		// 1. Limpiar datos anteriores (ponemos un string vacío en todas las celdas)
		    for (int i = 0; i < 9; i++) {
		        for (int j = 0; j < 13; j++) {
		            model1.setValueAt("", i, j);
		            model2.setValueAt("", i, j);
					model3.setValueAt("", i, j);
		        }
		    }
	 }


	public void actualizarTablasDosMarcadores(DefaultTableModel model1, DefaultTableModel model3, List<String> lista1, List<String> lista3) {
		// 1. Limpiar datos anteriores (ponemos un string vacío en todas las celdas)
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 13; j++) {
				model1.setValueAt("", i, j);
				model3.setValueAt("", i, j);
			}
		}

		// 2. Llenar con los nuevos datos
		int index = 0;
		for (int fila = 0; fila < 9; fila++) {
			for (int col = 0; col < 13; col++) {
				if (index < lista1.size()) {
					model1.setValueAt(lista1.get(index), fila, col);
					model3.setValueAt(lista3.get(index), fila, col);
					index++;
				}
			}
		}
	}


	public void actualizarTablasCuatroMarcadores(DefaultTableModel model1,DefaultTableModel model2, DefaultTableModel model3, List<String> lista1, List<String> lista2, List<String> lista3) {
		    // 1. Limpiar datos anteriores (ponemos un string vacío en todas las celdas)
		    for (int i = 0; i < 9; i++) {
		        for (int j = 0; j < 13; j++) {
		            model1.setValueAt("", i, j);
		            model2.setValueAt("", i, j);
					model3.setValueAt("", i, j);
		        }
		    }

		    // 2. Llenar con los nuevos datos
		    int index = 0;
		    for (int fila = 0; fila < 9; fila++) {
		        for (int col = 0; col < 13; col++) {
		            if (index < lista1.size()) {
		                model1.setValueAt(lista1.get(index), fila, col);
		                model2.setValueAt(lista2.get(index), fila, col);
						model3.setValueAt(lista3.get(index), fila, col);
		                index++;
		            }
		        }
		    }
		}




	public String lineaBase(int n) {
		if (n <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append("-");
		}
		return sb.toString();
	}

	public String textoNormalizado(String str, int n) {
		// Manejo de caso nulo
		if (str == null) str = "";

		// Si el texto ya es más largo o igual a n, devolvemos el texto tal cual
		if (str.length() >= n) {
			return "|" + str + "";
		}

		// Calculamos el espacio total de relleno necesario
		int paddingTotal = n - str.length();

		int leftPadding = paddingTotal / 2;
		int rightPadding = paddingTotal - leftPadding;

		// --- REEMPLAZO DE .repeat() PARA JAVA 8 ---
		StringBuilder sbLeft = new StringBuilder();
		for (int i = 0; i < leftPadding; i++) {
			sbLeft.append(" ");
		}

		StringBuilder sbRight = new StringBuilder();
		for (int i = 0; i < rightPadding; i++) {
			sbRight.append(" ");
		}
		// ------------------------------------------

		// Construimos el resultado final usando los StringBuilders
		return "|" + sbLeft.toString() + str + sbRight.toString() + "";
	}
	 /**
	  * Calcula la DENSIDAD de probabilidad (PDF) de la distribución normal
	  * en el punto 'resultado'. Esto es la "frecuencia" o altura de la curva gaussiana.
	  */
	 public double calcularDensidadGaussiana(double resultado, double media, double sd) {
	     
	     if (sd <= 0) {
	         throw new IllegalArgumentException("La desviación estándar (sd) debe ser mayor que 0");
	     }

	     double z = (resultado - media) / sd;                    // z-score
	     double exponente = -0.5 * z * z;
	     
	     double densidad = (1.0 / (sd * Math.sqrt(2.0 * Math.PI))) 
	                       * Math.exp(exponente);

	     return  densidad;
	 }
	 
	 /**
	  * Función Error (erf) - aproximación precisa
	  */
	 private double erf(double x) {
	     // Constantes para una aproximación muy precisa
	     double t = 1.0 / (1.0 + 0.3275911 * Math.abs(x));
	     double y = 1.0 - (((((1.061405429 * t - 0.453352027) * t) 
	                         + 1.421413741) * t - 1.453152027) * t + 0.254829592) * t 
	                         * Math.exp(-x * x);
	     
	     return (x >= 0) ? y : -y;
	 }
	 
	 
	 public void exportarListaACsv(JComboBox jComboBox, java.util.List<String> datos1, String nombre1, java.util.List<String> datos2, String nombre2, java.util.List<String> datos3, String nombre3) {

		 if (jComboBox.getSelectedIndex() == 0){
		 // 1. Validación inicial
		    if (datos1 == null || datos1.size() != 117 || datos3.size() != 117) {
		        JOptionPane.showMessageDialog(null, "Error: Las listas deben tener exactamente 117 elementos.", "Error", JOptionPane.ERROR_MESSAGE);
		        return;
		    }}

		 else if (jComboBox.getSelectedIndex() == 1){
			 // 1. Validación inicial
			 if (datos1 == null || datos1.size() != 117 || datos2 == null || datos2.size() != 117 || datos3.size() != 117) {
				 JOptionPane.showMessageDialog(null, "Error: Las listas deben tener exactamente 117 elementos.", "Error", JOptionPane.ERROR_MESSAGE);
				 return;
			 }}

		    // 2. Elegir carpeta
		    JFileChooser chooser = new JFileChooser();
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setDialogTitle("Seleccione la carpeta donde guardar los archivos");

		    int result = chooser.showSaveDialog(null);
		    if (result != JFileChooser.APPROVE_OPTION) {
		        return; // El usuario canceló la selección
		    }

		    File carpetaDestino = chooser.getSelectedFile();
		    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

		    // 3. Intento de escritura
		    try {
		        // Usamos un método auxiliar para no repetir código
		        escribirCsv(new File(carpetaDestino, nombre1 + timestamp + ".csv"), datos1);
				if (jComboBox.getSelectedIndex()==1){
		       			 escribirCsv(new File(carpetaDestino, nombre2 + timestamp + ".csv"), datos2);}
				escribirCsv(new File(carpetaDestino, nombre3 + timestamp + ".csv"), datos3);

		        // Mensaje de éxito
		        JOptionPane.showMessageDialog(null, "Los archivos se guardaron correctamente en:\n" + carpetaDestino.getAbsolutePath(), 
		                                      "Éxito", JOptionPane.INFORMATION_MESSAGE);

		    } catch (IOException e) {
		        // Mensaje de error
		        JOptionPane.showMessageDialog(null, "Ocurrió un error al guardar los archivos: " + e.getMessage(), 
		                                      "Error", JOptionPane.ERROR_MESSAGE);
		        e.printStackTrace();
		    }
		}

		// Método auxiliar para evitar repetir la lógica de escritura
		private void escribirCsv(File archivo, java.util.List<String> datos) throws IOException {
		    try (PrintWriter writer = new PrintWriter(archivo)) {
		        int index = 0;
		        for (int i = 0; i < 9; i++) {
		            for (int j = 0; j < 13; j++) {
		                writer.print(datos.get(index++));
		                if (j < 12) {
		                    writer.print(";");
		                }
		            }
		            writer.println();
		        }
		    }
		}
	 
	 public float extraerFloatDeTextField(javax.swing.JTextField campo) {
		    try {
		        // Obtenemos el texto, eliminamos espacios innecesarios
		        String texto = campo.getText().trim();
		        
		        // Convertimos a float
		        return Float.parseFloat(texto);
		        
		    } catch (NumberFormatException e) {
		        // En caso de que el usuario no escriba un número válido
		       // System.err.println("Error: El campo no contiene un número válido.");
		        // Retornamos un valor por defecto o manejamos el error según tu lógica
		        return 0.0f; 
		    }
		}

}
	
	
	

