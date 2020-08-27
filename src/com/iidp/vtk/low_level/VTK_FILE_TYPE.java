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
 * Allowed VTK file types.
 */
public enum VTK_FILE_TYPE {
    IMAGE_DATA, POLY_DATA, RECTILINEAR_GRID,
    STRUCTURED_GRID, UNSTRUCTURED_GRID;

    /**
     * Returns string representation of this type of file.
     */
    @Override
    public String toString() {
        switch(this) {
            case IMAGE_DATA:        return "ImageData";
            case POLY_DATA:         return "PolyData";
            case RECTILINEAR_GRID:  return "RectilinearGrid";
            case STRUCTURED_GRID:   return "StructuredGrid";
            case UNSTRUCTURED_GRID: return "UnstructuredGrid";
        }
        assert false;
        return "NONE";
    }

    /**
     * Returns file extension fir this file type.
     */
    public String extension() {
        switch(this) {
            case IMAGE_DATA:        return "vti";
            case POLY_DATA:         return "vtp";
            case RECTILINEAR_GRID:  return "vtr";
            case STRUCTURED_GRID:   return "vts";
            case UNSTRUCTURED_GRID: return "vtu";
        }
        assert false;
        return "NONE";
    }

}
