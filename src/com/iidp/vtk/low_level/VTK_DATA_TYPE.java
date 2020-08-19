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
 * Enumerates data types allowed in VTK files.
 */
public enum VTK_DATA_TYPE {

    INT8, UINT8, INT16, UINT16, INT32, UINT32,
    INT64, UINT64, FLOAT32, FLOAT64;

    @Override
    public String toString() {
        switch (this) {
            case INT8:
                return "Int8";
            case UINT8:
                return "UInt8";
            case INT16:
                return "Int16";
            case UINT16:
                return "UInt16";
            case INT32:
                return "Int32";
            case UINT32:
                return "UInt32";
            case INT64:
                return "Int64";
            case UINT64:
                return "UInt64";
            case FLOAT32:
                return "Float32";
            case FLOAT64:
                return "Float64";
        }
        assert false;
        return "NONE";
    }

    /**
     * Returns size in bytes of type.
     */
    public int sizeof() {
        switch (this) {
            case INT8:
                return 1;
            case UINT8:
                return 1;
            case INT16:
                return 2;
            case UINT16:
                return 2;
            case INT32:
                return 4;
            case UINT32:
                return 4;
            case INT64:
                return 8;
            case UINT64:
                return 8;
            case FLOAT32:
                return 4;
            case FLOAT64:
                return 8;
        }
        assert false;
        return 0;
    }
}
