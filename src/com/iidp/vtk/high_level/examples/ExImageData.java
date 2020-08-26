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

import java.util.Random;

import static com.iidp.vtk.high_level.EVTK.*;

public class ExImageData {

    public static void main(String[] args) throws Exception {
        var nc = 10;
        var ncells = new int[] {nc, nc, nc};
        var nnpoints = (nc + 1) * (nc + 1) * (nc + 1);
        var nncells = nc * nc * nc;

        var origin = new double[]{0.0, 0.0, 0.0};
        var spacing = new double[]{1.0, 1.0, 1.0};

        var cellData = makeCellData();
        var temp = new double[nncells];
        var rnd = new Random();
        for (int i = 0; i < nncells; i++) {
            temp[i] = rnd.nextDouble();
        }
        cellData.addData("temp", temp);

        var pointData = makePointData();

        var comments = makeComments();
        comments.add("Comment 1");
        comments.add("Comment 2");

        imageToVTK("tmp/Ex_image", ncells, origin, spacing, cellData, pointData, comments);
        System.out.println("*** ALL DONE ***");
    }
}
