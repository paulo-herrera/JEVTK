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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Writes XML to a binary file.
 *
 * This is useful to write VTK files that use XML for the header and can include
 * binary data.
 */
class VTKXmlWriter {
    //   public final static String ENCODING = "US-ASCII";
    //   final static Charset encoding = Charset.forName(ENCODING);

    /**
     * Stream connected to this writer.
     * <p>
     * This is public to allow direct writing.
     */
    public final DataOutputStream out;
    private boolean openTag = false;
    private static final int IO_BUFFER_SIZE = 8096;

    public VTKXmlWriter(File file) throws FileNotFoundException {
        out = new DataOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(file), IO_BUFFER_SIZE));
    }

    public final void close() throws IOException {
        if (openTag) out.writeBytes("/>");
        out.close();
    }

    public final VTKXmlWriter addDeclaration() throws IOException {
        out.writeBytes("<?xml version=\"1.0\"?>");
        return this;
    }

    /**
     * This doesn't work. I don't know why.
     */
    public final VTKXmlWriter addComment(String text) throws IOException {
        if (openTag) {
            out.writeBytes(">\n");
            openTag = false;
        }
        out.writeBytes("<!-- ");
        out.writeBytes(text);
        out.writeBytes(" -->");
        return this;
    }

    /**
     * Open element tag, without closing it to add attributes later.
     */
    public final VTKXmlWriter openElement(String tag) throws IOException {
        if (openTag) out.writeBytes(">");
        out.writeBytes("\n<");
        out.writeBytes(tag);
        openTag = true;
        return this;
    }

    /**
     * Closes tag of simple element without children
     */
    public VTKXmlWriter closeTag() throws IOException {
        assert openTag;
        out.writeBytes("/>");
        openTag = false;
        return this;
    }

    public VTKXmlWriter addText(String text) throws IOException {
        if (openTag) {
            out.writeBytes(">\n");
            openTag = false;
        }
        out.writeBytes(text);
        return this;
    }

    public VTKXmlWriter closeElement(String tag) throws IOException {
        if (openTag) {
            out.writeBytes(">");
            openTag = false;
        }
        out.writeBytes("\n</");
        out.writeBytes(tag);
        out.writeBytes(">");
        return this;
    }

    public VTKXmlWriter addAttribute(String name, String value) throws IOException {
        assert openTag;
        out.writeBytes(" ");
        out.writeBytes(name);
        out.writeBytes("=\"");
        out.writeBytes(value);
        out.writeBytes("\"");
        return this;
    }

    public VTKXmlWriter addAttribute(String name, int value) throws IOException {
        assert openTag;
        out.writeBytes(" ");
        out.writeBytes(name);
        out.writeBytes("=\"");
        out.writeBytes(Integer.toString(value));
        out.writeBytes("\"");
        return this;
    }

    public VTKXmlWriter addAttribute(String name, double value) throws IOException {
        assert openTag;
        out.writeBytes(" ");
        out.writeBytes(name);
        out.writeBytes("=\"");
        out.writeBytes(Double.toString(value));
        out.writeBytes("\"");
        return this;
    }

    //@Test
    public static void main(String[] args) throws FileNotFoundException, IOException {
        VTKXmlWriter xml = new VTKXmlWriter(new File("xml_writer.bin") );
        xml.addDeclaration();
        xml.openElement("child1");
        xml.addText("This is the text");
        xml.closeElement("child1");
        xml.openElement("child2");
        xml.openElement("child3")
                .addAttribute("id", "child3")
                .addAttribute("value", "None");
        xml.close();
    }

}

