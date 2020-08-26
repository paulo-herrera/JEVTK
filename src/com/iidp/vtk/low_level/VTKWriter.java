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

import com.iidp.vtk.low_level.VTKXmlWriter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Writes binary VTK files.
 */
public class VTKWriter {

    /**
     * The binary order of the saved data.
     *
     * Java by default write binary data in big endian format
     */
    public final static String VTK_BYTE_ORDER = "BigEndian";

    private final VTKXmlWriter xw;
    private int offset = 0;

    /**
     * Creates VTK file.
     *
     * @param file name of file with extension.
     * @param type type of file.
     * throw Exception if the file extension does not match the given VTK_FILE_TYPE or
     *                  if the file is not found.
     */
    public VTKWriter(File file, VTK_FILE_TYPE type) throws Exception{
        if ( !file.getName().endsWith(type.extension()) )
            throw new Exception("Wrong file extension. Filename: " + file.getName() +
                    "  VTK_EXTENSION: " + type.extension());

        xw = new VTKXmlWriter(file);
        xw.addDeclaration();

        var sdate = LocalDateTime.now().toString();
        xw.addComment( "Created: " + sdate);

        xw.openElement("VTKFile").addAttribute("type", type.toString()).
                addAttribute("version", "0.1").addAttribute("byte_order", VTK_BYTE_ORDER);

    }

    /** Closes this VTK file. */
    public final void close() throws IOException {
        xw.closeElement("VTKFile");
        xw.close();
    }

    /**
     * Returns binary stream connected to this writer.
     *
     * This is useful to directly write into the appended section data that is not
     * stored in an array.
     *
     * NOTE: Before writing the actual data one must write the size in bytes
     *       of the data array. That usually means writing something like
     *       <code> stream.writeInt(dataSize * VTK_DATA_TYPE.XXX.sizeof() ); </code>.
     */
    public DataOutputStream getStream() {
        return xw.out;
    }

    public final VTKWriter openPiece(int[] start, int[] end) throws IOException {
        int[] ext = mix_extents(start, end);
        xw.openElement("Piece").addAttribute("Extent", IOArray.toString(ext));
        return this;
    }

    public final VTKWriter openPiece(int npoints, int ncells) throws IOException {
        xw.openElement("Piece").addAttribute("NumberOfPoints", npoints).
                addAttribute("NumberOfCells", ncells);
        return this;
    }

    public final VTKWriter openPiece(int npoints, int nverts, int nlines,
                                     int nstrips, int npolys) throws IOException {
        xw.openElement("Piece").addAttribute("NumberOfPoints", npoints).
                addAttribute("NumberOfVerts", nverts).addAttribute("NumberOfLines", nlines).
                addAttribute("NumberOfStrips", nstrips).addAttribute("NumberOfPolys", npolys);
        return this;
    }

    public final VTKWriter closePiece() throws IOException {
        xw.closeElement("Piece");
        return this;
    }

    public final VTKWriter openAppendedData() throws IOException {
        xw.openElement("AppendedData").addAttribute("encoding", "raw").addText("_");
        return this;
    }

    public final VTKWriter closeAppendedData() throws IOException {
        xw.closeElement("AppendedData");
        return this;
    }

    /**
     * Opens a point data section.
     *
     * The parameters should indicate the name of the DataArray containing Scalar, Vector, etc..
     * elements. Only non-null elements are added, so one can pass null
     * to avoid writing that attribute.
     */
    public final VTKWriter openPointData(String scalars, String vectors, String normals,
                                         String tensors, String tcoords) throws IOException {
        xw.openElement("PointData");
        if (scalars != null) {
            xw.addAttribute("Scalars", scalars);
        }
        if (vectors != null) {
            xw.addAttribute("Vectors", vectors);
        }
        if (normals != null) {
            xw.addAttribute("Normals", normals);
        }
        if (tensors != null) {
            xw.addAttribute("Tensors", tensors);
        }
        if (tcoords != null) {
            xw.addAttribute("TCoords", tcoords);
        }
        return this;
    }

    public final VTKWriter closePointData() throws IOException {
        xw.closeElement("PointData");
        return this;
    }

