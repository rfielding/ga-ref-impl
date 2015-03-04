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
 * DoubleU.java
 *
 * Created on March 1, 2005, 8:56 PM
 *
 * Copyright 2005-2007 Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.util;

/**
 * Utilities for Double
 * @author  fontijne
 */
public final class DoubleU {

    /** Creates a new instance of DoubleU */
    public DoubleU() {
    }

    public static void main(String[] args) {
	double []a = new double[]{1.0001, 1.49, 2.02, -0.51, -10000.1};
	for (int i = 0; i < a.length; i++)
	    System.out.println(a[i] + " -> " + round(a[i], 0.07, 0.07));
    }

    /**
     * Rounds <code>what</code> to the nearest multiple X of <code>multipleOf</code>,
     * if |X - what| <= epsilon
     */
    public static double round(double what, double multipleOf, double epsilon) {
	double a = what / multipleOf;
	double b = Math.round(a) * multipleOf;
	return (Math.abs((what - b)) <= epsilon) ? b : what;
    }
}
