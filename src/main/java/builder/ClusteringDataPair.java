/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

import java.util.List;

/**
 *
 * @author taleb
 */
public class ClusteringDataPair {

    private String columnName;
    private List<ColumnMembreValue> columnPoints;

    public ClusteringDataPair() {
    }

    public ClusteringDataPair(String column, List<ColumnMembreValue> points) {
        this.columnName = column;
        this.columnPoints = points;
    }

    public String getColumnName() {
        return columnName;
    }

    public List<ColumnMembreValue> getColumnPoints() {
        return columnPoints;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setColumnPoints(List<ColumnMembreValue> columnPoints) {
        this.columnPoints = columnPoints;
    }

    @Override
    public String toString() {
        return "\nClusteringDataPair{" + "\ncolumn=" + columnName + ",\n points=" + columnPoints + "\n}";
    }
    
    
}
