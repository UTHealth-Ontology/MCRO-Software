/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uth.sbmi.mco.java.query;

import java.util.Set;
import static java.util.stream.Collectors.toSet;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;

/**
 *
 * 
 */
public class DLQueryDriver {
    
    private OWLOntology ontology;
    private BidirectionalShortFormProvider bidiShortFormProvider;
    
    private static DLQueryDriver INSTANCE = null;
    
    private OWLReasoner reasoner;
    
    public synchronized static DLQueryDriver getInstance(){
        
        if(INSTANCE == null){
            INSTANCE = new DLQueryDriver();
        }
        
        return INSTANCE;
        
    }
    
    private DLQueryDriver(){
        
    }
    
    public void setupDLQueryDriver(OWLReasoner reasoner, ShortFormProvider shortFormProvider){
        this.reasoner = reasoner;
        this.ontology = reasoner.getRootOntology();
        
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        
        Set<OWLOntology> importsClosure  = ontology.importsClosure().collect(toSet());
        
        bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(
                manager, importsClosure, shortFormProvider);
        
    }
    
    public Set<OWLClass> getDirectSuperClasses(String queryExpression){
        
        OWLClassExpression expression = this.parseClassExpression(queryExpression);
        
        NodeSet<OWLClass> results = reasoner.getSuperClasses(expression, true);
        
        return results.entities().collect(toSet());
        
    }
    
    public Set<OWLClass> getDirectSubClasses(String queryExpression){
        OWLClassExpression expression = this.parseClassExpression(queryExpression);
        
        NodeSet<OWLClass> results = reasoner.getSubClasses(expression, true);
        
        return results.entities().collect(toSet());
    }

    private OWLClassExpression parseClassExpression(String classExpressionString) {
        OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(dataFactory, classExpressionString);
        parser.setDefaultOntology(ontology);
        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
        parser.setOWLEntityChecker(entityChecker);

        return parser.parseClassExpression();

    }
    
    
}