    /**
     * Opens a cell data section.
     *
     * The parameters should indicate the name of the DataArray containing Scalar, Vector, etc..
     * elements. Only non-null elements are added, so one can pass null
     * to avoid writing that attribute.
     */
    public final VTKWriter openCellData(String scalars, String vectors, String normals,
                                        String tensors, String tcoords) throws IOException {
        xw.openElement("CellData");
        if (scalars != null) {
            xw.addAttribute("Scalars", scalars);
        }
        if (vectors != null) {
            xw.addAttribute("Vectors", vectors);
        }
        if (normals != null) {
            xw.addAttribute("Normals", normals);
        }
        if (tensors != null) {
            xw.addAttribute("Tensors", tensors);
        }
        if (tcoords != null) {
            xw.addAttribute("TCoords", tcoords);
        }
        return this;
    }

    public final VTKWriter closeCellData() throws IOException {
        xw.closeElement("CellData");
        return this;
    }

    public final VTKWriter openStructuredGrid(int[] start, int[] end) throws IOException {
        int[] ext = mix_extents(start, end);
        xw.openElement("StructuredGrid").addAttribute("WholeExtent", IOArray.
                toString(ext));
        return this;
    }

    public final VTKWriter closeStructuredGrid() throws IOException {
        xw.closeElement("StructuredGrid");
        return this;
    }

    public final VTKWriter openRectilinearGrid(int[] start, int[] end) throws IOException {
        int[] ext = mix_extents(start, end);
        xw.openElement("RectilinearGrid").addAttribute("WholeExtent", IOArray.
                toString(ext));
        return this;
    }

    public final VTKWriter closeRectilinearGrid() throws IOException {
        xw.closeElement("RectilinearGrid");
        return this;
    }

    public final VTKWriter openUnstructuredGrid() throws IOException {
        xw.openElement("UnstructuredGrid");
        return this;
    }

    public final VTKWriter closeUnstructuredGrid() throws IOException {
        xw.closeElement("UnstructuredGrid");
        return this;
    }

    public final VTKWriter openPolyData() throws IOException {
        xw.openElement("PolyData");
        return this;
    }

    public final VTKWriter closePolyData() throws IOException {
        xw.closeElement("PolyData");
        return this;
    }

    public final VTKWriter openImageData(int[] start, int[] end,
                                         double[] origin, double[] spacing) throws IOException {
        int[] ext = mix_extents(start, end);
        xw.openElement("ImageData").addAttribute("WholeExtent", IOArray.toString(ext)).
                addAttribute("Origin", IOArray.toString(origin)).addAttribute("Spacing", IOArray.
                toString(spacing));
        return this;
    }

    public final VTKWriter closeImageData() throws IOException {
        xw.closeElement("ImageData");
        return this;
    }

    /**
     * Useful to add elements such as: Coordinates, Points, Verts, etc.
     */
    public final VTKWriter openElement(String name) throws IOException {
        xw.openElement(name);
        return this;
    }

    public final VTKWriter closeElement(String name) throws IOException {
        xw.closeElement(name);
        return this;
    }

    /**
     * Adds XML comment that is not part of VTK format.
     *
     * This doesn't work. I don't know why.
     */
    public final VTKWriter addComment(String text) throws Exception {
        xw.addComment(text);
        return this;
    }

    public final VTKWriter addComments(List<String> comments) throws Exception {
        xw.addComments(comments);
        return this;
    }

    /**
     * Add data array declaration to XML section of file.
     *
     * Array data must be added later calling one of the <code> writeArray </code>
     * methods or by directly writing to the stream, e.g. see
     * {link org.napa.vtk.examples.RectangularGrid RectangularGrid example}.
     *
     * @param name data description, e.g. "Pressure", etc.
     * @param type data type.
     * @param nelements number of points or cells.
     * @param ncomponents number of components per point or cell, i.e.: scalar = 1, vector = 3.
     */
    public final VTKWriter addDataArray(String name, VTK_DATA_TYPE type, int nelements, int ncomponents) throws IOException {
        //System.out.println("name: " + name);
        //System.out.println("type: " + type);

        xw.openElement("DataArray");
        var t = type.toString();
        xw.addAttribute("type", type.toString());
        xw.addAttribute("Name", name);
        xw.addAttribute("NumberOfComponents", ncomponents);
        xw.addAttribute("format", "appended");
        xw.addAttribute("offset", offset);
        xw.closeTag();

        offset += nelements * ncomponents * type.sizeof() + 4; // add 4 to indicate array size

        return this;
    }

