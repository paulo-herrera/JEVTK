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
package com.iidp.vtk.grids.examples;

import com.iidp.vtk.grids.StructuredGrid;

/**
 * Example of how to use the StructuredGrid class to create and export
 * a structured grid.
 */
public class ExStructuredGrid {

    public static void main(String[] args) throws Exception {
        // Define dimensions
        final int nc = 5;
        final int np = nc + 1;
        final int ncells = nc * nc * nc;
        final int nnodes = np * np * np;
        final double dx = 0.1;

        // Creates temporary arrays
        double[] x = new double[np * np * np];
        double[] y = new double[np * np * np];
        double[] z = new double[np * np * np];

        double[] cellData = new double[ncells];
        double[] nodeData = new double[nnodes];

        // Fill arrays with coordinates
        int ii = 0;
        // Note this loop has the wrong index order, however
        // the coordinates are internally sorted, so it is not a problem.
        for (int k = 0; k < np; k++) {
            for (int i = 0; i < np; i++) {
                for (int j = 0; j < np; j++) {
                    x[ii] = i * dx;
                    y[ii] = j * dx;
                    z[ii] = k * dx;
                    ii += 1;
                }
            }
        }

        // Fill arrays that represent node and cell data
        ii = 0;
        for (int k = 0; k < np; k++) {
            for (int i = 0; i < np; i++) {
                for (int j = 0; j < np; j++) {
                    nodeData[ii] = ii;
                    ii += 1;
                }
            }
        }

        for (int i = 0; i < ncells; i++) {
            cellData[i] = i;
        }

        // Sometimes it is only necessary to add data to few cells,
        // which are defined by a ijk =(row, col, lay) combination.
        var ijk = new int[2][3];
        ijk[0] = new int[]{0, 0, 0};
        ijk[1] = new int[]{0, 1, 1};

        var values = new double[2][2];
        values[0] = new double[]{1.0, 0.0};
        values[1] = new double[]{2.0, -0.1};

        // Create grid and add variables
        StructuredGrid sg = new StructuredGrid(np, np, np);
        sg.setNodeCoordinates(x, y, z);
        sg.addCellVariable("Pressure", cellData);
        sg.addNodeVariable("Temperature", nodeData);
        sg.addCellVariable("Drn_head", ijk, values, 0, -999.0);

        // Export grid to file
        sg.toVTKAsASCII("tmp/ExStructuredGrid.vts");
    }
}
