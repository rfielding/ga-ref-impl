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
 * Bits.java
 *
 * Created on December 25, 2004, 1:59 PM
 *
 * Copyright 2004-2007 Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.util;

/**
 * Many of these functions adapted from:
 * Henry S. Warren, Jr.'s Hacker's Delight, (Addison Wesley, 2002)
 *
 * <p>Matched JDK 1.5 function signature for identical functions in
 * JDK 1.5 so the functions in here
 * can be mapped to that JDK version once everyone uses it . . .
 * @author  fontijne
 */
public class Bits {

	public static void main(String[] args) {
		System.out.println("OK: " + numberOfLeadingZeroBits(0x40000000));

		/*int x;
		x = 4;
		System.out.println(x + " numberOfLeadingZeroBits: " + numberOfLeadingZeroBits(x));
		x = 0x40000000;
		System.out.println(x + " numberOfLeadingZeroBits: " + numberOfLeadingZeroBits(x));
		x = 4;
		System.out.println(x + " numberOfTrailingZeroBits: " + numberOfTrailingZeroBits(x));
		x = 4;
		System.out.println(x + " lowestOneBit: " + lowestOneBit(x));
		x = 0;
		System.out.println(x + " lowestOneBit: " + lowestOneBit(x));
		x = 0;
		System.out.println(x + " highestOneBit: " + highestOneBit(x));*/
	}

	/** Creates a new instance of Integer */
	public Bits() {
	}

	// *!*HTML_TAG*!* bitCount
	/**
	 * @return the number of 1 bits in <code>i</code>
	 */
	public static int bitCount(int i) {
		// Note that unsigned shifting (>>>) is not required.
		i = i - ((i >> 1) & 0x55555555);
		i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
		i = (i + (i >> 4)) & 0x0F0F0F0F;
		i = i + (i >> 8);
		i = i + (i >> 16);
		return i & 0x0000003F;
	}

	/** returns in the index [-1 31] of the highest bit that is on in <code>i</code> (-1 is returned if no bit is on at all (i == 0)) */
	public static int highestOneBit(int i) {
		return 31 - numberOfLeadingZeroBits(i);
	}

	/** returns in the index [0 32] of the lowest bit that is on in <code>i</code> (32 is returned if no bit is on at all (i == 0))*/
	public static int lowestOneBit(int i) {
		return numberOfTrailingZeroBits(i);
	}

	/** returns the number of 0 bits before the first 1 bit in <code>i</code>
	 * <p>For example if i = 4 (100 binary), then 29 (31 - 2) is returned.
	 */
	public static int numberOfLeadingZeroBits(int i) {
		// Note that unsigned shifting (>>>) is not required.
		i = i | (i >> 1);
		i = i | (i >> 2);
		i = i | (i >> 4);
		i = i | (i >> 8);
		i = i | (i >> 16);
		return bitCount(~i);
	}

	/**
	 * returns the number of 0 bits after the last 1 bit in <code>i</code>
	 * <p>For example if i = 4 (100 binary), then 2 is returned.
	 */
	public static int numberOfTrailingZeroBits(int i) {
		return bitCount(~i & (i - 1));
	}

	/** 'rounds' <code>i</code> to first power-of-2 integer larger than or equal to <code>i</code>
	 * Treats <code>i</code> as unsigned int
	 */
	public static int roundUpPow2(int i) {
		i = i - 1;
		i = i | (i >> 1);
		i = i | (i >> 2);
		i = i | (i >> 4);
		i = i | (i >> 8);
		i = i | (i >> 16);
		return i + 1;
	}

	/** 'rounds' <code>i</code> to first power-of-2 integer smallar than or equal to <code>i</code>
	 * Treats <code>i</code> as unsigned int
	 */
	public static int roundDownPow2(int i) {
		i = i | (i >> 1);
		i = i | (i >> 2);
		i = i | (i >> 4);
		i = i | (i >> 8);
		i = i | (i >> 16);
		return i - (i >> 1);
	}

}
