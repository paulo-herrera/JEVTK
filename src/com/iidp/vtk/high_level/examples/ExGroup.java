package com.iidp.vtk.high_level.examples;

import com.iidp.vtk.high_level.EVTK;
import static com.iidp.vtk.high_level.EVTK.createGroup;
import static com.iidp.vtk.high_level.EVTK.cylinderToVTK;

public class ExGroup {

    private static String makeCylinder(String filename, double radius) throws Exception {
        double x0 = 0.0; double y0 = 0.0;
        double z0 = 0.0; double z1 = 20.0;

        int nlayers = 3; int npilars = 10;

        var ncells = nlayers * npilars;
        var npoints = npilars * (nlayers + 1);

        var cellData = EVTK.makeCellData();
        var mag = new double[ncells];
        for (int i = 0; i < ncells; i++) {
            mag[i] = radius;
        }
        cellData.addData("radius", mag);
        var pointData = EVTK.makePointData();
        var  full_path = cylinderToVTK(filename, x0, y0, z0, z1, radius, nlayers, npilars, cellData, pointData);
        return full_path;
    }

    public static void main(String[] args) throws Exception {
        // Creates three cylinders with different radius
        var f1 = makeCylinder("group1", 10.0);
        var f2 = makeCylinder("group2", 5.0);
        var f3 = makeCylinder("group3", 2.5);

        // Create group that uses previously saved cylinders as different time steps,
        // so they can be animated.
        var g1 = createGroup("group_time_step");
        g1.addFile(f1, 0.0, 0);
        g1.addFile(f2, 1.0, 0);
        g1.addFile(f3, 2.0, 0);
        g1.close();

        // Create group that uses previously saved cylinders to create a single scene or
        // mesh.
        var g2 = createGroup("group_merge");
        g2.addFile(f1, 0.0, 0);
        g2.addFile(f2, 0.0, 1);
        g2.addFile(f3, 0.0, 2);
        g2.close();
    }

}
