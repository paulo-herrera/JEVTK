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

import static com.iidp.vtk.high_level.EVTK.*;

public class ExLines {
    public static void main(String[] args) throws Exception {
        var path = "tmp/Ex_lines";

        var npoints = 6;
        var ncells = 3;
        var x = new double[]{0.0, 0.0, 0.0, 4.0, 4.0, 4.0};
        var y = new double[]{-1.0, 1.0, 0.0, 0.0, -1.0, 1.0};
        var z = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

        var cellData = makeCellData();
        var ncell = new int[] {1, 2, 3};
        cellData.addData("ncell", ncell);

        var pointData = makePointData();
        var npoint = new int[] {1, 2, 3, 4, 5, 6};
        pointData.addData("npoint", npoint);

        var comments = makeComments();
        comments.add("Example of using linesToVTK");

        linesToVTK(path, x, y, z, cellData, pointData, comments);

        System.out.println("*** ALL DONE ***");
    }
}
