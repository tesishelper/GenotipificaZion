GenotipificaZionDQB1

Este programa es una versión modificada del programa GenotipificaZion, concebida para el análisis del locus DQB1.


GenotipificaZion es una herramienta desarrollada en Java diseñada para la automatización del análisis de genotipificación de diversos marcadores moleculares mediante PCR de tiempo real con sondas de hidrólisis alelo-específicas, con una arquitectura que permite procesar una amplia gama de variantes genéticas utilizando modelos de máxima verosimilitud.

Características Principales

a) Universalidad: Capacidad de analizar diferentes marcadores moleculares, no limitándose únicamente a alelos DQB1.

b) Flexibilidad de Configuración: Soporta análisis utilizando dos sondas o configuraciones ampliadas de hasta cuatro sondas marcadas (ej. FAM, HEX, TexasRED, Cy5).

c) Procesamiento por Lotes: Carga y analiza automáticamente archivos CSV exportados directamente desde el termociclador.

d) Alta Precisión: Emplea cálculos estadísticos basados en datos de referencia para garantizar asignaciones de genotipos confiables.

Metodología de Análisis

El programa implementa un flujo de trabajo lógico riguroso basado en el procesamiento de fluorescencia cruda:

1. Cálculo de la fluorescencia neta (EndRFU): Para cada pocillo (reacción de PCR) y canal de color, se calcula la fluorescencia final restando el promedio de la línea base (ciclos 10 a 20) al promedio de la señal final (ciclos 40 a 45).

2. Normalización de Valores negativos: Los valores de EndRFU menores a 1 se fijan automáticamente en 1 para evitar errores en cálculos logarítmicos posteriores.

3. Control de Calidad: Se filtran automáticamente las muestras con amplificación insuficiente o señal débil (suma de EndRFU \< 150), clasificándolas como "ND" (No Determinado).

```
     Si EndRFU\_A + EndRFU\_B \< 150 ⟹ Resultado = "ND"
```

4. Cálculo de logaritmo en base 2 (Log 2) de la relación alélica: Se determina la relación entre las señales de las sondas que reconocen diferentes alelos mediante la fórmula

```
     Relación = log₂ ( EndRFU\_Sonda\_A / EndRFU\_Sonda\_B )
```

5. Asignación por Máxima Verosimilitud: El software utiliza una función de distribución normal con valores de Media y Desviación Estándar (SD) de referencia para calcular la verosimilitud de los genotipos posibles (homocigotos y heterocigotos).

```
     L(x│μ,σ) = \[ 1 / (σ·√2π) \] · e^\[-½((x-μ)/σ)²\]
```

Esta emplea los valores de Media (μ) y SD (σ) obtenidos en muestras de referencia.  
x: Es el valor de la relación logarítmica calculada.  
μ y σ: Son la media y la desviación estándar obtenidas de las muestras de referencia para cada genotipo. Los valores de verosimilitud se normalizan al 100%, asignando el genotipo con mayor valor de confianza.

1. Asignación de fenotipos: Finalmente, los tres valores de verosimilitud son ajustados a 100% total y el valor más alto es el genotipo asignado con su correspondiente valor de confianza.

Requisitos de los Datos de Entrada

Para que GenotipificaZion procese los datos correctamente, los archivos del termociclador deben exportarse con los siguientes parámetros:

```
  \>Sustracción de línea base: Desactivada.   
  \>Formato: CSV (delimitado por punto y coma).   
  \>Archivos requeridos: Un archivo de resultados de amplificación por cada canal utilizado (ej. Quantification Amplification Results\_FAM.csv). 
```

Validación Técnica

La robustez de este algoritmo fue validada originalmente con 55 muestras de saliva directa para el marcador DQB1 ([https://doi.org/10.64898/2026.05.19.26353109](https://doi.org/10.64898/2026.05.19.26353109)), demostrando: Una concordancia del 100% con métodos de ADN purificado y análisis manual. Niveles de confianza superiores al 95% en todas las asignaciones, con un 94.5% de las muestras alcanzando el 100% de confianza.

Nota: Este programa es una herramienta de análisis automatizado para laboratorios de biología molecular que buscan estandarizar y acelerar la interpretación de datos de genotipificación.