    /**
     * Add data array in ASCII format.
     *
     * Array is immediately written to file, so it must not be appended in binary
     * section.
     *
     * @param name data description, e.g. "Pressure", etc.
     * @param data data values.
     */
    public final VTKWriter addDataArrayASCII(String name, int[] data) throws IOException {
        xw.openElement("DataArray").addAttribute("type", VTK_DATA_TYPE.INT32.toString()).
                addAttribute("Name", name).addAttribute("NumberOfComponents", 1).
                addAttribute("format", "ascii");
        xw.addText("");
        for (int i = 0; i < data.length; i++) {
            xw.out.writeBytes(Integer.toString(data[i]));
            xw.out.writeBytes(" ");
        }
        xw.closeElement("DataArray");

        // offset += nelements * ncomponents * type.sizeof() + 4; // add 4 to indicate array size
        return this;
    }

    /**
     * Add data array in ASCII format.
     *
     * Array is immediately written to file, so it must not be appended in binary
     * section.
     *
     * @param name data description, e.g. "Pressure", etc.
     * @param data data values.
     */
    public final VTKWriter addDataArrayIntASCII(String name, List<Integer> data) throws IOException {
        xw.openElement("DataArray").addAttribute("type", VTK_DATA_TYPE.INT32.toString()).
                addAttribute("Name", name).addAttribute("NumberOfComponents", 1).
                addAttribute("format", "ascii");
        xw.addText("");
        for (int i = 0; i < data.size(); i++) {
            xw.out.writeBytes(Integer.toString(data.get(i)));
            xw.out.writeBytes(" ");
        }
        xw.closeElement("DataArray");

        // offset += nelements * ncomponents * type.sizeof() + 4; // add 4 to indicate array size
        return this;
    }

    /**
     * Add data array in ASCII format.
     *
     * Array is immediately written to file, so it must not be appended in binary
     * section.
     *
     * @param name data description, e.g. "Pressure", etc.
     * @param data data values.
     * TODO: CHECK THIS METHOD IT USED TO WORK FINE.
     */
    public final VTKWriter addDataArrayASCII(String name, double[] data) throws IOException {
        xw.openElement("DataArray").addAttribute("type", VTK_DATA_TYPE.FLOAT64.toString()).
                addAttribute("Name", name).addAttribute("NumberOfComponents", 1).
                addAttribute("format", "ascii");
        xw.addText("");
        for (int i = 0; i < data.length; i++) {
            xw.out.writeBytes(Double.toString(data[i]));
            xw.out.writeBytes(" ");
        }
        xw.closeElement("DataArray");

        // offset += nelements * ncomponents * type.sizeof() + 4; // add 4 to indicate array size
        return this;
    }

    public final VTKWriter addDataArrayDoubleASCII(String name, List<Double> data) throws IOException {
        xw.openElement("DataArray").addAttribute("type", VTK_DATA_TYPE.FLOAT64.toString()).
                addAttribute("Name", name).addAttribute("NumberOfComponents", 1).
                addAttribute("format", "ascii");
        xw.addText("");
        for (int i = 0; i < data.size(); i++) {
            xw.out.writeBytes(Double.toString(data.get(i)));
            xw.out.writeBytes(" ");
        }
        xw.closeElement("DataArray");

        // offset += nelements * ncomponents * type.sizeof() + 4; // add 4 to indicate array size
        return this;
    }

