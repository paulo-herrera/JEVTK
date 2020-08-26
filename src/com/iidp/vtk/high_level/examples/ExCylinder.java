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

import static com.iidp.vtk.high_level.EVTK.cylinderToVTK;
import static com.iidp.vtk.high_level.EVTK.makeComments;

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

        var comments = makeComments();
        comments.add("Comment 1");

        cylinderToVTK("tmp/Ex_cylinder", x0, y0, z0, z1, radius, nlayers, npilars, cellData, pointData, comments);

        System.out.println("*** ALL DONE ***");
    }
}
