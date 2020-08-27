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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Helper class to write a VTKGroup file that can be used for visualization of
 * time dependent data or for combining multiples grid files.
 */
public class VTKGroup {
    PrintWriter stream;
    XMLBuilder xml;

    /** Create a group file */
    public VTKGroup(File file) throws Exception {
        stream = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(file)));

        xml = new XMLBuilder();
        xml.addElement("VTKFile")
                .addAttribute("type", "Collection")
                .addAttribute("version", "0.1")
                .addAttribute("byte_order", "BigEndian");
        var sdate = LocalDateTime.now().toString();
        xml.addComment("Created: " + sdate);
        xml.addElement("Collection");
    }

    /** Closes group and write file. */
    public void close() {
        xml.closeElement("Collection");
        xml.closeElement("VTKFile");
        stream.write(xml.toString());
        stream.close();
    }

    /**
     * Add VTK file to this group.
     *
     * @param filename path to file.
     * @param time simulation time. This will be used to display time in animations.
     * @param part number of grid file if group is used to combine multiple files.
     * @return this VTKGroup.
     */
    public VTKGroup addFile(String filename, double time, int part) {
        xml.addElement("DataSet")
                .addAttribute("timestep", Double.toString(time))
                .addAttribute("group", "")
                .addAttribute("part", Integer.toString(part))
                .addAttribute("file", filename)
                .closeElement();

        return this;
    }

}