/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uth.sbmi.mco.java;

import edu.uth.sbmi.mco.java.models.ClassData;
import edu.uth.sbmi.mco.java.ontology.OntologyManagement;
import java.util.List;

/**
 *
 * @author tuan
 */
public class McoJavaMain {
    
    private static McoJavaMain INSTANCE = null;
    
    private OntologyManagement ontology_management = null;
    
    public synchronized static McoJavaMain getInstance(){
        if(INSTANCE == null){
            INSTANCE = new McoJavaMain();
        }
        
        return INSTANCE;
    }
    
    public McoJavaMain(){
        ontology_management = OntologyManagement.getInstance();
        ontology_management.initOntologyManager();
    }
    
    public List<ClassData> get_model_card_concepts (){
        return ontology_management.retrieveMCOClasses();
    }
    
    public List<ClassData> get_licenses(){
        return ontology_management.retrieveLicenses();
    }
    
    public List<ClassData> get_doc_parts(){
        return ontology_management.retrieveIAODocumentParts();
    }
    
    public List<ClassData> get_algorithms(){
        return ontology_management.retrieveAlgrothimClasses();
    }
    
    public void add_annotated_text(String text, String class_type){
        ontology_management.addUserClassInstance(text, class_type);
    }
    
    public void generateDocument(){
        ontology_management.generateNetwork();
        
    }
    
 
    
    public static void main(String[] args) {
        
    }
}
