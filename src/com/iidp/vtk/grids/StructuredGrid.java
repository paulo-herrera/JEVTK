/*
 *  Copyright (C) 2009-2020 Paulo A. Herrera
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.iidp.vtk.grids;

import java.io.DataOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import com.iidp.vtk.low_level.VTK_DATA_TYPE;
import com.iidp.vtk.low_level.VTK_FILE_TYPE;
import com.iidp.vtk.low_level.VTKWriter;

/**
 * Provides helper methods to build and export a structured grid.
 * This is a convenience class that can be useful if you need to create a grid
 * from scratch.
 * See {@link com.iidp.vtk.grids.examples.ExStructuredGrid} for an example.
 */
public class StructuredGrid {
    /**
     * Nodes coordinates
     */
    private double[][] coords;

    /**
     * Number of cells in each direction
     */
    public int nx, ny, nz;
    public int ncols, nrows, nlays;

    private int ncells, nnodes;

    HashMap<String, double[]> cellVars = new HashMap<String, double[]>();
    HashMap<String, double[]> nodeVars = new HashMap<String, double[]>();
    //TODO: ADD INT VARIABLES

    /**
     * @param nx number of nodes in x direction.
     * @param ny number of nodex in y direction.
     * @param nz number of nodex in z direction.
     */
    public StructuredGrid(int nx, int ny, int nz) {
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;

        // this follows MODFLOW convention. NO VERY LOGICAL, BUT...!
        this.ncols = nx - 1;
        this.nrows = ny - 1;
        this.nlays = nz - 1;

        ncells = (nx - 1) * (ny - 1) * (nz - 1);
        nnodes = nx * ny * nz;
    }

    private final int getCellIndex1D(int i, int j, int k) {
        return i + j * this.ncols + k * this.ncols * this.nrows; // THIS MAY OVERFLOW, WOW!
    }

