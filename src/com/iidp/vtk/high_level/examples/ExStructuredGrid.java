package com.iidp.vtk.high_level.examples;

import com.iidp.vtk.high_level.EVTK;

import java.util.Random;

public class ExStructuredGrid {

    public static void main(String[] args) throws Exception {
        var nc = 5;
        var dx = 1.0;
        var dy = 1.0;
        var dz = 2.0;

        var nncells = nc * nc * nc;
        var nnpoints = (nc + 1) * (nc + 1) * (nc + 1);

        var x = new double[nc][nc][nc];
        var y = new double[nc][nc][nc];
        var z = new double[nc][nc][nc];

        var rnd = new Random();
        var factor = 0.2;
        for (int k = 0; k < nc; k++) {
            for (int j = 0; j < nc; j++) {
                for (int i = 0; i < nc; i++) {
                    x[i][j][k] = dx * i + rnd.nextDouble() * dx * factor;
                    y[i][j][k] = dy * j + rnd.nextDouble() * dy * factor;
                    z[i][j][k] = dz * k + rnd.nextDouble() * dz * factor;
                }
            }
        }

        var cellData = EVTK.makeCellData();
        var pointData = EVTK.makePointData();

        var temp  = new double[nncells];
        for (int i = 0; i < nncells; i++) {
            temp[i] = rnd.nextDouble();
        }
        cellData.addData("temperature", temp);

        var pressure = new double[nnpoints];
        for (int i = 0; i < nnpoints; i++) {
            pressure[i] = rnd.nextDouble();
        }
        pointData.addData("pressure", pressure);

        EVTK.structuredGridToVTK("structured", x, y, z, cellData, pointData);

        System.out.println("*** ALL DONE ***");
    }
}
