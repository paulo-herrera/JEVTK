/*
 * Copyright (C) 2020 Paulo A. Herrera <paulo.herrera.eirl at gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.iidp.vtk.high_level;

import com.iidp.vtk.Version;
import com.iidp.vtk.low_level.*;
import com.iidp.vtk.high_level.data.Data;

import java.io.File;
import java.lang.Math;

/**
 * Provides a high-level interface to export data to VTK similar to the one for Python in EVTK.
 * <p>
 * There is one writer for almost each grid type that is supported by VTK.
 * This interface is supposed to be used with the data containers in the data package, thus it
 * introduces some memory and computational overhead, because data in arrays are copied to Lists that
 * contain objects that store numbers. Then, there is also some overhead for the garbage collector.
 * However, the cost of using this interface should be relatively small for typical grids that
 * are used in simulations that run in desktop PCs (say few million cells), hence the easy of use
 * makes it a better option than the low-level interface provided by the VTKWriter.
 * <p>
 * See the examples in com.iidp.vtk.hl.examples package.
 */
public class EVTK {

    static {
        Version.report();
    }

    /**
     * Exports an image defined as a Cartesian 3D grid with constant spacing in each direction.
     *
     * @param path:      path to save grid file without extension.
     * @param ncells:    number of cells in each direction as a int[3].
     * @param origin:    coordinates of orgin of the grid as a double[3].
     * @param spacing:   grid spacing in each direction as a double[3].
     * @param cellData:  a container with data for each cell created with makeCellData.
     * @param pointData: a container with data for each point created with makePointData.
     * @return the full path to where the grid file was saved including extension.
     * @throws Exception
     */
    public static String imageToVTK(String path, int[] ncells, double[] origin, double[] spacing, Data cellData, Data pointData) throws Exception {
        var full_path = path + ".vti";
        var dst = new File(full_path);
        var vw = new VTKWriter(dst, VTK_FILE_TYPE.IMAGE_DATA);

        var nx = ncells[0];
        var ny = ncells[1];
        var nz = ncells[2];

        var nncells = nx * ny * nz;
        var nnpoints = (nx + 1) * (ny + 1) * (nz + 1);

        var start = new int[]{0, 0, 0};
        var end = new int[]{nx, ny, nz};

        vw.openImageData(start, end, origin, spacing);
        vw.openPiece(start, end);

        if (pointData != null) {
            pointData.addDataArray(vw, nnpoints, nncells);
        }

        if (cellData != null) {
            cellData.addDataArray(vw, nnpoints, nncells);
        }

        vw.closePiece();
        vw.closeImageData();


        // Open data section
        vw.openAppendedData();

        if (pointData != null) {
            pointData.appendData(vw, nnpoints, nncells);
        }

        if (cellData != null) {
            cellData.appendData(vw, nnpoints, nncells);
        }

        // Close data section.
        vw.closeAppendedData();
        vw.close();

        return full_path;
    }

    /**
     * Exports a Cartesian grid to VTK. Nodes coordinates are defined by 1D arrays.
     *
     * @param path:       full path without extension where grid file should be saved.
     * @param x:          x-coordinate of grid nodes. It should have nx + 1 elements, where nx is the number of cells.
     * @param y:          y-coordinate of grid nodes. It should have ny + 1 elements, where ny is the number of cells.
     * @param z:          z-coordinate of grid nodes. It should have nz + 1 elements, where nz is the number of cells.
     * @param cellData:   a container with data for each cell created with makeCellData.
     * @param pointData:a container with data for each point created with makePointData.
     * @return full path with extension where grid file was saved.
     */
    public static String rectilinearGridToVTK(String path, double[] x, double[] y, double[] z, Data cellData, Data pointData) throws Exception {
        var full_path = path + ".vtr";
        var dst = new File(full_path);
        var vw = new VTKWriter(dst, VTK_FILE_TYPE.RECTILINEAR_GRID);

        var nx = x.length - 1;
        var ny = y.length - 1;
        var nz = z.length - 1;

        var nncells = nx * ny * nz;
        var nnpoints = (nx + 1) * (ny + 1) * (nz + 1);

        var start = new int[]{0, 0, 0};
        var end = new int[]{nx, ny, nz};

        vw.openRectilinearGrid(start, end);
        vw.openPiece(start, end);

        vw.openElement("Coordinates");
        vw.addDataArray("x_coordinates", VTK_DATA_TYPE.FLOAT64, x.length, 1);
        vw.addDataArray("y_coordinates", VTK_DATA_TYPE.FLOAT64, y.length, 1);
        vw.addDataArray("z_coordinates", VTK_DATA_TYPE.FLOAT64, z.length, 1);
        vw.closeElement("Coordinates");

        // ADD CELL AND POINT DATA
        if (cellData != null) {
            cellData.addDataArray(vw, nnpoints, nncells);
        }

        if (pointData != null) {
            pointData.addDataArray(vw, nnpoints, nncells);
        }

        vw.closePiece();
        vw.closeRectilinearGrid();

        vw.openAppendedData();
        vw.appendArray(x);
        vw.appendArray(y);
        vw.appendArray(z);

        if (cellData != null) {
            cellData.appendData(vw, nnpoints, nncells);
        }

        if (pointData != null) {
            pointData.appendData(vw, nnpoints, nncells);
        }

        vw.closeAppendedData();
        vw.close();

        return full_path;
    }


