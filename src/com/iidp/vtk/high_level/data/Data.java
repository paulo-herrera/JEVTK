package com.iidp.vtk.high_level.data;

import com.iidp.vtk.low_level.VTKWriter;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Data {
    String type; // cellData or pointData
    List<PairData> pd;

    public Data(String _type) {
        type = _type;
        pd = new ArrayList<PairData>();
    }

    /*public List<PairData> getData() {
        return pd;
    } */

    /**
     * Add data to this container.
     * @param name: name of the data.
     * @param _data: values that should be associated to cells or points of the grid.
     * NOTE: array values must be given in the VTK grid order,
     *       i.e. x direction changing fastest, then y, then z.
     */
    public void addData(String name, double[] _data) {
        var p = PairData.makeDoublePair(name, _data);
        pd.add(p);
    }

    /**
     * Add data to this container.
     * @param name: name of the data.
     * @param _data: values that should be associated to cells or points of the grid.
     * NOTE: array values must be given in the VTK grid order,
     *       i.e. x direction changing fastest, then y, then z.
     */
    public void addData(String name, int[] _data) {
        var p = PairData.makeIntegerPair(name, _data);
        pd.add(p);
    }

    /**
     * Write data information to xml section of file.
     * @param vw
     * @param nnpoints
     * @throws Exception
     */
    public void addDataArray(VTKWriter vw, int nnpoints, int nncells) throws Exception {
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

        for (PairData p : pd) {
            assert  p.size() == size;
            vw.addDataArray(p.name, p.type, p.size(), 1);
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
