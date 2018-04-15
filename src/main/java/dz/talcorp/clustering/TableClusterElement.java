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

    public TableClusterElement(final String individu, final String cluster , final String noise, final String taux) {
        this.cluster = new SimpleStringProperty(cluster);
        this.individu = new SimpleStringProperty(individu);
        this.noise = new SimpleStringProperty(noise);
        this.taux = new SimpleStringProperty(taux);
    }

    public void setCluster(String cluster) {
        this.cluster.setValue(cluster);
    }

    public String getCluster() {
        return cluster.getValue();
    }

    private final SimpleStringProperty individu;

    public void setIndividu(String individu) {
        this.individu.setValue(individu);
    }

    public String getIndividu() {
        return individu.getValue();
    }

    private final SimpleStringProperty noise;

    public void setNoise(String noise) {
        this.noise.setValue(noise);
    }

    public String getNoise() {
        return noise.getValue();
    }

    private final SimpleStringProperty taux;

    public void setTaux(String taux) {
        this.taux.setValue(taux);
    }

    public String getTaux() {
        return taux.getValue();
    }

}
