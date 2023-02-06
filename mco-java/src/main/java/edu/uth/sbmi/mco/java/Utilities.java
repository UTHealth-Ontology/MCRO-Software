/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uth.sbmi.mco.java;

import edu.uth.sbmi.mco.java.ontology.mcoIRI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;

/**
 *
 * @author tuan
 */
public class Utilities {
    
    
    
    private static Utilities INSTANCE = null;
    
    public synchronized static Utilities getInstance(){
        
        if (INSTANCE == null) {
            INSTANCE = new Utilities();
        }
        return INSTANCE;
        
    }
    
    private Utilities() {
    }
    
    public String getCommentForClass(OWLClass owlclass, OWLOntology ontology, OWLDataFactory owlfactory){
        List<String> collect = EntitySearcher.getAnnotations(owlclass, ontology, owlfactory.getRDFSComment())
                .filter(p->p.getValue() instanceof OWLLiteral).
                map(a->(OWLLiteral)a.getValue()).
                map(l->l.getLiteral()).
                collect(Collectors.toList());
        
        return (StringUtils.join(collect, ". "));
        
    }
    
    public String getDefinitionForClass(OWLClass owlclass, OWLOntology ontology, OWLDataFactory owlfactory){
        
        StringBuilder definition = new StringBuilder();
        
        OWLAnnotationProperty documentation_annotation = owlfactory.getOWLAnnotationProperty(mcoIRI.definition_iri);
       
        EntitySearcher.getAnnotations(owlclass, ontology, documentation_annotation).forEach(
                p->{
                    
                    if(p.getProperty().getIRI().equals(mcoIRI.definition_iri)){
                        
                        definition.append(p.getValue().asLiteral().get());
                    }
                    
                }
        );
        
        return definition.toString();
        
    }
    
    public String getDocumentationForClass(OWLClass owlclass, OWLOntology ontology, OWLDataFactory owlfactory){
        //owlfactory.getOWLDataProperty(documentation_iri);
        
        OWLAnnotationProperty documentation_annotation = owlfactory.getOWLAnnotationProperty(mcoIRI.documentation_iri);
        EntitySearcher.getAnnotations(owlclass, ontology).forEach(
                p->{
                    
                    p.annotations(documentation_annotation).forEach(
                            d->{
                                System.out.println(d.annotationValue().asLiteral().toString());
                            }
                    );
                }
        );
        
        return null;
    }
    
    public String getLabelForClass(OWLClass owlclass, OWLOntology ontology, OWLDataFactory owlfactory){
        
      

        List<String> collect = EntitySearcher.getAnnotations(owlclass, ontology, owlfactory.getRDFSLabel()).
                filter(p-> p.getValue() instanceof OWLLiteral ).
                map(a->(OWLLiteral)a.getValue()).
                map(l->l.getLiteral()).
                collect(Collectors.toList());

        return StringUtils.join(collect, " , ");
        
        
    }
    
    public String getLabelForIndividual(OWLNamedIndividual i, OWLOntology ontology, OWLDataFactory owlfactory){
        
        

        List <String> collect = EntitySearcher.getAnnotations(i, ontology, owlfactory.getRDFSLabel())
                .filter(p-> p.getValue() instanceof OWLLiteral)
                .map(a->(OWLLiteral)a.getValue())
                .map(l->l.getLiteral())
                .collect(Collectors.toList());

        return StringUtils.join(collect, " , ");
        
    }
    
    public static void main(String[] args) {
        
    }
}
