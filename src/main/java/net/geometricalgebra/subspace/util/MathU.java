// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

/*
 * Math.java
 *
 * Created on April 30, 2005, 3:39 PM
 *
 * Copyright 2005-2007 Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.util;

/**
 * Stuff that is missing from java.lang.Math (or missing in Java 1.4, anyway)
 *
 * @author  fontijne
 */
public class MathU {

	/** Creates a new instance of Math */
	public MathU() {
	}

	/**
	 * @return the hyperbolic cosine of @param x
	 *
	 * Computed as 0.5 (e^x + e^-x)
	 */
	public static double cosh(double x) {
		return 0.5 * (java.lang.Math.exp(x) + java.lang.Math.exp(-x));
	}

	/**
	 * @return the hyperbolic cosine of @param x
	 *
	 * Computed as 0.5 (e^x - e^-x)
	 */
	public static double sinh(double x) {
		return 0.5 * (java.lang.Math.exp(x) - java.lang.Math.exp(-x));
	}

}
