package com.iidp.vtk.high_level.examples;

import static com.iidp.vtk.high_level.EVTK.*;

public class ExPolygons {
    public static void main(String[] args) throws Exception {
        var path = "tmp/Ex_polygons";

        var x = new double[]{0.0, 0.5, 0.3, -0.5, 0.5, 1.0, 1.0};
        var y = new double[]{0.0, 1.0, 2.0, 1.5, 1.0, -0.5, 0.5};
        var z = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        var pointsPerPolygon = new int[] {4, 3};

        var cellData = makeCellData();
        var ncell = new int[] {1, 2};
        cellData.addData("ncell", ncell);

        var pointData = makePointData();
        var npoint = new int[] {1, 2, 3, 4, 5, 6, 7};
        pointData.addData("npoint", npoint);

        var comments = makeComments();
        comments.add("Example of using linesToVTK");

        polygonsToVTK(path, x, y, z, pointsPerPolygon, cellData, pointData, comments);

        System.out.println("*** ALL DONE ***");
    }
}
