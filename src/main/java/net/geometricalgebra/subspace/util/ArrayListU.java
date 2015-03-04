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
 * ArrayListU.java
 *
 * Created on February 3, 2005, 11:29 AM
 *
 * Copyright 2005-2007 Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.util;

import java.util.*;

/**
 *
 * @author  fontijne
 */
public final class ArrayListU {

	/** Creates a new instance of ArrayListU */
	public ArrayListU() {
	}

	/**
	 * An alternative version of ArrayList.equals().
	 * @return true is A and B are of equal length and
	 * contain the same (as in A.get(i) == B.get(i)) Objects
	 */
	public static boolean equals(ArrayList A, ArrayList B) {
		if (A.size() != B.size())
			return false;

		for (int i = 0; i < A.size(); i++)
			if (A.get(i) != B.get(i))
				return false;

		return true;
	}

	/** @return new empty ArrayList */
	public static ArrayList newArrayList() {
		return new ArrayList();
	}

	/** @return new ArrayList with one Object as initial content */
	public static ArrayList newArrayList(Object O1) {
		ArrayList A = new ArrayList();
		A.add(O1);
		return A;
	}

	/** @return new ArrayList with 2 Objects as initial content */
	public static ArrayList newArrayList(Object O1, Object O2) {
		ArrayList A = new ArrayList();
		A.add(O1);
		A.add(O2);
		return A;
	}

	/** @return new ArrayList with 3 Objects as initial content */
	public static ArrayList newArrayList(Object O1, Object O2, Object O3) {
		ArrayList A = new ArrayList();
		A.add(O1);
		A.add(O2);
		A.add(O3);
		return A;
	}

	/** @return new ArrayList with 4 Objects as initial content */
	public static ArrayList newArrayList(Object O1, Object O2, Object O3,
			Object O4) {
		ArrayList A = new ArrayList();
		A.add(O1);
		A.add(O2);
		A.add(O3);
		A.add(O4);
		return A;
	}

	/** @return new ArrayList with 5 Objects as initial content */
	public static ArrayList newArrayList(Object O1, Object O2, Object O3,
			Object O4, Object O5) {
		ArrayList A = new ArrayList();
		A.add(O1);
		A.add(O2);
		A.add(O3);
		A.add(O4);
		A.add(O5);
		return A;
	}

	/** @return new ArrayList with 6 Objects as initial content */
	public static ArrayList newArrayList(Object O1, Object O2, Object O3,
			Object O4, Object O5, Object O6) {
		ArrayList A = new ArrayList();
		A.add(O1);
		A.add(O2);
		A.add(O3);
		A.add(O4);
		A.add(O5);
		A.add(O6);
		return A;
	}

	/** @return new ArrayList with 7 Objects as initial content */
	public static ArrayList newArrayList(Object O1, Object O2, Object O3,
			Object O4, Object O5, Object O6, Object O7) {
		ArrayList A = new ArrayList();
		A.add(O1);
		A.add(O2);
		A.add(O3);
		A.add(O4);
		A.add(O5);
		A.add(O6);
		A.add(O7);
		return A;
	}

	/** @return new ArrayList with 8 Objects as initial content */
	public static ArrayList newArrayList(Object O1, Object O2, Object O3,
			Object O4, Object O5, Object O6, Object O7, Object O8) {
		ArrayList A = new ArrayList();
		A.add(O1);
		A.add(O2);
		A.add(O3);
		A.add(O4);
		A.add(O5);
		A.add(O6);
		A.add(O7);
		A.add(O8);
		return A;
	}

	/** @return a new ArrayList with the elements [from ... to) from L */
	public static ArrayList crop(ArrayList L, int from, int to) {
		if ((from < 0) || (to < from) || (to > L.size()))
			throw new ArrayIndexOutOfBoundsException("Invalid bounds");
		ArrayList R = new ArrayList();
		for (int i = from; i < to; i++)
			R.add(L.get(i));
		return R;
	}

	/** @return a new ArrayList that is created as follows:
	 * L = new ArrayList(); L.addAll(C1); L.addAll(C2);
	 */
	public static ArrayList merge(Collection C1, Collection C2) {
		ArrayList L = new ArrayList();
		L.addAll(C1);
		L.addAll(C2);
		return L;
	}

	/** returns <code>L</code> with null objects removes (so actually changes L) */
	public static ArrayList removeNullObjects(ArrayList L) {
		for (int i = L.size() - 1; i >= 0; i--)
			if (L.get(i) == null)
				L.remove(i);
		return L;
	}

}
