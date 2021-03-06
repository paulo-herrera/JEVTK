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

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Helper class to create a XML file using a StringBuffer.
 * Data is kept in memory until the file is closed and written to disk.
 * It uses "US-ASCII" as default encoding.
 *
 * See main method for an example of how to use it.
 */
public class XMLBuilder {
    public final static String ENCODING = "US-ASCII";
    final static Charset encoding = Charset.forName(ENCODING);

    private boolean openTag = false;
    private final StringBuffer sb;

    public XMLBuilder() {
        sb = new StringBuffer();
    }

    /**
     * Adds initial declaration for file including version and encoding.
     *
     * @return this XMLBuilder
     */
    public final XMLBuilder addDeclaration() {
        sb.append("<?xml version=\"1.0\" encoding=\"");
        sb.append(ENCODING);
        sb.append("\"?>\n");
        return this;
    }

    /**
     * Adds text as a comment.
     */
    public final XMLBuilder addComment(String text) {
        if (openTag) {
            sb.append(">\n");
            openTag = false;
        }
        sb.append("<!-- ");
        sb.append(text);
        sb.append(" -->\n");
        return this;
    }

    /**
     * Open element tag, without closing it to add attributes later.
     *
     * @param tag name of the element, e.g. addElement("grid")
     * @return this XMLBuilder
     */
    public final XMLBuilder addElement(String tag) {
        if (openTag) sb.append(">\n");
        sb.append("<");
        sb.append(tag);
        openTag = true;
        return this;
    }

    /**
     * Adds text as a child of an open element.
     *
     * @param text content that must be added to the file.
     * @return this XMLBuilder
     */
    public XMLBuilder addText(String text) {
        if (openTag) {
            sb.append(">\n");
            openTag = false;
        }
        sb.append(text);
        return this;
    }

    /**
     * Closes an open element.
     *
     * @param tag name of element that will be closed.
     *            NOTE: It does not check if the tag corresponds to the name of the element that is open.
     * @return this XMLBuilder
     */
    public XMLBuilder closeElement(String tag) {
        if (openTag) {
            sb.append(">\n");
            openTag = false;
        }
        sb.append("</");
        sb.append(tag);
        sb.append(">");
        // sb.append("  <!-- ");
        // sb.append(tag);
        // sb.append("-->");
        sb.append("\n");
        return this;
    }

    /**
     * Closes tag of simple element without children.
     *
     * This must be called explicitly.
     * @return this XMLBuilder
     */
    public XMLBuilder closeElement() {
        assert openTag;
        sb.append("/>\n");
        openTag = false;
        return this;
    }

    /**
     * Adds attribute to open element.
     *
     * @param name attribute name.
     * @param value attribute value.
     * @return this XMLBuilder.
     */
    public XMLBuilder addAttribute(String name, String value) {
        assert openTag;
        sb.append(" ");
        sb.append(name);
        sb.append("=\"");
        sb.append(value);
        sb.append("\"");
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    byte[] toBytes() {
        ByteBuffer bf = XMLBuilder.encoding.encode(sb.toString());
        return bf.array();
    }

    public static void main(String[] args) {
        var xml = new XMLBuilder();
        xml.addDeclaration();
        xml.addComment("Useless comment");
        xml.addElement("Root");
        xml.addAttribute("id", "file0");
        xml.addElement("text");
        xml.addText("Some nice text");
                xml.closeElement("text");
        System.out.println(xml);
    }

}
