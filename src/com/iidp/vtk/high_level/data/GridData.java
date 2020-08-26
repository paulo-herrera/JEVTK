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

import com.iidp.vtk.low_level.VTKWriter;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GridData {
    String type; // cellData or pointData
    List<PairData> pd;

    public GridData(String _type) {
        type = _type;
        pd = new ArrayList<PairData>();
    }

    /**
     * Add data to this container.
     *
     * @param name:  name of the data.
     * @param _data: values that should be associated to cells or points of the grid.
     *               NOTE: array values must be given in the VTK grid order,
     *               i.e. x direction changing fastest, then y, then z.
     */
    public void addData(String name, double[] _data) {
        var p = PairData.makeDoublePair(name, _data);
        pd.add(p);
    }

    /**
     * Add data to this container.
     *
     * @param name:  name of the data.
     * @param _data: values that should be associated to cells or points of the grid.
     *               NOTE: array values must be given in the VTK grid order,
     *               i.e. x direction changing fastest, then y, then z.
     */
    public void addData(String name, int[] _data) {
        var p = PairData.makeIntegerPair(name, _data);
        pd.add(p);
    }

    /**
     *  Writes array declaration to xml section of file.
     *  Actual data must be appended to the binary section later on.
     *
     * @param vw: VTKWriter
     * @param nnpoints: number of points in grid
     * @param nncells: number of cells in grid
     * @throws Exception
     */
    public void addArrayToVTK(VTKWriter vw, int nnpoints, int nncells) throws Exception {
        addArrayToVTK(vw, nnpoints, nncells, false);
    }

    /**
     * Writes data information to xml section of file.
     * @param vw
     * @param nnpoints
     * @param writeAsASCII: If true, data is appended to the XML section as text.
     *                      If false, it only adds a declaration into the XML section,
     *                      but data must be manually appendended to the binary section later.
     * @throws Exception
     */
    public void addArrayToVTK(VTKWriter vw, int nnpoints, int nncells, boolean writeAsASCII) throws Exception {
        if (pd.size() == 0) return;

        var size = 0;

        var default_name = pd.get(0).name;
        if (type == "pointData") {
            vw.openPointData(default_name, null, null, null, null);
            size = nnpoints;
        } else if (type == "cellData") {
            vw.openCellData(default_name, null, null, null, null);
            size = nncells;
        }

        if (writeAsASCII) {
            for (PairData p : pd) {
                assert p.size() == size;
                p.addToVTKAsAscii(vw);
            }
        } else {
            for (PairData p : pd) {
                assert p.size() == size;
                vw.addDataArray(p.name, p.type, p.size(), 1);
            }
        }

        if (type == "pointData") {
            vw.closePointData();
        } else if (type == "cellData"){
            vw.closeCellData();
        }
    }

    /**
     * Writes data to binary section of the file.
     */
    public void appendData(VTKWriter vw, int nnpoints, int nncells) throws Exception {
        if (pd.size() == 0) return;

        var size = (type == "pointData") ? nnpoints : nncells;
        DataOutputStream dos = vw.getStream();
        for (PairData p : pd) {
            assert p.size() == size;
            p.appendTo(dos);
        }
    }
}
