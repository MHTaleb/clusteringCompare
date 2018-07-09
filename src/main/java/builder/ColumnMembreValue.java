/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

/**
 *
 * @author taleb
 */
public class ColumnMembreValue {
    private Float value;
    private int index;

    public ColumnMembreValue() {
    }

    public ColumnMembreValue(Float value, int index) {
        this.value = value;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "\nLine{" + "value=" + value + ", index=" + index + '}';
    }
    
    
}
