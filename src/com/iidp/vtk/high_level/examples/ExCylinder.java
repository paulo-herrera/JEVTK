package com.iidp.vtk.high_level.examples;

import com.iidp.vtk.high_level.EVTK;

import static com.iidp.vtk.high_level.EVTK.cylinderToVTK;

public class ExCylinder {

    public static void main(String[] args) throws Exception {

        double x0 = 0.0; double y0 = 0.0;
        double z0 = 0.0; double z1 = 20.0;
        double radius = 5.0;
        int nlayers = 3; int npilars = 10;

        var ncells = nlayers * npilars;
        var npoints = npilars * (nlayers + 1);

        var cellData = EVTK.makeCellData();
        var pointData = EVTK.makePointData();

        var lay = new int[ncells];

        var ii = 0;
        for (int l = 0; l < nlayers; l++) {
            for (int p = 0; p < npilars; p++) {
                lay[ii] = l;
                ii = ii + 1;
            }
        }

        cellData.addData("layer", lay);

        cylinderToVTK("test/cylinder", x0, y0, z0, z1, radius, nlayers, npilars, cellData, pointData);

        System.out.println("*** ALL DONE ***");
    }
}
