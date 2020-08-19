package com.iidp.vtk.high_level.data;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Helpers {

    public static void writeDouble(DataOutputStream stream, List<Double> data) throws Exception {
        for(Double d: data) {
            stream.writeDouble(d);
        }
    }

    public static void writeInt(DataOutputStream stream, List<Integer> data) throws Exception {
        for(Integer i: data) {
            stream.writeInt(i);
        }
    }

    public static List<Double> createList(double[] a) {
        var l = new ArrayList<Double>(a.length);
        for (int i = 0; i < a.length; i++) {
            var d = Double.valueOf(a[i]);
            l.add(d);
        }
        return l;
    }

    public static List<Integer> createList(int[] a) {
        var l = new ArrayList<Integer>(a.length);
        for (int i = 0; i < a.length; i++) {
            var d = Integer.valueOf(a[i]);
            l.add(d);
        }
        return l;
    }
}
