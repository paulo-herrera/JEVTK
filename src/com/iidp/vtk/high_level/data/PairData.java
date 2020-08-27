/*
 * Copyright (C) 2009-2020 Paulo A. Herrera <paulo.herrera.eirl at gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.iidp.vtk.high_level.data;

import com.iidp.vtk.low_level.VTKWriter;
import com.iidp.vtk.low_level.VTK_DATA_TYPE;

import java.io.DataOutputStream;
import java.util.List;

/**
 * Container to store data associated to cells or nodes of the grid.
 * It also provides methods to append data to a binary file or write it
 * as text as part of the XML section of the file.
 *
 * Current version can store double or int data.
 */
public class PairData {
    public final String name;
    public final VTK_DATA_TYPE type;

    List<Integer> idata;
    List<Double> ddata;

    int _size;
    public int size() {
        return _size;
    }

    /**
     * Creates a PairData to store a double or int variable.
     * This should not be called directly, PairData are created
     * though the static factory methods.
     *
     * @param _name:  name of the variable.
     * @param _ddata: double data, if null then _idata should be a list.
     * @param _idata: int data, if null then _ddata should be a list.
     */
    private PairData(String _name, List<Double> _ddata, List<Integer> _idata) {
        name = _name;
        VTK_DATA_TYPE _t = null;
        if (_ddata != null) {
            _t = VTK_DATA_TYPE.FLOAT64;
            ddata = _ddata;
            _size = ddata.size();
        }

        if (_idata != null) {
            assert _t == null;
            _t = VTK_DATA_TYPE.INT32;
            idata = _idata;
            _size = idata.size();
        }
        type = _t;
        assert type != null;
    }

    /**
     * Appends data stored in this PairData to a binary stream.
     * NOTE: The data declaration should have been previously included in
     * the file XML section of the file.
     *
     * @param stream: binary stream.
     * @throws Exception
     */
    public void appendTo(DataOutputStream stream) throws Exception {
        stream.writeInt(this._size * type.sizeof());
        if (type == VTK_DATA_TYPE.FLOAT64) {
            Helpers.writeDouble(stream, ddata);
        } else if (type == VTK_DATA_TYPE.INT32) {
            Helpers.writeInt(stream, idata);
        }
    }

    /**
     * Writes data stored in this PairData as text to the XML section.
     *
     * @param vw: writer used to create this file.
     * @throws Exception
     */
    public void addToVTKAsAscii(VTKWriter vw) throws Exception {
       if (type == VTK_DATA_TYPE.FLOAT64) {
           vw.addDataArrayDoubleASCII(name, ddata);
       } else if (type == VTK_DATA_TYPE.INT32) {
           vw.addDataArrayIntASCII(name, idata);
       }
    }

    /**
     * Factory method to create a PairData that contains double data.
     *
     * @param name: name of the variable.
     * @param data: data to store.
     * @return a new PairData.
     */
    public static PairData makeDoublePair(String name, List<Double> data) {
        return new PairData(name, data, null);
    }

    /**
     * Factory method to create a PairData that contains double data.
     *
     * @param name: name of the variable.
     * @param data: data to store.
     * @return a new PairData.
     */
    public static PairData makeDoublePair(String name, double[] data) {
        var _data = Helpers.createList(data);
        assert _data != null;
        return new PairData(name, _data, null);
    }

    /**
     * Factory method to create a PairData that contains integer data.
     *
     * @param name: name of the variable.
     * @param data: data to store.
     * @return a new PairData.
     */
    public static PairData makeIntegerPair(String name, List<Integer> data) {
        return new PairData(name, null, data);
    }

    /**
     * Factory method to create a PairData that contains integer data.
     *
     * @param name: name of the variable.
     * @param data: data to store.
     * @return a new PairData.
     */
    public static PairData makeIntegerPair(String name, int[] data) {
        var _data = Helpers.createList(data);
        assert _data != null;
        return new PairData(name, null, _data);
    }
}
