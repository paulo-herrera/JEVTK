package com.iidp.vtk.high_level.examples;

import com.iidp.vtk.high_level.EVTK;

import java.util.Random;

public class ExPoints {
    public static void main(String[] args) throws Exception {
        var npoints = 25;
        var x = new double[npoints];
        var y = new double[npoints];
        var z = new double[npoints];
        var temp = new double[npoints];

        var rnd = new Random();
        for (int i = 0; i < npoints; i++) {
            x[i] = rnd.nextDouble();
            y[i] = rnd.nextDouble();
            z[i] = rnd.nextDouble();
            temp[i] = rnd.nextDouble();
        }

        var pointData = EVTK.makePointData();
        pointData.addData("temp", temp);

        EVTK.pointsToVTK("points", x, y, z, pointData);

        System.out.println("*** ALL DONE ***");
    }
}