    /**
     * Exports a logically structured grid that is composed of hexahedral cells.
     * <p>
     * There is also another option to export a structured grid that also allows
     * building it from zero, using the StructuredGrid class.
     *
     * @param path:       full path withouth extension where grid file should be saved.
     * @param x:          3D array with x coordinate of nodes in the grid, i.e. x[i][j][k] = x of node (i,j,k)
     * @param y:          3D array with x coordinate of nodes in the grid, i.e. y[i][j][k] = y of node (i,j,k)
     * @param z:          3D array with x coordinate of nodes in the grid, i.e. z[i][j][k] = z of node (i,j,k)
     * @param cellData:   a container with data for each cell created with makeCellData.
     * @param pointData:a container with data for each point created with makePointData.
     * @return full path where grid file was saved.
     */
    public static String structuredGridToVTK(String path, double[][][] x, double[][][] y, double[][][] z, Data cellData, Data pointData) throws Exception {
        var full_path = path + ".vts";
        var dst = new File(full_path);
        var vw = new VTKWriter(dst, VTK_FILE_TYPE.STRUCTURED_GRID);

        var nx = x.length - 1;
        var ny = x[0].length - 1;
        var nz = x[0][0].length - 1;

        assert (y.length == nx) && (y[0].length == ny) && (y[0][0].length == nz);
        assert (z.length == nx) && (z[0].length == ny) && (z[0][0].length == nz);

        var nncells = nx * ny * nz;
        var nnpoints = (nx + 1) * (ny + 1) * (nz + 1);

        var start = new int[]{0, 0, 0};
        var end = new int[]{nx, ny, nz};

        vw.openStructuredGrid(start, end);
        vw.openPiece(start, end);
        if (cellData != null) {
            cellData.addDataArray(vw, nnpoints, nncells);
        }

        if (pointData != null) {
            pointData.addDataArray(vw, nnpoints, nncells);
        }

        vw.openElement("Points");
        vw.addDataArray("points", VTK_DATA_TYPE.FLOAT64, nnpoints, 3);
        vw.closeElement("Points");

        vw.closePiece();
        vw.closeStructuredGrid();

        vw.openAppendedData();

        if (cellData != null) {
            cellData.appendData(vw, nnpoints, nncells);
        }

        if (pointData != null) {
            pointData.appendData(vw, nnpoints, nncells);
        }

        // Add coordinates
        var dos = vw.getStream();
        dos.writeInt(nnpoints * VTK_DATA_TYPE.FLOAT64.sizeof() * 3);
        for (int k = 0; k < nz + 1; k++) {
            for (int j = 0; j < nx + 1; j++) {
                for (int i = 0; i < nx + 1; i++) {
                    dos.writeDouble(x[i][j][k]);
                    dos.writeDouble(y[i][j][k]);
                    dos.writeDouble(z[i][j][k]);
                }
            }
        }

        vw.closeAppendedData();
        vw.close();
        return full_path;
    }