    public StructuredGrid setNodeCoordinates(double[] x, double[] y, double[] z) {
        System.out.println("Setting node coordinates...");
        //System.out.printf("x.size: %d\n", x.length);
        //System.out.printf("y.size: %d\n", y.length);
        //System.out.printf("z.size: %d\n", z.length);

        assert (x.length == nnodes);
        assert (x.length == y.length && x.length == z.length);

        double[][] tmp = new double[nnodes][3];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i][0] = x[i];
            tmp[i][1] = y[i];
            tmp[i][2] = z[i];
        }

        // Sort coordinates such that they define the nodes of a structured grid
        // with column major order, i.e. cell number change fastest in x direction, and
        // faster in y than in z directions.

        final int npx = nx;
        final int npy = ny;
        final int npz = nz;

        // First sort in x- and then in y-directions
        Arrays.sort(tmp, new OrderCoordinates(0));

        final int nnyz = npy * npz;
        int nyz = 0;
        while (nyz < nnodes) {
            //System.out.println("nnodes: " + nnodes + "  nrow: " + nrow);
            Arrays.sort(tmp, nyz, (nyz + nnyz), new OrderCoordinates(1));
            nyz += nnyz;
        }

        // Now we have to sort the elevations along each pilar column
        int ncolumn = 0;
        while (ncolumn < nnodes) {
            Arrays.sort(tmp, ncolumn, ncolumn + npz, new OrderCoordinates(2));
            ncolumn += npz;
        }

        // At this point nodes are ordered with z coordinate changing fastest.
        // So we have to invert order.
        coords = new double[tmp.length][];
        int pos = 0;
        for (int i = 0; i < npx; i++) {
            for (int j = 0; j < npy; j++) {
                for (int k = 0; k < npz; k++) {
                    final int ii = i + j * npx + k * npx * npy;
                    coords[ii] = tmp[pos];
                    pos += 1;
                }
            }
        }
        return this;
    }

    /**
     * Adds variable values to grid that are given as a list of (i,j,k) tuples and
     * a list of values for each cell. It assumes that the list of values is square,
     * i.e. each tuple (i,j,k) has the same number of values associated.
     *
     * @param name:          Variable name.
     * @param ijk:           2D array of dimension [ncells][3] with i,j,k indexes for each cell.
     * @param values:        2D array of dimension [ncells][nvals] with values associated to each cell in the list.
     * @param value_pos:     position in the list of values of the variable to export.
     * @param default_value: Value assigned to cells that are not in the list.
     */
    public StructuredGrid addCellVariable(String name, int[][] ijk, double[][] values, int value_pos, double default_value) {
        System.out.println("Adding cell variable to grid from list of tuples...");
        var a = new double[this.ncells];

        for (int i = 0; i < a.length; i++) {
            a[i] = default_value;
        }

        for (int t = 0; t < ijk.length; t++) {
            var iijk = ijk[t];
            var i = iijk[0];
            var j = iijk[1];
            var k = iijk[2];
            assert i < this.ncols;
            assert j < this.nrows;
            assert k < this.nlays;
            final int ii = getCellIndex1D(i, j, k);
            assert ii < a.length;
            //System.out.printf("i: %d  j: %d  k: %d ii: %d  ncells: %d\n", i, j, k, ii, this.ncells);

            a[ii] = values[t][value_pos];
        }

        this.addCellVariable(name, a);
        return this;
    }

    /** Returns array with x-coordinate. */
    public double[] getX() {
        double[] x_ = new double[nnodes];
        for (int i = 0; i < x_.length; i++) {
            x_[i] = coords[i][0];
        }
        return x_;
    }

    /** Returns array with y-coordinate. */
    public double[] getY() {
        double[] y_ = new double[nnodes];
        for (int i = 0; i < y_.length; i++) {
            y_[i] = coords[i][1];
        }
        return y_;
    }

    /** Returns array with z-coordinate. */
    public double[] getZ() {
        double[] z_ = new double[nnodes];
        for (int i = 0; i < z_.length; i++) {
            z_[i] = coords[i][2];
        }
        return z_;
    }

    // Helper class
    private static class OrderCoordinates implements Comparator {
        final int dim;

        public OrderCoordinates(int d) {
            dim = d;
        }

        public int compare(Object o1, Object o2) {
            double[] pos1 = (double[]) o1;
            double[] pos2 = (double[]) o2;

            if (pos1[dim] < pos2[dim]) {
                return -1;
            } else if (pos1[dim] > pos2[dim]) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Adds a variable associated to each cell of the grid.
     *
     * NOTE: array values must be given in the grid order,
     * i.e. x direction changing fastest, then y, then z
     * @param name variable name.
     * @param var array with variable values. It should contains ncells elements.
     */
    public StructuredGrid addCellVariable(String name, double[] var) {
        assert (var.length == ncells);
        System.out.println("adding cell variable: " + var.length);
        cellVars.put(name, var);
        return this;
    }

    /**
     * Adds a variable associated to each node of the grid.
     *
     * NOTE: array values must be given in the grid order,
     * i.e. x direction changing fastest, then y, then z
     * @param name variable name.
     * @param var array with variable values. It should contains nnodes elements.
     */
    public StructuredGrid addNodeVariable(String name, double[] var) {
        assert (var.length == nnodes);
        System.out.println("adding node variable: " + var.length);
        nodeVars.put(name, var);
        return this;
    }

    /**
     * Writes grid as a binary XML VTK file.
     *
     * @param filename full path to where grid should be saved.
     * @throws Exception
     */
    public void toVTK(String filename) throws Exception {
        System.out.println("Writing structured grid to file: " + filename);
        System.out.printf(" grid dims - nx: %d \t ny: %d \t nz: %d \n", nx, ny, nz);
        System.out.println(" # cells: " + ncells);
        System.out.println(" # nodes: " + nnodes);
        var vw = new VTKWriter(new File(filename), VTK_FILE_TYPE.STRUCTURED_GRID);

        int[] start = {0, 0, 0};
        int[] end = {nx - 1, ny - 1, nz - 1};
        vw.openStructuredGrid(start, end);
        vw.openPiece(start, end);

        // Declare point data
        String pdefault = nodeVars.keySet().size() > 0 ? nodeVars.keySet().iterator().next() : null;
        vw.openPointData(pdefault, null, null, null, null);
        for (String name : nodeVars.keySet()) {
            vw.addDataArray(name, VTK_DATA_TYPE.FLOAT64, nnodes, 1);
        }
        vw.closePointData();

        // Declare cell data
        String cdefault = cellVars.keySet().size() > 0 ? cellVars.keySet().iterator().next() : null;
        vw.openCellData(cdefault, null, null, null, null);
        for (String name : cellVars.keySet()) {
            vw.addDataArray(name, VTK_DATA_TYPE.FLOAT64, ncells, 1);
        }
        vw.closeCellData();

        // Coordinates of cell vertices
        vw.openElement("Points");
        vw.addDataArray("coordinates", VTK_DATA_TYPE.FLOAT64, nnodes, 3);
        vw.closeElement("Points");

        vw.closePiece();
        vw.closeStructuredGrid();

        vw.openAppendedData();

        // Point data
        for (String name : nodeVars.keySet()) {
            // System.out.println("Writing node data");
            vw.appendArray(nodeVars.get(name));
        }

        // Cell data
        for (String name : cellVars.keySet()) {
            System.out.println("Writing cell variable: " + name);
            vw.appendArray(cellVars.get(name));
        }

        // Coordinates
        DataOutputStream out = vw.getStream();
        out.writeInt(nnodes * VTK_DATA_TYPE.FLOAT64.sizeof() * 3);
        for (int i = 0; i < nnodes; i++) {
            out.writeDouble(coords[i][0]);
            out.writeDouble(coords[i][1]);
            out.writeDouble(coords[i][2]);
        }

        vw.closeAppendedData();
        vw.close();
    }

    /**
     * Writes grid as a pure ASCII XML VTK file.
     *
     * @param filename full path to where grid should be saved.
     * @throws Exception
     */
    public void toVTKAsASCII(String filename) throws Exception {
        System.out.println("Writing structured grid to file: " + filename);
        System.out.printf(" grid dims - nx: %d \t ny: %d \t nz: %d \n", nx, ny, nz);
        System.out.println(" # cells: " + ncells);
        System.out.println(" # nodes: " + nnodes);
        var vw = new VTKWriter(new File(filename), VTK_FILE_TYPE.STRUCTURED_GRID);

        int[] start = {0, 0, 0};
        int[] end = {nx - 1, ny - 1, nz - 1};
        vw.openStructuredGrid(start, end);
        vw.openPiece(start, end);

        // Declare point data
        String pdefault = nodeVars.keySet().size() > 0 ? nodeVars.keySet().iterator().next() : null;
        vw.openPointData(pdefault, null, null, null, null);
        for (String name : nodeVars.keySet()) {
            vw.addDataArrayASCII(name, nodeVars.get(name));
        }
        vw.closePointData();

        // Declare cell data
        String cdefault = cellVars.keySet().size() > 0 ? cellVars.keySet().iterator().next() : null;
        vw.openCellData(cdefault, null, null, null, null);
        for (String name : cellVars.keySet()) {
            vw.addDataArrayASCII(name, cellVars.get(name));
        }
        vw.closeCellData();

        // Coordinates of cell vertices
        vw.openElement("Points");
        vw.addDataArrayASCII("coordinates", coords);
        vw.closeElement("Points");

        vw.closePiece();
        vw.closeStructuredGrid();
        vw.close();
    }
}
