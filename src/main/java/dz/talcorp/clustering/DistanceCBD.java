/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.talcorp.clustering;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author taleb
 */
public  class DistanceCBD {

        private final StringProperty distanceName = new SimpleStringProperty();
        private final IntegerProperty distanceValue = new SimpleIntegerProperty();

        public int getDistanceValue() {
            return distanceValue.get();
        }

        public void setDistanceValue(int value) {
            distanceValue.set(value);
        }

        public IntegerProperty distanceValueProperty() {
            return distanceValue;
        }

        public String getDistanceName() {
            return distanceName.get();
        }

        public void setDistanceName(String value) {
            distanceName.set(value);
        }

        public StringProperty distanceNameProperty() {
            return distanceName;
        }

        public DistanceCBD(String distanceName, int value) {
            this.distanceName.setValue(distanceName);
            distanceValue.setValue(value);
        }

        @Override
        public String toString() {
            return distanceName.getValue();
        }

    }
