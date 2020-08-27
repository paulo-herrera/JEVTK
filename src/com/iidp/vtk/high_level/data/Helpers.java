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
package com.iidp.vtk.high_level.data;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper static methods used by other classes.
 */
public class Helpers {

    /**
     * Writes double values to binary file.
     *
     * @param stream:
     * @param data:
     * @throws Exception
     */
    public static void writeDouble(DataOutputStream stream, List<Double> data) throws Exception {
        for(Double d: data) {
            stream.writeDouble(d);
        }
    }

    /**
     * Writes integer values to binary file.
     *
     * @param stream:
     * @param data:
     * @throws Exception
     */
    public static void writeInt(DataOutputStream stream, List<Integer> data) throws Exception {
        for(Integer i: data) {
            stream.writeInt(i);
        }
    }

    /**
     * Makes a list of double values from an array.
     *
     * @param a: array double[]
     * @return a List(Double) with elements of array a
     */
    public static List<Double> createList(double[] a) {
        var l = new ArrayList<Double>(a.length);
        for (int i = 0; i < a.length; i++) {
            var d = Double.valueOf(a[i]);
            l.add(d);
        }
        return l;
    }

    /**
     * Makes a list of integer values from an array.
     *
     * @param a: array int[]
     * @return a List<Integer> with elements of array a
     */
    public static List<Integer> createList(int[] a) {
        var l = new ArrayList<Integer>(a.length);
        for (int i = 0; i < a.length; i++) {
            var d = Integer.valueOf(a[i]);
            l.add(d);
        }
        return l;
    }
}
