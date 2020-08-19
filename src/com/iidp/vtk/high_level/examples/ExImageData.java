package com.iidp.vtk.high_level.examples;

import static com.iidp.vtk.high_level.EVTK.imageToVTK;
import static com.iidp.vtk.high_level.EVTK.makeCellData;
import static com.iidp.vtk.high_level.EVTK.makePointData;
import java.util.Random;

public class ExImageData {
    public static void main(String[] args) throws Exception {
        var nc = 10;
        var ncells = new int[] {nc, nc, nc};
        var nnpoints = (nc + 1) * (nc + 1) * (nc + 1);
        var nncells = nc * nc * nc;

        var origin = new double[]{0.0, 0.0, 0.0};
        var spacing = new double[]{1.0, 1.0, 1.0};

        var cellData = makeCellData();
        var temp = new double[nncells];
        var rnd = new Random();
        for (int i = 0; i < nncells; i++) {
            temp[i] = rnd.nextDouble();
        }
        cellData.addData("temp", temp);

        var pointData = makePointData();

        imageToVTK("image", ncells, origin, spacing, cellData, pointData);
        System.out.println("*** ALL DONE ***");
    }
}