    /**
     * Exports an unstructured grid as VTK grid file.
     *
     * @param path:
     * @param x:            1D array with x-coordinate of the nodes.
     * @param y:            1D array with y-ccordinate of the nodes.
     * @param z:            1D array with z-ccordinate of the nodes.
     * @param connectivity: 1D array that defines the vertices associated to each element.
     *                      Together with offsets define the connectivity or topology of the grid.
     *                      It is assumed that vertices in an element are listed consecutively.
     * @param offsets:      1D array with the index of the last vertex of each element in the connectivity array.
     *                      It should have length nelem, where nelem is the number of cells or elements in the grid.
     * @param cell_types:   1D array with an integer that defines the cell type of each element in the grid.
     *                      It should have size nelem. This should be assigned from VTK_CELL_TYPE.XXX,
     *                      where XXXX represent the type of cell.
     *                      Check the VTK file format specification for allowed cell types.
     * @param cellData:     a container with data for each cell created with makeCellData.
     * @param pointData:a   container with data for each point created with makePointData.
     * @return full path including extension where grid file was saved.
     */
    public static String unstructuredGridToVTK(String path, double[] x, double[] y, double[] z, int[] connectivity, int[] offsets,
                                               VTK_CELL_TYPE[] cell_types, Data cellData, Data pointData) throws Exception {
        var full_path = path + ".vtu";
        var dst = new File(full_path);
        var vw = new VTKWriter(dst, VTK_FILE_TYPE.UNSTRUCTURED_GRID);

        var nnpoints = x.length;
        var nncells = cell_types.length;

        assert (y.length == nnpoints);
        assert (z.length == nnpoints);
        assert (offsets.length == nncells);
        assert (cell_types.length == nncells);

        vw.openUnstructuredGrid();
        vw.openPiece(nnpoints, nncells);

        if (cellData != null) {
            cellData.addDataArray(vw, nnpoints, nncells);
        }

        if (pointData != null) {
            pointData.addDataArray(vw, nnpoints, nncells);
        }

        vw.openElement("Points");
        vw.addDataArray("points", VTK_DATA_TYPE.FLOAT64, nnpoints, 3);
        vw.closeElement("Points");
        vw.openElement("Cells");
        vw.addDataArray("connectivity", VTK_DATA_TYPE.INT32, connectivity.length, 1);
        vw.addDataArray("offsets", VTK_DATA_TYPE.INT32, offsets.length, 1);
        vw.addDataArray("types", VTK_DATA_TYPE.UINT8, cell_types.length, 1);
        vw.closeElement("Cells");

        vw.closePiece();
        vw.closeUnstructuredGrid();

        // APPENDED DATA
        vw.openAppendedData();

        if (cellData != null) {
            cellData.appendData(vw, nnpoints, nncells);
        }

        if (pointData != null) {
            pointData.appendData(vw, nnpoints, nncells);
        }

        // coordinates
        vw.appendArraysAs3D(x, y, z);

        vw.appendArray(connectivity);
        vw.appendArray(offsets);
        var ct = VTK_CELL_TYPE.asByteArray(cell_types);
        vw.appendArray(ct);

        vw.closeAppendedData();
        vw.close();

        return full_path;
    }

    /**
     * Exports points with associated data as a VTK Unstructured Grid.
     * <p>
     * Internally, this method calls unstructuredGridToVTK with the appropriated arrays that describe the
     * topology of the unstructured grid that represents the group of points.
     *
     * @param path:      path to where grid file should be saved without extension.
     * @param x:         1D array with x coordinate.
     * @param y:         1D array with y coordinate.
     * @param z:         1D array with z coordinate.
     * @param pointData: Data stored in a container that is associated to each point, e.g. Temperature.
     * @return full path to saved grid file.
     * @throws Exception
     */
    public static String pointsToVTK(String path, double[] x, double[] y, double[] z, Data pointData) throws Exception {
        var nnpoints = x.length;

        var connectivity = new int[nnpoints + 1];
        var offsets = new int[nnpoints];
        var cell_types = new VTK_CELL_TYPE[nnpoints];
        for (int i = 0; i < nnpoints; i++) {
            connectivity[i] = i;
            offsets[i] = i;
            cell_types[i] = VTK_CELL_TYPE.VTK_VERTEX;
        }
        connectivity[nnpoints] = nnpoints;

        Data cellData = null;

        var p = unstructuredGridToVTK(path, x, y, z, connectivity, offsets, cell_types, cellData, pointData);
        return p;
    }

