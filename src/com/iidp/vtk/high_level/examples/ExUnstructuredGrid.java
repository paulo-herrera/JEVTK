package com.iidp.vtk.high_level.examples;

import com.iidp.vtk.low_level.VTK_CELL_TYPE;
import com.iidp.vtk.high_level.EVTK;

public class ExUnstructuredGrid {

    public static void main(String[] args) throws Exception {
       var nnpoints = 6;

       // Vertex of the grid
       var x = new double[6];
       var y = new double[6];
       var z = new double[6];

       x[0] = 0.0; y[0] = 0.0; z[0] = 0.0;
       x[1] = 1.0; y[1] = 0.0; z[1] = 0.0;
       x[2] = 2.0; y[2] = 0.0; z[2] = 0.0;
       x[3] = 0.0; y[3] = 1.0; z[3] = 0.0;
       x[4] = 1.0; y[4] = 1.0; z[4] = 0.0;
       x[5] = 2.0; y[5] = 1.0; z[5] = 0.0;

       // Define connectivity of vertex that belongs to each elemen
        var conn = new int[10];
        conn[0] = 0; conn[1] = 1; conn[2] = 3;              // first triangle
        conn[3] = 1; conn[4] = 4; conn[5] = 3;              // second triangle
        conn[6] = 1; conn[7] = 2; conn[8] = 5; conn[9] = 4; // rectangle

        // Define offset of last vertex of each element
        var offset = new int[3];
        offset[0] = 3;
        offset[1] = 6;
        offset[2] = 10;

        // Define cell types
        var ctype = new VTK_CELL_TYPE[3];
        ctype[0] = VTK_CELL_TYPE.VTK_TRIANGLE;
        ctype[1] = VTK_CELL_TYPE.VTK_TRIANGLE;
        ctype[2] = VTK_CELL_TYPE.VTK_QUAD;

        // Some data
        var cellData = EVTK.makeCellData();
        var temp = new double[]{1.0, 2.5, 3.0};
        cellData.addData("temperature", temp);

        var pointData = EVTK.makePointData();
        var pressure = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6};
        pointData.addData("pressure", pressure);

        EVTK.unstructuredGridToVTK("unstructured", x, y, z, conn, offset, ctype, cellData, pointData);

        System.out.println("*** ALL DONE ***");
    }
}
