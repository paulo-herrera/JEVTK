package com.iidp.vtk.high_level.examples;

import com.iidp.vtk.high_level.EVTK;

import java.util.Random;

public class ExRectilinearGrid {
    public static void main(String[] args) throws Exception {
        var nc = 15;
        var dx = 1.0;
        var dy = 0.5;
        var dz = 2.0;

        var ncells = new int[] {nc, nc, nc};
        var nnpoints = (nc + 1) * (nc + 1) * (nc + 1);
        var nncells = nc * nc * nc;

        var x = new double[nc + 1];
        var y = new double[nc + 1];
        var z = new double[nc + 1];

        for (int i = 0; i < x.length; i++) {
            x[i] = dx * i;
            y[i] = dy * i;
            z[i] = dz * i;
        }

        // Make some data to color the grid
        var rnd = new Random();
        var cellData = EVTK.makeCellData();
        var temp = new double[nncells];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = rnd.nextDouble();
        }
        cellData.addData("temp", temp);

        var pointData = EVTK.makePointData();
        var pressure = new double[nnpoints];
        for (int i = 0; i < pressure.length; i++) {
            pressure[i] = rnd.nextDouble();
        }
        pointData.addData("pressure", pressure);

        EVTK.rectilinearGridToVTK("rectilinear", x, y, z, cellData, pointData);

        System.out.println("*** ALL DONE ***");
    }
}