    /**
     * Exports cylinder as VTK unstructured grid.
     *
     * @return Full path to saved file.
     * @param path: path to file without extension.
     * @param x0: center of cylinder.
     * @param y0: center of cylinder.
     * @param z0: lower and top elevation of the cylinder.
     * @param z1: lower and top elevation of the cylinder.
     * @param radius: radius of cylinder.
     * @param nlayers: Number of layers in z direction to divide the cylinder.
     * @param npilars: Number of points around the diameter of the cylinder.
     * Higher value gives higher resolution to represent the curved shape.
     * @param cellData: Data container with 1D arrays that store data associated to each cell.
     * Arrays should have number of elements equal to ncells = npilars * nlayers.
     * @param pointData: Data container with 1D arrays that store data associated to each point.
     * Arrays should have number of elements equal to npoints = npilars * (nlayers + 1).
     */
    public static String cylinderToVTK(String path, double x0, double y0, double z0,
                                       double z1, double radius, int nlayers, int npilars,
                                       Data cellData, Data pointData) throws Exception {
        // Define x, y coordinates from polar coordinates.
        var dpi = 2.0 * Math.PI / npilars;
        var nang = (int) (2.0 * Math.PI / dpi);
        var angles = new double[nang];
        var x = new double[nang];
        var y = new double[nang];

        for (int i = 0; i < nang; i++) {
            angles[i] = i * dpi;

            x[i] = radius * Math.cos(angles[i]) + x0;
            y[i] = radius * Math.sin(angles[i]) + y0;
        }

        var dz = (z1 - z0) / nlayers;
        var z = new double[nlayers + 1];
        for (int i = 0; i < nlayers + 1; i++) {
            z[i] = z0 + dz * i;
        }

        var npoints = npilars * (nlayers + 1);
        var ncells = npilars * nlayers;

        var xx = new double[npoints];
        var yy = new double[npoints];
        var zz = new double[npoints];

        var ii = 0;
        for (int k = 0; k < nlayers + 1; k++) {
            for (int p = 0; p < npilars; p++) {
                xx[ii] = x[p];
                yy[ii] = y[p];
                zz[ii] = z[k];
                ii = ii + 1;
            }
        }

        // Define connectivity
        var conn = new int[4 * ncells];
        ii = 0;
        var p1 = 0;
        for (int l = 0; l < nlayers; l++) {
            for (int p = 0; p < npilars; p++) {
                var p0 = p;
                if (p + 1 == npilars) {
                    p1 = 0;
                } else {
                    p1 = p + 1; //circular loop
                }

                var n0 = p0 + l * npilars;
                var n1 = p1 + l * npilars;
                var n2 = n0 + npilars;
                var n3 = n1 + npilars;

                conn[ii + 0] = n0;
                conn[ii + 1] = n1;
                conn[ii + 2] = n2;
                conn[ii + 3] = n3;
                ii = ii + 4;
            }
        }
        // Define offsets
        var offsets = new int[ncells];
        // Define cell types
        var ctype = new VTK_CELL_TYPE[ncells];

        for (int i = 0; i < ncells; i++) {
            offsets[i] = (i + 1) * 4;
            ctype[i] = VTK_CELL_TYPE.VTK_PIXEL;
        }

        var full_path = unstructuredGridToVTK(path, xx, yy, zz, conn, offsets, ctype, cellData, pointData);
        return full_path;
    }

    /**
     * Creates and returns a VTKGroup that can be used to specify links to multiple files that
     * can be used for animations or merging multiple files in a single scene.
     *
     * @param path: path to file where group should be saved without extension.
     * @return VTKGroup, that can be used to add files.
     * @throws Exception
     */
    public static VTKGroup createGroup(String path) throws Exception {
        var full_path = path + ".pvd";
        return new VTKGroup(new File(full_path) );
    }

    public static Data makeCellData() {
        return new Data("cellData");
    }

    public static Data makePointData() {
        return new Data("pointData");
    }

}
