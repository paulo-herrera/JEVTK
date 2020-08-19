# INTRODUCTION

This is an implememtation of EVTK in Java.

EVTK (Export VTK) package allows exporting data to binary VTK files for
visualization and data analysis with any of the visualization packages that
support VTK files, e.g.  Paraview, VisIt and Mayavi. EVTK does not depend on any
external library (e.g. VTK), so it is easy to install in different systems.

Since its first version the package is composed only of a set of pure Java files, hence
it is straightforwrd to install and run in any system where Java is installed.
EVTK provides low and high level interfaces.  While the low level interface 
can be used to export data that is stored in any type of container, the high 
level functions make easy to export data stored as Java arrays. 

# EXAMPLE

To export a cylinder as an unstructured grid:

```
  double x0 = 0.0; double y0 = 0.0;
  double z0 = 0.0; double z1 = 20.0;
  double radius = 5.0;
  int nlayers = 3; int npilars = 10;

  var ncells = nlayers * npilars;
  var npoints = npilars * (nlayers + 1);

  var cellData = EVTK.makeCellData();
  var pointData = EVTK.makePointData();

  // Assign layer number to each cell
  var lay = new int[ncells];
  var ii = 0;
  for (int l = 0; l < nlayers; l++) {
    for (int p = 0; p < npilars; p++) {
        lay[ii] = l;
        ii = ii + 1;
    }
  }
  cellData.addData("layer", lay);

  cylinderToVTK("cylinder", x0, y0, z0, z1, radius, nlayers, npilars, cellData, pointData);
```

<a href="url"><img src="https://github.com/paulo-herrera/JEVTK/blob/master/src/com/iidp/vtk/high_level/examples/images/ExCylinder.png" align="center" height="400" width="550" ></a>

# INSTALLATION

The package has been developed with IntelliJ so it can be easily compiled and packaged as JAR file with it.

# DOCUMENTATION

This file together with the included examples in the high_level/examples directory in the
source tree provide enough information to start using the package.

# REQUIREMENTS

Tested with JDK-14.
    
# DESIGN GUIDELINES:

The design of the package considered the following objectives:

1. Self-contained. The package does not require any external library.

2. Flexibility. It is possible to use EVTK to export data stored in any
container and in any of the grid formats supported by VTK by using the low-level
interface.

3. Easy of use. The high level interface makes very easy to export data stored
in Java arrays. The high level interface provides functions to export most of
the grids supported by VTK: image data, rectilinear, structured and unstructured grids. 
It also includes a function to export point sets and associated data that can be
used to export results from particle and meshless numerical simulations.

4. Performance. The aim of the package is to be used as a part of
post-processing tools. Thus, good performance is important to handle the results
of large simulations. In general, Java performance is good enough for most applications.

# CONTRIBUTE:

I am open to incorporate bug fixes and additional improvements contributed by other
developers. As a non-native English speaker, I would also appreciate proof reading of
this page. Interesting pictures of grids exported using JEVTK would also be welcome.

# SUPPORT:

I will continue releasing this package as open source, so it is free to be used 
in any kind of project. I will also continue providing support for simple questions 
and making incremental improvements as time allows. 

**NOTE: JEVTK is a similar project to PyEVTK for Python (https://github.com/paulo-herrera/PyEVTK)**
