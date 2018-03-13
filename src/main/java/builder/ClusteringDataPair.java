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

    private String column;
    private List<Line> points;

    public ClusteringDataPair() {
    }

    public ClusteringDataPair(String column, List<Line> points) {
        this.column = column;
        this.points = points;
    }

    public String getColumn() {
        return column;
    }

    public List<Line> getPoints() {
        return points;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public void setPoints(List<Line> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "\nClusteringDataPair{" + "\ncolumn=" + column + ",\n points=" + points + "\n}";
    }
    
    
}