    /**
     * Add data array with vector components in ASCII format.
     *
     * Array is immediately written to file, so it must not be appended in binary
     * section.
     *
     * @param name data description, e.g. "Velocity", etc.
     *  @param data data values. It must be a "truly" square array, i.e. all rows
     *             have the same number of elements or vector components.
     */
    public final VTKWriter addDataArrayASCII(String name, double[][] data) throws IOException {
        xw.openElement("DataArray").addAttribute("type", VTK_DATA_TYPE.FLOAT64.toString()).
                addAttribute("Name", name).addAttribute("NumberOfComponents", data[0].length).
                addAttribute("format", "ascii");
        xw.addText("");
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                xw.out.writeBytes(Double.toString(data[i][j]));
                xw.out.writeBytes(" ");
            }
            xw.out.writeBytes("\n");
        }
        xw.closeElement("DataArray");

        // offset += nelements * ncomponents * type.sizeof() + 4; // add 4 to indicate array size
        return this;
    }

    /** Appends byte array to appended section. */
    public final VTKWriter appendArray(byte[] a) throws IOException {
        xw.out.writeInt(a.length * VTK_DATA_TYPE.INT8.sizeof());
        for (int i = 0; i < a.length; i++) {
            xw.out.writeByte(a[i]);
        }
        return this;
    }

    /** Appends short array to appended section. */
    public final VTKWriter appendArray(short[] a) throws IOException {
        xw.out.writeInt(a.length * VTK_DATA_TYPE.INT16.sizeof());
        for (int i = 0; i < a.length; i++) {
            xw.out.writeShort(a[i]);
        }
        return this;
    }

    /** Appends int array to appended section. */
    public final VTKWriter appendArray(int[] a) throws IOException {
        xw.out.writeInt(a.length * VTK_DATA_TYPE.INT32.sizeof());
        for (int i = 0; i < a.length; i++) {
            xw.out.writeInt(a[i]);
        }
        return this;
    }

    /** Appends long array to appended section. */
    public final VTKWriter appendArray(long[] a) throws IOException {
        xw.out.writeInt(a.length * VTK_DATA_TYPE.INT64.sizeof() );
        for (int i = 0; i < a.length; i++) {
            xw.out.writeLong(a[i]);
        }
        return this;
    }

    /** Appends float array to appended section. */
    public final VTKWriter appendArray(float[] a) throws IOException {
        xw.out.writeInt(a.length * VTK_DATA_TYPE.FLOAT32.sizeof());
        for (int i = 0; i < a.length; i++) {
            xw.out.writeFloat(a[i]);
        }
        return this;
    }

    /** Appends double array to appended section. */
    public final VTKWriter appendArray(double[] a) throws IOException {
        xw.out.writeInt(a.length * VTK_DATA_TYPE.FLOAT64.sizeof());
        for (int i = 0; i < a.length; i++) {
            xw.out.writeDouble(a[i]);
        }
        return this;
    }

    public final VTKWriter appendArraysAs3D(double[] x, double[] y, double[] z) throws Exception {
        var nnpoints = x.length;
        assert (nnpoints == y.length) && (nnpoints == z.length);

        xw.out.writeInt(nnpoints * VTK_DATA_TYPE.FLOAT64.sizeof() * 3);
        for (int i = 0; i < nnpoints; i++) {
            xw.out.writeDouble(x[i]);
            xw.out.writeDouble(y[i]);
            xw.out.writeDouble(z[i]);
        }
        return this;
    }

    // Helper function.
    private int[] mix_extents(int[] start, int[] end) {
        assert start.length == 3;
        assert end.length == 3;

        int[] ext = new int[6];
        ext[0] = start[0];
        ext[1] = end[0];
        ext[2] = start[1];
        ext[3] = end[1];
        ext[4] = start[2];
        ext[5] = end[2];
        return ext;
    }

    /**
     * Provides some methods to create string representations of arrays.
     */
    private static class IOArray {

        /**
         * Returns string representation of integer array.
         */
        public static String toString(int[] a) {
            StringBuilder sb = new StringBuilder();
            for (int e : a) {
                sb.append(Integer.toString(e));
                sb.append(" ");
            }
            return sb.toString();
        }

        /**
         * Returns string representation of double array.
         */
        public static String toString(double[] a) {
            StringBuilder sb = new StringBuilder();
            for (double e : a) {
                sb.append(Double.toString(e));
                sb.append(" ");
            }
            return sb.toString();
        }
    }
}
