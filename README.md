GenotipificaZionDQB1

This program is a modified version of the GenotipificaZion software, designed specifically for the analysis of the DQB1 locus.

GenotipificaZion is a Java-based tool designed to automate the genotyping analysis of various molecular markers using real-time PCR with allele-specific hydrolysis probes. Its architecture allows for processing a wide range of genetic variants using maximum likelihood models.

Key Features

a) Universality: Ability to analyze different molecular markers, not limited solely to DQB1 alleles.

b) Configuration Flexibility: Supports analysis using two probes or expanded configurations of up to four labeled probes (e.g., FAM, HEX, TexasRED, Cy5).

c) Batch Processing: Automatically loads and analyzes CSV files exported directly from the thermal cycler.

d) High Precision: Employs statistical calculations based on reference data to ensure reliable genotype assignments.

Analysis Methodology

The program implements a rigorous logical workflow based on raw fluorescence processing:

1. Calculation of Net Fluorescence (EndRFU): For each well (PCR reaction) and color channel, the final fluorescence is calculated by subtracting the baseline average (cycles 10 to 20) from the final signal average (cycles 40 to 45).

2. Negative Value Normalization: EndRFU values under 1 are automatically set to 1 to prevent errors in subsequent logarithmic calculations.

3. Quality Control: Samples with insufficient amplification or weak signal (sum of EndRFU < 150) are automatically filtered out and classified as "ND" (Not Determined).

   If EndRFU_A + EndRFU_B < 150 ⟹ Result = "ND"

4. Calculation of Base 2 Logarithm (Log 2) of the Allelic Ratio: The relationship between probe signals recognizing different alleles is determined using the formula:

   Ratio = log₂ ( EndRFU_Probe_A / EndRFU_Probe_B )

5. Maximum Likelihood Assignment: The software uses a normal distribution function with reference Mean and Standard Deviation (SD) values to calculate the likelihood of possible genotypes (homozygous and heterozygous).

   L(x│μ,σ) = [ 1 / (σ·√2π) ] · e^[-½((x-μ)/σ)²]

   This uses the Mean (μ) and SD (σ) values obtained from reference samples.  
   x: The calculated logarithmic ratio value.  
   μ and σ: The mean and standard deviation obtained from reference samples for each genotype. Likelihood values are normalized to 100%, assigning the genotype with the highest confidence value.

6. Phenotype Assignment: Finally, the three likelihood values are adjusted to a 100% total, and the highest value is assigned as the genotype along with its corresponding confidence level.

Input Data Requirements

For GenotipificaZion to process data correctly, thermal cycler files must be exported with the following parameters:

  > Baseline subtraction: Disabled.  
  > Format: CSV (semicolon delimited).  
  > Required files: One amplification result file per channel used (e.g., Quantification Amplification Results_FAM.csv).  

Technical Validation

The robustness of this algorithm was originally validated with 55 direct saliva samples for the DQB1 marker (https://doi.org/10.64898/2026.05.19.26353109), demonstrating: 100% concordance with purified DNA methods and manual analysis. Confidence levels exceeding 95% in all assignments, with 94.5% of samples reaching 100% confidence.

Note: This software is an automated analysis tool for molecular biology laboratories seeking to standardize and accelerate the interpretation of genotyping data.
