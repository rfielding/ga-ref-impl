Geometric Algebra Repackaged for Github and Maven
=================================================

* Tested with Java7 and Maven2 on Linux

Maven simplifies the process of getting software compiled and built,
provided you hava gotten through Java7 (JDK, not just JRE! you need a compiler!).
Maven will first download every jar file on the internet, with this Geometric Algebra
code "colt" linear algebra library as a dependency.

Targets

* mvn test                    #run tests (and build)
* mvn java-formatter:format   #reformat code to standards

Maven has targets for setting up Eclipse IDE

On Linux:

ctags -R . #generate cross-reference for vim 

The package names had net.geometricalgebra appended to them to ensure
that the jar files packaged out of the libraries resemble the namespaces,
and to ensure uniqueness.

I repackaged this code myself just to be able to use it after downloading it,
and will be notifying the author of this code shortly.

--Rob Fielding

Original readme from www.geometricalgebra.net source
=============================================
 
This directory contains the Reference Implementation from the book
Geometric Algebra for Computer Science: 
An Object Oriented Approach To Geometry.

The main content is in the subspace.basis directory.
subspace.util and subspace.metric are just some support classes.
You can run 'java test' in this directory just to see if all
classes are present and working.

Do make sure you have the Colt packages (colt.jar) in you classpath.
It is used for its linear algebra implementation.
You can get it from:
http://dsd.lbl.gov/~hoschek/colt/

You can view the source code as HTML at:
http://www.geometricalgebra.net

The most interesting files are:

* BasisBlade.java                    Geometric algebra for weighted basis blades (Chapter 19)
* Multivector.java                   Geometric algebra for multivector, inverse, exp, sin, cos, etc (Chapters 20, 21)
* MeetJoin.java                      Meet and Join algorithm (Section 21.7)
* Metric.java                        Metric, Eigenvalue Decomposition (Section 19.4)
* MultivectorType.java               Determinining the 'multivector type' of a multivector (Section 21.5)
* Util.java                          Blade factorization (Section 21.6), rotationMatrixToRotor, orthogonalization w.r.t. any metric

--Daniel Fontijne (fontijne@science.uva.nl)

Last change: 20070104

