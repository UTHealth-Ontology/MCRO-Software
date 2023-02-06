/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uth.sbmi.mco.java.ontology;

import org.semanticweb.owlapi.model.IRI;

/**
 *
 * @author tuan
 */
public class mcoIRI {
    
    
    public final static IRI sourceOntology = IRI.create("https://raw.githubusercontent.com/UTHealth-Ontology/MCRO/main/mcro.owl");
    public final static IRI sourceSWO = IRI.create("http://purl.obolibrary.org/obo/swo.owl");
    public final static IRI sourceIAO = IRI.create("http://purl.obolibrary.org/obo/iao.owl");
    //http://purl.obolibrary.org/obo/obi>
    public final static IRI sourceOBI = IRI.create("http://purl.obolibrary.org/obo/obi>");
    
    public final static IRI documentation_iri = IRI.create("http://sbmi.uth.edu/ontology/mcro#documentation");

    public final static String base = "http://sbmi.uth.edu/ontology/mcro#";
    
    public final static IRI has_part = IRI.create("http://purl.obolibrary.org/obo/BFO_0000051");
    public final static IRI part_of = IRI.create("http://purl.obolibrary.org/obo/BFO_0000050");
    public final static IRI algorithm = IRI.create("http://purl.obolibrary.org/obo/IAO_0000064");
    public final static IRI license = IRI.create(
            "http://www.ebi.ac.uk/swo/SWO_0000002");
    
    public final static IRI document_parts = IRI.create("http://purl.obolibrary.org/obo/IAO_0000314");
    
    public final static IRI definition_iri = IRI.create("http://purl.obolibrary.org/obo/IAO_0000115");
    
    
    
}
