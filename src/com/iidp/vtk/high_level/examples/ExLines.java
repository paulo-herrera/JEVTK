package com.iidp.vtk.high_level.examples;

import static com.iidp.vtk.high_level.EVTK.*;

public class ExLines {
    public static void main(String[] args) throws Exception {
        var path = "tmp/Ex_lines";

        var npoints = 6;
        var ncells = 3;
        var x = new double[]{0.0, 0.0, 0.0, 4.0, 4.0, 4.0};
        var y = new double[]{-1.0, 1.0, 0.0, 0.0, -1.0, 1.0};
        var z = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

        var cellData = makeCellData();
        var ncell = new int[] {1, 2, 3};
        cellData.addData("ncell", ncell);

        var pointData = makePointData();
        var npoint = new int[] {1, 2, 3, 4, 5, 6};
        pointData.addData("npoint", npoint);

        var comments = makeComments();
        comments.add("Example of using linesToVTK");

        linesToVTK(path, x, y, z, cellData, pointData, comments);

        System.out.println("*** ALL DONE ***");
    }
}
