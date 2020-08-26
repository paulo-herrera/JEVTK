package com.iidp.vtk.low_level.examples;

import com.iidp.vtk.high_level.data.GridData;
import com.iidp.vtk.low_level.VTKWriter;
import com.iidp.vtk.low_level.VTK_FILE_TYPE;

import java.io.File;
import java.util.Random;

import static com.iidp.vtk.high_level.EVTK.makeCellData;
import static com.iidp.vtk.high_level.EVTK.makePointData;

/**
 * Example of using the low-level interface provided by VTKXmlWriter to export and image VTK file.
 */
public class ExVTKXmlWriter {

    private static GridData createPointData(int nnpoints) {
        var pointData = makePointData();
        var pressure = new double[nnpoints];
        var rnd = new Random();
        for (int i = 0; i < nnpoints; i++) {
            pressure[i] = rnd.nextDouble();
        }
        pointData.addData("pressure", pressure);

        var pointnr = new double[nnpoints];
        for (int i = 0; i < nnpoints; i++) {
            pointnr[i] = i;
        }
        pointData.addData("pointNr", pointnr);

        return pointData;
    }

    private static GridData createCellData(int nncells) {
        var cellData = makeCellData();
        var temp = new double[nncells];
        var rnd = new Random();
        for (int i = 0; i < nncells; i++) {
            temp[i] = rnd.nextDouble();
        }
        cellData.addData("temp", temp);

        var cellnr = new int[nncells];
        for (int i = 0; i < nncells; i++) {
            cellnr[i] = i;
        }
        cellData.addData("cellNr", cellnr);
        return cellData;
    }

    private static void writeFile(String path, int nncells, int nnpoints, int[] start, int[] end, double[] origin, double[] spacing, boolean asASCII) throws Exception {
        var dst = new File(path);
        var vw = new VTKWriter(dst, VTK_FILE_TYPE.IMAGE_DATA);

        var cellData = createCellData(nncells);
        var pointData = createPointData(nnpoints);

        vw.openImageData(start, end, origin, spacing);
        vw.openPiece(start, end);

        // Only as declaration of data (asASCII = false), or
        // writes data as text
        pointData.addArrayToVTK(vw, nnpoints, nncells, asASCII);
        cellData.addArrayToVTK(vw, nnpoints, nncells, asASCII);

        vw.closePiece();
        vw.closeImageData();

        if (!asASCII) {
            // Appended binary data section
            vw.openAppendedData();
            pointData.appendData(vw, nnpoints, nncells);
            cellData.appendData(vw, nnpoints, nncells);
            vw.closeAppendedData();
        }

        vw.close();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("EXAMPLE: Low-level interface - Image grid");

        // Defines grid size and origin
        var nc = 30;
        var ncells = new int[]{nc, nc, nc};
        var origin = new double[]{0.0, 0.0, 0.0};
        var spacing = new double[]{1.0, 1.0, 1.0};

        // Computes size of grid
        var nx = ncells[0];
        var ny = ncells[1];
        var nz = ncells[2];
        var nncells = nx * ny * nz;
        var nnpoints = (nx + 1) * (ny + 1) * (nz + 1);

        var start = new int[]{0, 0, 0};
        var end = new int[]{nx, ny, nz};

        // Option 1: writes file as a mix of a ASCII XML section  that contains "light" information
        //           and a binary section that contains "heavy" information.
        //           This is the recommened option for large files.
        writeFile( "tmp/Ex_low_level_appended.vti", nncells, nnpoints, start, end, origin, spacing, false);

        // Option 2: writes file as pure ASCII XML contains "light" and "heavy" information.
        //           Recommened for debugging the data or when data is available as text.
        writeFile( "tmp/Ex_low_level_ascii.vti", nncells, nnpoints, start, end, origin, spacing, true);

        System.out.println("*** ALL DONE ***");
    }
}
