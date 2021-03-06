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
package com.iidp.vtk;

public class Version {
    public static final String MODULE_NAME     = "com.iidp.vtk";
    public static final String MAJOR_REVISION = "0";
    public static final String MINOR_REVISION = "9";

    /** Short revision string including revision and id numbers */
    public static String getRevision() {
        return MAJOR_REVISION + "." + MINOR_REVISION;
    }

    /** Long revision string including module. */
    public static String getLongRevString() {
        String sb = "";
        sb = sb + "* Module:   " + MODULE_NAME + "\n";
        sb = sb + "* Revision: " + getRevision() + "\n";
        return sb;
    }

    /** Prints version to std output */
    public static void report() {
        System.out.println("JEVTK VERSION");
        System.out.println("  * Module:   " + MODULE_NAME);
        System.out.println("  * Revision: " + getRevision());
    }

    /** Used only when only version must be printed from command line */
    public static void main(String[] args) {
        report();
    }
}