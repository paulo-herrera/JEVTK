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

public class ExPoints {
    public static void main(String[] args) throws Exception {
        var npoints = 25;
        var x = new double[npoints];
        var y = new double[npoints];
        var z = new double[npoints];
        var temp = new double[npoints];

        var rnd = new Random();
        for (int i = 0; i < npoints; i++) {
            x[i] = rnd.nextDouble();
            y[i] = rnd.nextDouble();
            z[i] = rnd.nextDouble();
            temp[i] = rnd.nextDouble();
        }

        var pointData = EVTK.makePointData();
        pointData.addData("temp", temp);

        var comments = makeComments();
        comments.add("Comment 1");
        comments.add("Comment 2");

        EVTK.pointsToVTK("tmp/points", x, y, z, pointData, comments);

        System.out.println("*** ALL DONE ***");
    }
}
