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

import com.iidp.vtk.low_level.VTK_DATA_TYPE;

import java.io.DataOutputStream;
import java.util.List;

/**
 * Container to store data.
 *
 * @author Paulo A. Herrera paulo.herrera.eirl at gmail.com
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

    public void appendTo(DataOutputStream stream) throws Exception {
        stream.writeInt(this._size * type.sizeof());
        if (type == VTK_DATA_TYPE.FLOAT64) {
            Helpers.writeDouble(stream, ddata);
        } else if (type == VTK_DATA_TYPE.INT32) {
            Helpers.writeInt(stream, idata);
        }
    }

    public static PairData makeDoublePair(String name, List<Double> data) {
        return new PairData(name, data, null);
    }

    public static PairData makeDoublePair(String name, double[] data) {
        var _data = Helpers.createList(data);
        assert _data != null;
        return new PairData(name, _data, null);
    }

    public static PairData makeIntegerPair(String name, List<Integer> data) {
        return new PairData(name, null, data);
    }

    public static PairData makeIntegerPair(String name, int[] data) {
        var _data = Helpers.createList(data);
        assert _data != null;
        return new PairData(name, null, _data);
    }
}
