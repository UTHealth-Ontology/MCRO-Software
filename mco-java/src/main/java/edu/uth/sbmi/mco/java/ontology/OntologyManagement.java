/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uth.sbmi.mco.java.ontology;

import edu.uth.sbmi.mco.java.Utilities;
import edu.uth.sbmi.mco.java.models.ClassData;
import edu.uth.sbmi.mco.java.query.DLQueryDriver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toSet;
import org.apache.commons.lang3.RandomStringUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RDFJsonLDDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.InferenceDepth;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import uk.ac.manchester.cs.jfact.JFactFactory;

/**
 *
 * @author tuan
 */
public class OntologyManagement {

    private static OntologyManagement INSTANCE = null;
    
    private OWLOntology ontology;
    private OWLDataFactory owlfactory;
    private OWLOntologyManager ontologyManager;

    private OWLReasonerFactory reasonerFactory = null;
    private OWLReasoner jfact;

    private OntologyManagement() {

    }

    public synchronized static OntologyManagement getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OntologyManagement();
        }
        
        
        
        return INSTANCE;
    }

    public OWLDataFactory getFactory() {
        return ontologyManager.getOWLDataFactory();
    }

    public OWLOntologyManager getOWLManager() {
        return this.ontologyManager;
    }

    public void initOntologyManager() {
        ontologyManager = OWLManager.createConcurrentOWLOntologyManager();
        try {
            ontology = ontologyManager.loadOntology(mcoIRI.sourceOntology);
            

        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(OntologyManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        reasonerFactory = new JFactFactory();
        jfact = reasonerFactory.createReasoner(ontology, new SimpleConfiguration(50000));
        jfact.precomputableInferenceTypes();
        owlfactory = ontologyManager.getOWLDataFactory();
    }

    private void refreshReasoner() {
        jfact = reasonerFactory.createReasoner(ontology, new SimpleConfiguration(50000));
        jfact.precomputableInferenceTypes();
        owlfactory = ontologyManager.getOWLDataFactory();
    }

    /**
     * *SEARCH FUNCTION*
     */
    
    public List<ClassData> retrieveLicenses(){
        ArrayList<ClassData> list_classes = new ArrayList<ClassData>();
        
        
        
        Optional<OWLOntology> swo = ontology.importsClosure().filter(o->o.getOntologyID().match(mcoIRI.sourceSWO)).findFirst();
        
        
        Utilities util = Utilities.getInstance();
        OWLClass licenseClass = owlfactory.getOWLClass(mcoIRI.license);
        
        Set<OWLClass> licenses = jfact.getSubClasses(licenseClass, InferenceDepth.ALL).entities().collect(toSet());
        
        for(OWLClass l: licenses){
            ClassData class_data = new ClassData();
            String labelForClass = util.getLabelForClass(l, swo.get(), owlfactory);
            String definitionForClass = util.getDefinitionForClass(l, swo.get(), owlfactory);
            
            
            class_data.setHuman_friendly_name(labelForClass);
            class_data.setClass_iri(l.getIRI());
            class_data.setTip_text(definitionForClass);
            list_classes.add(class_data); 
        }
        
        return list_classes;
    }
    
    public List<ClassData>retrieveIAODocumentParts(){
        
        ArrayList<ClassData> list_classes = new ArrayList<ClassData>();
        
        Optional<OWLOntology> iao = ontology.importsClosure().filter(o->o.getOntologyID().match(mcoIRI.sourceIAO)).findFirst();
        
        Utilities util = Utilities.getInstance();
        OWLClass docpart = owlfactory.getOWLClass(mcoIRI.document_parts);
        
        Set<OWLClass> docparts = jfact.getSubClasses(docpart, InferenceDepth.ALL).entities().
                filter(d-> !d.getIRI().getNamespace().matches(mcoIRI.base)).
                collect(toSet());
        
        for(OWLClass dp : docparts){
            ClassData class_data = new ClassData();
            String labelForClass = util.getLabelForClass(dp, iao.get(), owlfactory);
            String definitionForClass = util.getDefinitionForClass(dp, iao.get(), owlfactory);
            
            
            class_data.setHuman_friendly_name(labelForClass);
            class_data.setClass_iri(dp.getIRI());
            class_data.setTip_text(definitionForClass);
            
            list_classes.add(class_data);
        }
        
        //System.out.println(list_classes.size());
        
        return list_classes;
    }
    
    public List<ClassData> retrieveAlgrothimClasses(){
        
        ArrayList<ClassData> list_classes = new ArrayList<ClassData>();
        
        Optional<OWLOntology> swo = ontology.importsClosure().filter(o->o.getOntologyID().match(mcoIRI.sourceSWO)).findFirst();
        

        Utilities util = Utilities.getInstance();
        OWLClass algoClass = owlfactory.getOWLClass(mcoIRI.algorithm);
        
        Set<OWLClass> algoClasses = jfact.getSubClasses(algoClass, InferenceDepth.ALL).entities().collect(toSet());
        
        for(OWLClass ac : algoClasses){
            ClassData class_data = new ClassData();
            String labelForClass = util.getLabelForClass(ac, swo.get(), owlfactory);
            String definitionForClass = util.getDefinitionForClass(ac, swo.get(), owlfactory);
            
            class_data.setHuman_friendly_name(labelForClass);
            class_data.setClass_iri(ac.getIRI());
            class_data.setTip_text(definitionForClass);
            
            list_classes.add(class_data);
            
        }
        
        return list_classes;
    }
    
    //class name and its alternative label
    public List<ClassData> retrieveMCOClasses() {

        ArrayList<ClassData> list_classes = new ArrayList<ClassData>();
        

        Utilities util = Utilities.getInstance();
        ontology.classesInSignature(Imports.EXCLUDED).forEach(cl -> {

            if (cl.toString().contains(mcoIRI.base)) {
                ClassData class_data = new ClassData();
                String labelForClass = util.getLabelForClass(cl, ontology, owlfactory);
                util.getCommentForClass(cl, ontology, owlfactory);

                class_data.setClass_iri(cl.getIRI());
                class_data.setTip_text(util.getDefinitionForClass(cl, ontology, owlfactory).trim());
                class_data.setTip_text(util.getCommentForClass(cl, ontology, owlfactory).trim());
                class_data.setHuman_friendly_name(labelForClass);
                
                
                list_classes.add(class_data);
            }


        });

        return list_classes;
    }

    /**
     * * ADD AS INSTANCE **
     */
    public void addUserClassInstance(String documentation, String classtype) {

        //create individual
        OWLNamedIndividual i = owlfactory.getOWLNamedIndividual(
                IRI.create(mcoIRI.base + "inst_" + classtype.replaceAll(" ", "_") + "_" + RandomStringUtils.randomAlphanumeric(12))
        );

        //create individual's class
        OWLClass i_class = owlfactory.getOWLClass(
                IRI.create(mcoIRI.base + classtype)
        );

        //add class assertion
        OWLClassAssertionAxiom oca = owlfactory.getOWLClassAssertionAxiom(i_class, i);

        this.ontologyManager.addAxiom(ontology, oca);

        //create documentation
        OWLLiteral doc_literal = owlfactory.getOWLLiteral(documentation);
        OWLAnnotation owlAnnotation = owlfactory.getOWLAnnotation(
                owlfactory.getOWLAnnotationProperty(mcoIRI.documentation_iri), doc_literal);

        //add annotation text (documentation) to instance
        OWLAxiom annotateAxiom = owlfactory.getOWLAnnotationAssertionAxiom(i.asOWLNamedIndividual().getIRI(), owlAnnotation);

        this.ontologyManager.applyChange(new AddAxiom(ontology, annotateAxiom));

    }
    
    public void addUserClassInstance(String documentation, String classtype ,IRI iri){
        //create individual
        OWLNamedIndividual i = owlfactory.getOWLNamedIndividual(
                IRI.create(mcoIRI.base + "inst_" + classtype.replaceAll(" ", "_") + "_" + RandomStringUtils.randomAlphanumeric(12))
        );
        
        OWLClass i_class = owlfactory.getOWLClass(iri);
        
        //add class assertion
        OWLClassAssertionAxiom oca = owlfactory.getOWLClassAssertionAxiom(i_class, i);

        this.ontologyManager.addAxiom(ontology, oca);

        //create documentation
        OWLLiteral doc_literal = owlfactory.getOWLLiteral(documentation);
        OWLAnnotation owlAnnotation = owlfactory.getOWLAnnotation(
                owlfactory.getOWLAnnotationProperty(mcoIRI.documentation_iri), doc_literal);

        //add annotation text (documentation) to instance
        OWLAxiom annotateAxiom = owlfactory.getOWLAnnotationAssertionAxiom(i.asOWLNamedIndividual().getIRI(), owlAnnotation);

        this.ontologyManager.applyChange(new AddAxiom(ontology, annotateAxiom));
        
    }
    
    public void addUserClassInstanceCombinedIds(String documentation, String combinedIdentifiers){
        //System.out.println("checking here..." + combinedIdentifiers);
        String [] identifiers = combinedIdentifiers.split(",");
        //Ex. AWS algorithm | http://www.ebi.ac.uk/swo/SWO_1100015,MCR algorithm | http://www.ebi.ac.uk/swo/SWO_0000278
        
        //create individual
        OWLNamedIndividual i = owlfactory.getOWLNamedIndividual(
                IRI.create(mcoIRI.base + "inst_" + RandomStringUtils.randomAlphanumeric(12))
        );
        
        
        
        //add class assertion for multiple identifers
        for(String identifier : identifiers){
            String [] ids = identifier.split("\\|");
            IRI iri = IRI.create(ids[1].trim()); 
            OWLClass i_class = owlfactory.getOWLClass(iri);
            OWLClassAssertionAxiom oca = owlfactory.getOWLClassAssertionAxiom(i_class, i);
            this.ontologyManager.addAxiom(ontology, oca);
        }
        
        //create documentation
        OWLLiteral doc_literal = owlfactory.getOWLLiteral(documentation);
        OWLAnnotation owlAnnotation = owlfactory.getOWLAnnotation(
                owlfactory.getOWLAnnotationProperty(mcoIRI.documentation_iri), doc_literal);

        //add annotation text (documentation) to instance
        OWLAxiom annotateAxiom = owlfactory.getOWLAnnotationAssertionAxiom(i.asOWLNamedIndividual().getIRI(), owlAnnotation);

        this.ontologyManager.applyChange(new AddAxiom(ontology, annotateAxiom));
        
    }

    public OWLNamedIndividual addAdHocClassInstance(String classtype) {
        //create individual
        OWLNamedIndividual i = owlfactory.getOWLNamedIndividual(
                IRI.create(mcoIRI.base + "inst_adhoc_" + classtype + "_" + RandomStringUtils.randomAlphanumeric(12))
        );

        //create individual's class
        OWLClass i_class = owlfactory.getOWLClass(
                IRI.create(mcoIRI.base + classtype)
        );

        //add class assertion
        OWLClassAssertionAxiom oca = owlfactory.getOWLClassAssertionAxiom(i_class, i);

        this.ontologyManager.addAxiom(ontology, oca);

        return i;
    }

    public void generateNetwork() {

        this.refreshReasoner();

        DLQueryDriver dlq = DLQueryDriver.getInstance();
        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
        dlq.setupDLQueryDriver(jfact, shortFormProvider);

        System.out.println("FUNCTION: Generate network \n");

        Set<OWLNamedIndividual> instances = null;

        Set<OWLClass> mco_classes = ontology.classesInSignature(Imports.EXCLUDED).
                filter(cl -> cl.toString().contains(mcoIRI.base)).collect(toSet());

        for (OWLClass mcoc : mco_classes) {

            instances = jfact.getInstances(mcoc, true).entities().collect(toSet());
            if (instances.size() > 0) {

                //DIRECT SUBCLASS: 'has part' some [class]
                String subclass_expression = mcoIRI.has_part.getFragment() + " some " + mcoc.asOWLClass().getIRI().getFragment().toString();
                //System.out.println("\t" + subclass_expression);

                Set<OWLClass> directSubClasses = dlq.getDirectSubClasses(subclass_expression);
                for (OWLClass subclass : directSubClasses) {
                    if (directSubClasses.size() > 1 && !subclass.toStringID().contains("Model_Card_Report")) {

                        Set<OWLIndividual> individualsOfClass = this.getIndividualsOfClass(subclass);
                        
                        if (individualsOfClass.size() > 0) 
                        {
                            OWLObjectProperty has_part_property = this.owlfactory.getOWLObjectProperty(mcoIRI.has_part);
                            for(OWLIndividual o : individualsOfClass)
                            {
                                for(OWLNamedIndividual oc : instances)
                                {
                                    OWLObjectPropertyAssertionAxiom op = owlfactory.getOWLObjectPropertyAssertionAxiom(has_part_property, o, oc);
                                    this.ontologyManager.addAxiom(ontology, op);
                                }
                            }

                            
                        } 
                        else //fall back method IF an target instance DOES NOT EXIST
                        {
                            System.out.println("\t ...need to create a new instance for " + subclass.toStringID());
                            OWLNamedIndividual addAdHocClassInstance = this.addAdHocClassInstance(subclass.asOWLClass().getIRI().getFragment());

                            OWLObjectProperty has_part_property = this.owlfactory.getOWLObjectProperty(mcoIRI.has_part);

                            
                            
                            for (OWLNamedIndividual oc : instances) {
                                System.out.println("\t" + oc.toString());
                                OWLObjectPropertyAssertionAxiom op = owlfactory.getOWLObjectPropertyAssertionAxiom(has_part_property, addAdHocClassInstance, oc);
                                this.ontologyManager.addAxiom(ontology, op);
                            }

                        }
                    }
                    else if(directSubClasses.size() == 1 && subclass.toStringID().contains("Model_Card_Report")){
                       
                        //TODO: Write linkage for root node
                        
                    }

                }

            }

        }
        
        System.out.println("Finished Generating network");

    }

    private Set<OWLIndividual> getIndividualsOfClass(OWLClass owlclass) {

        return EntitySearcher.getIndividuals(owlclass, ontology).collect(toSet());


    }

    public void addAxiomLabel(OWLIndividual I, OWLAnnotation owlAnnotation) {

        OWLAxiom axiom = owlfactory.getOWLAnnotationAssertionAxiom(I.asOWLNamedIndividual().getIRI(), owlAnnotation);
        this.ontologyManager.applyChange(new AddAxiom(ontology, axiom));
    }

    /**
     * ** OUTPUT RDF **
     */
    public void exportTurtle(String fileName) {

        TurtleDocumentFormat turtleFormat = new TurtleDocumentFormat();
       

        File outputFile = new File(fileName + ".ttl");
        try {
            this.ontologyManager.saveOntology(ontology, turtleFormat, new FileOutputStream(outputFile));
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(OntologyManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OntologyManagement.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void exportRDFXML(String fileName) {
        RDFXMLDocumentFormat rdfXMLFormat = new RDFXMLDocumentFormat();

        File outputFile = new File(fileName + ".rdf");

        try {
            this.ontologyManager.saveOntology(ontology, rdfXMLFormat, new FileOutputStream(outputFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OntologyManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(OntologyManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exportOWLXML(String fileName) {
        OWLXMLDocumentFormat owlXMLFormat = new OWLXMLDocumentFormat();

        File outputFile = new File(fileName + ".owl");

        try {
            this.ontologyManager.saveOntology(ontology, owlXMLFormat, new FileOutputStream(outputFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OntologyManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(OntologyManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /* not working */
    public void exportJSON(String fileName){
        
        OWLDocumentFormat ontologyFormat = new RDFJsonLDDocumentFormat();
        
        File outputFile = new File(fileName + ".json");
        
        
        try {
            this.ontologyManager.saveOntology(ontology, ontologyFormat, new FileOutputStream(outputFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OntologyManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(OntologyManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static void main(String[] args) {
        
        OntologyManagement om = OntologyManagement.getInstance();
        om.initOntologyManager();
        
        //om.retrieveMCOClasses(); //to populate the comboboxes - DONE
        //om.retrieveAlgrothimClasses(); //to populate the comboboxes of algorhtimns - DONE
        //om.retrieveLicenses(); //to populate the comboxes of licenses - DONE
        om.retrieveIAODocumentParts();
        
        
        //TEST TODOS...
        //TODO: add instances - DONE (consider adding options for rdf comments)
        om.addUserClassInstance(
                "Detection of previously unknown HIV infections in the young MSM (Men who have Sex with Men) population (16-29) in Houston, Texas",
                "Version_Information");
        
        om.addUserClassInstance(
                "Developed by researchers at University of Texas Health Science Center at Houston, University of Chicago, and Jilin University", 
                "Model_Detail");
        
        om.addUserClassInstance(
                "Small training sample size and sparse features can make model training difficult, especially given challenges in recruiting members of the MSM population for such studies", 
                "Limitation_Information");
        
        //TODO: construct network connections - DONE
        om.generateNetwork();
        
        //TODO: export to computable format - DONE
        om.exportTurtle("sampleName_x_alt");
    }

}
