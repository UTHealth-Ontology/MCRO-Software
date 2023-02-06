/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uth.sbmi.mco.java.models;

import org.semanticweb.owlapi.model.IRI;

/**
 *
 * @author tuan
 */
public class ClassData {
    
    final private String DEFAULT_TiP = "[no tip text]";
    
    private IRI class_iri = null;
    private String human_friendly_name;
    private StringBuilder tip_text;
    
    public ClassData(){
        tip_text = new StringBuilder();
    }

    /**
     * @return the class_iri
     */
    public IRI getClass_iri() {
        return class_iri;
    }

    /**
     * @param class_iri the class_iri to set
     */
    public void setClass_iri(IRI class_iri) {
        this.class_iri = class_iri;
    }

    /**
     * @return the human_friendly_name
     */
    public String getHuman_friendly_name() {
        if(human_friendly_name == null){
            return class_iri.getFragment().toString();
        }
        
        return human_friendly_name;
    }

    /**
     * @param human_friendly_name the human_friendly_name to set
     */
    public void setHuman_friendly_name(String human_friendly_name) {
        this.human_friendly_name = human_friendly_name;
    }

    /**
     * @return the tip_text
     */
    public String getTip_text() {
        
        if(tip_text.length()<1){
            return this.DEFAULT_TiP;
        }
        
        return tip_text.toString();
    }

    /**
     * @param tip_text the tip_text to set
     */
    public void setTip_text(String tip_text) {
        
        if(tip_text == null || tip_text.trim().length()<1){
            return;
        }
        
        //if(this.tip_text.equals(tip_text))
        
        this.tip_text.append(tip_text);
    }
    
    
    
}
