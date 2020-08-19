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
package com.iidp.vtk.low_level;

/**
 * Enumerates cell types allowed in VTK files.
 */
public enum VTK_CELL_TYPE {
    VTK_VERTEX, VTK_POLY_VERTEX, VTK_LINE, VTK_POLY_LINE,
    VTK_TRIANGLE, VTK_TRIANGLE_STRIP, VTK_POLYGON, VTK_PIXEL,
    VTK_QUAD, VTK_TETRA, VTK_VOXEL, VTK_HEXAHEDRON, VTK_WEDGE,
    VTK_PYRAMID, VTK_QUADRATIC_EDGE, VTK_QUADRATIC_TRIANGLE,
    VTK_QUADRATIC_QUAD, VTK_QUADRATIC_TETRA, VTK_QUADRATIC_HEXAHEDRON;

    public byte valueOf() {
        switch (this) {
            case VTK_VERTEX:
                return 1;
            case VTK_POLY_VERTEX:
                return 2;
            case VTK_LINE:
                return 3;
            case VTK_POLY_LINE:
                return 4;
            case VTK_TRIANGLE:
                return 5;
            case VTK_TRIANGLE_STRIP:
                return 6;
            case VTK_POLYGON:
                return 7;
            case VTK_PIXEL:
                return 8;
            case VTK_QUAD:
                return 9;
            case VTK_TETRA:
                return 10;
            case VTK_VOXEL:
                return 11;
            case VTK_HEXAHEDRON:
                return 12;
            case VTK_WEDGE:
                return 13;
            case VTK_PYRAMID:
                return 14;
            case VTK_QUADRATIC_EDGE:
                return 21;
            case VTK_QUADRATIC_TRIANGLE:
                return 22;
            case VTK_QUADRATIC_QUAD:
                return 23;
            case VTK_QUADRATIC_TETRA:
                return 24;
            case VTK_QUADRATIC_HEXAHEDRON:
                return 25;
        }
        assert false;
        return 0;
    }

    public static byte[] asByteArray(VTK_CELL_TYPE[] t) {
        var b = new byte[t.length];
        for (int i = 0; i < t.length; i++) {
            b[i] = t[i].valueOf();
        }
        return b;
    }
}