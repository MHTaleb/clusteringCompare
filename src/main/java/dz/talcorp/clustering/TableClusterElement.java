/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.talcorp.clustering;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author taleb
 */
public class TableClusterElement {
    
    private final SimpleStringProperty cluster;
    
    private final SimpleStringProperty individu;

    public TableClusterElement(final String cluster,final String individu) {
        this.cluster = new SimpleStringProperty(cluster);
        this.individu = new SimpleStringProperty(individu);
    }

    public void setCluster(String cluster) {
        this.cluster.setValue(cluster);
    }
    public void setIndividu(String individu) {
        this.individu.setValue(individu);
    }

    public String getCluster() {
        return cluster.getValue();
    }

    public String getIndividu() {
        return individu.getValue();
    }
    
}
