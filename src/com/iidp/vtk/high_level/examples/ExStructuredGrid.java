/*
 *  Copyright (C) 2009-2020 Paulo A. Herrera <pauloa.herrera@gmail.com>
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
package com.iidp.vtk.high_level.examples;

import com.iidp.vtk.high_level.EVTK;

import java.util.Random;

import static com.iidp.vtk.high_level.EVTK.makeComments;

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

        var comments = makeComments();
        comments.add("Comment 1");

        EVTK.structuredGridToVTK("tmp/structured", x, y, z, cellData, pointData, comments);

        System.out.println("*** ALL DONE ***");
    }
}
