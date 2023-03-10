# MCRO-Software : Model Card Report Ontology Software (library and sample application)

## Description
This repository contains an experimental Java library to manage and generate data for model card reports using the Model Card Reports Ontology (MCRO) to formalize the structure of an ontology-based model card report. 

### Java library (mco-java)
https://github.com/UTHealth-Ontology/MCRO-Software/tree/main/mco-java

### Sample application utilizing the library (mco-java-app)
https://github.com/UTHealth-Ontology/MCRO-Software/tree/main/mco-java-app


#### For more information about the Model Card Reports Ontology:

Amith, M.T., Cui, L., Zhi, D. et al. Toward a standard formal semantic representation of the model card report. BMC Bioinformatics 23 (Suppl 6), 281 (2022). https://doi.org/10.1186/s12859-022-04797-6

## Build instructions

The library and the sample application was developed in Netbeans 16 with Java 11. Through Netbeans, you can File > Open Project and select the mco-java folder and/or the mco-java-app. Select the parent node of the project and use the "Clean and Build" button (or through the Run menu). 

Note that mco-java-app has a dependency on the library (mco-java), so you'll have to build the library first in order to build the sample software application. You may have to add a dependency if it does not register (right click the Dependencies node and select Add Dependency..)


## Creators and contributors

- Tuan Amith
- Licong Cui
- Kirk Roberts
- Cui Tao

## Acknowledgements

 This research was supported by NIH grants under Award Numbers R56AI150272, RF1AG072799, R56AI150272, R56AG074604, and T15LM007093
 
