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
 * BasisBlade.java
 *
 * Created on February 1, 2005, 11:41 AM
 *
 * Copyright 2005-2007 Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.basis;

import java.util.ArrayList;
import net.geometricalgebra.subspace.util.*;
import net.geometricalgebra.subspace.metric.*;

/**
 * A simple class to represent a basis blade.
 *
 * <p>mutable :( Should have made it immutable . . .
 *
 * <p>Could use subspace.util.Bits.lowestOneBit() and such to make
 * loops slightly more efficient, but didn't to keep text simple for the book.
 *
 * @author  fontijne
 */
final public class BasisBlade implements Cloneable, InnerProductTypes {

	/** temp for testing */
	public static void main(String args[]) {
		BasisBlade e1 = new BasisBlade(1);
		BasisBlade no = new BasisBlade(2);
		BasisBlade ni = new BasisBlade(4);

		double[][] m = new double[][]{{1.0, 0.0, 0.0, 0.0, 0.0},
				{0.0, 1.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0, 0.0},
				{0.0, 0.0, 0.0, 0.0, -1.0}, {0.0, 0.0, 0.0, -1.0, 0.0}};

		try {
			Metric M = new Metric(m);
			{
				for (int i = 0; i < 32; i++)
					for (int j = 0; j < 32; j++) {
						BasisBlade a = new BasisBlade(i);
						BasisBlade b = new BasisBlade(j);
						ArrayList R = Util.round(gp(a, b, M), 1.0, 0.0001);
					}
			}
			/*
			ArrayList L = new ArrayList();
			long t = System.currentTimeMillis();
			for (int i = 0; i < 32; i++)
			for (int j = 0; j < 32; j++) {
				BasisBlade a = new BasisBlade(i);
				BasisBlade b = new BasisBlade(j);
				ArrayList R = gp(a, b, M);
				System.out.println(a + " " + b + " = " + R);
				L.add(R);
			}

			System.out.println("time: " + (System.currentTimeMillis() - t));*/
		} catch (MetricException E) {
			System.out.println("Exception: " + E);
		}

	}

	/** constructs an instance of BasisBlade */
	public BasisBlade(int b, double s) {
		bitmap = b;
		scale = s;
	}

	/** constructs an instance of a unit BasisBlade */
	public BasisBlade(int b) {
		bitmap = b;
		scale = 1.0;
	}

	/** constructs an instance of a scalar BasisBlade */
	public BasisBlade(double s) {
		bitmap = 0;
		scale = s;
	}

	/** constructs an instance of a zero BasisBlade */
	public BasisBlade() {
		bitmap = 0;
		scale = 0.0;
	}

	// *!*HTML_TAG*!* canonicalReorderingSign
	/**
	 * Returns sign change due to putting the blade blades represented
	 * by <code>a<code> and <code>b</code> into canonical order.
	 */
	public static double canonicalReorderingSign(int a, int b) {
		// Count the number of basis vector flips required to
		// get a and b into canonical order.
		a >>= 1;
		int sum = 0;
		while (a != 0) {
			sum += Bits.bitCount(a & b);
			a >>= 1;
		}

		// even number of flips -> return 1
		// odd number of flips -> return 1
		return ((sum & 1) == 0) ? 1.0 : -1.0;
	}

	/** shortcut to outerProduct() */
	public static BasisBlade op(BasisBlade a, BasisBlade b) {
		return outerProduct(a, b);
	}

	/** @return the outer product of two basis blades */
	public static BasisBlade outerProduct(BasisBlade a, BasisBlade b) {
		return gp_op(a, b, true);
	}

	/** shortcut to geometricProduct() */
	public static BasisBlade gp(BasisBlade a, BasisBlade b) {
		return geometricProduct(a, b);
	}

	/** return the geometric product of two basis blades */
	public static BasisBlade geometricProduct(BasisBlade a, BasisBlade b) {
		return gp_op(a, b, false);
	}

	// *!*HTML_TAG*!* gp_op
	/**
	 * @return the geometric product or the outer product
	 * of 'a' and 'b'.
	 */
	protected static BasisBlade gp_op(BasisBlade a, BasisBlade b, boolean outer) {
		// if outer product: check for independence
		if (outer && ((a.bitmap & b.bitmap) != 0))
			return new BasisBlade(0.0);

		// compute the bitmap:
		int bitmap = a.bitmap ^ b.bitmap;

		// compute the sign change due to reordering:
		double sign = canonicalReorderingSign(a.bitmap, b.bitmap);

		// return result:
		return new BasisBlade(bitmap, sign * a.scale * b.scale);
	}

	/** shortcut to geometricProduct() */
	public static BasisBlade gp(BasisBlade a, BasisBlade b, double[] m) {
		return geometricProduct(a, b, m);
	}

	// *!*HTML_TAG*!* reverse
	/**
	 * @return reverse of this basis blade (always a newly constructed blade)
	 */
	public BasisBlade reverse() {
		// multiplier = (-1)^(x(x-1)/2)
		return new BasisBlade(bitmap,
				minusOnePow((grade() * (grade() - 1)) / 2) * scale);
	}

	// *!*HTML_TAG*!* grade_inversion
	/**
	 * @return grade inversion of this basis blade (always a newly constructed blade)
	 */
	public BasisBlade gradeInversion() {
		// multiplier is (-1)^x
		return new BasisBlade(bitmap, minusOnePow(grade()) * scale);
	}

	// *!*HTML_TAG*!* clifford_conjugate
	/**
	 * @return clifford conjugate of this basis blade (always a newly constructed blade)
	 */
	public BasisBlade cliffordConjugate() {
		// multiplier is ((-1)^(x(x+1)/2)
		return new BasisBlade(bitmap,
				minusOnePow((grade() * (grade() + 1)) / 2) * scale);
	}

	// *!*HTML_TAG*!* gp_restricted_NE_metric
	/**
	 * Computes the geometric product of two basis blades in limited non-Euclidean metric.
	 * @param m is an array of doubles giving the metric for each basis vector.
	 */
	public static BasisBlade geometricProduct(BasisBlade a, BasisBlade b,
			double[] m) {
		// compute the geometric product in Euclidean metric:
		BasisBlade result = geometricProduct(a, b);

		// compute the meet (bitmap of annihilated vectors):
		int bitmap = a.bitmap & b.bitmap;

		// change the scale according to the metric:
		int i = 0;
		while (bitmap != 0) {
			if ((bitmap & 1) != 0)
				result.scale *= m[i];
			i++;
			bitmap = bitmap >> 1;
		}

		return result;
	}

	public static ArrayList gp(BasisBlade a, BasisBlade b, Metric M) {
		return geometricProduct(a, b, M);
	}

	// *!*HTML_TAG*!* gp_arbitrary_metric
	/**
	 * Computes the geometric product of two basis blades in arbitary non-Euclidean metric.
	 * @param M is an instance of Metric giving the metric (and precomputed eigen vectors).
	 * @return an ArrayList, because the result does not have to be a single BasisBlade anymore . . .
	 */
	public static ArrayList geometricProduct(BasisBlade a, BasisBlade b,
			Metric M) {
		// convert argument to eigenbasis
		ArrayList A = M.toEigenbasis(a);
		ArrayList B = M.toEigenbasis(b);

		ArrayList result = new ArrayList();
		for (int i = 0; i < A.size(); i++) {
			for (int j = 0; j < B.size(); j++) {
				result.add(gp((BasisBlade) A.get(i), (BasisBlade) B.get(j), M
						.getEigenMetric()));
			}
		}

		return M.toMetricBasis(simplify(result));
	}

	/**
	 * @return the geometric product of two basis blades
	 * @param type gives the type of inner product:
	 * LEFT_CONTRACTION,RIGHT_CONTRACTION, HESTENES_INNER_PRODUCT or MODIFIED_HESTENES_INNER_PRODUCT.
	 */
	public static BasisBlade innerProduct(BasisBlade a, BasisBlade b, int type) {
		return innerProductFilter(a.grade(), b.grade(), geometricProduct(a, b),
				type);
	}

	/**
	 * Computes the inner product of two basis blades in limited non-Euclidean metric.
	 * @param m is an array of doubles giving the metric for each basis vector.
	 * @param type gives the type of inner product:
	 * LEFT_CONTRACTION,RIGHT_CONTRACTION, HESTENES_INNER_PRODUCT or MODIFIED_HESTENES_INNER_PRODUCT.
	 */
	public static BasisBlade innerProduct(BasisBlade a, BasisBlade b,
			double[] m, int type) {
		return innerProductFilter(a.grade(), b.grade(), geometricProduct(a, b,
				m), type);
	}

	/**
	 * Computes the inner product of two basis blades in arbitary non-Euclidean metric.
	 * @param M is an instance of Metric giving the metric (and precomputed eigen vectors).
	 * @param type gives the type of inner product:
	 * LEFT_CONTRACTION,RIGHT_CONTRACTION, HESTENES_INNER_PRODUCT or MODIFIED_HESTENES_INNER_PRODUCT.
	 * @return an ArrayList, because the result does not have to be a single BasisBlade anymore . . .
	 *
	 * <p>Todo: no need to return an ArrayList here? Result is always a single blade?
	 */
	public static ArrayList innerProduct(BasisBlade a, BasisBlade b, Metric M,
			int type) {
		return innerProductFilter(a.grade(), b.grade(), geometricProduct(a, b,
				M), type);
	}

	/** shortcut to innerProduct(...) */
	public static BasisBlade ip(BasisBlade a, BasisBlade b, int type) {
		return innerProduct(a, b, type);
	}

	/** shortcut to innerProduct(...) */
	public static BasisBlade ip(BasisBlade a, BasisBlade b, double[] m, int type) {
		return innerProduct(a, b, m, type);
	}

	/** shortcut to innerProduct(...) */
	public static ArrayList ip(BasisBlade a, BasisBlade b, Metric M, int type) {
		return innerProduct(a, b, M, type);
	}

	/** returns the grade of this blade */
	public int grade() {
		return Bits.bitCount(bitmap);
	}

	// *!*HTML_TAG*!* minus_one_pow
	/** @return pow(-1, i) */
	public static int minusOnePow(int i) {
		return ((i & 1) == 0) ? 1 : -1;
	}

	public boolean equals(Object O) {
		if (O instanceof BasisBlade) {
			BasisBlade B = (BasisBlade) O;
			return ((B.bitmap == bitmap) && (B.scale == scale));
		} else
			return false;
	}

	public int hashCode() {
		return new Integer(bitmap).hashCode() ^ new Double(scale).hashCode();
	}

	public String toString() {
		return toString(null);
	}

	/**
	 * @param bvNames The names of the basis vector (e1, e2, e3) are used when
	 * not available
	 */
	public String toString(String[] bvNames) {
		StringBuffer result = new StringBuffer();
		int i = 1;
		int b = bitmap;
		while (b != 0) {
			if ((b & 1) != 0) {
				if (result.length() > 0)
					result.append("^");
				if ((bvNames == null) || (i > bvNames.length)
						|| (bvNames[i - 1] == null))
					result.append("e" + i);
				else
					result.append(bvNames[i - 1]);
			}
			b >>= 1;
			i++;
		}

		return (result.length() == 0) ? new Double(scale).toString() : scale
				+ "*" + result.toString();
	}

	public BasisBlade copy() {
		return (BasisBlade) clone();
	}

	public Object clone() {
		return new BasisBlade(bitmap, scale);
	}

	private static ArrayList innerProductFilter(int ga, int gb, ArrayList R,
			int type) {
		ArrayList result = new ArrayList();
		for (int i = 0; i < R.size(); i++) {
			BasisBlade B = innerProductFilter(ga, gb, (BasisBlade) R.get(i),
					type);
			if (B.scale != 0.0)
				result.add(B);
		}
		return result;
	}

	// *!*HTML_TAG*!* inner_product_filter
	/**
	 * Applies the rules to turn a geometric product into an inner product
	 * @param ga Grade of argument 'a'
	 * @param gb Grade of argument 'b'
	 * @param r the basis blade to be filter
	 * @param type the type of inner product required:
	 * LEFT_CONTRACTION,RIGHT_CONTRACTION, HESTENES_INNER_PRODUCT or MODIFIED_HESTENES_INNER_PRODUCT
	 * @return Either a 0 basis blade, or 'r'
	 */
	private static BasisBlade innerProductFilter(int ga, int gb, BasisBlade r,
			int type) {
		switch (type) {
			case LEFT_CONTRACTION :
				if ((ga > gb) || (r.grade() != (gb - ga)))
					return new BasisBlade();
				else
					return r;
			case RIGHT_CONTRACTION :
				if ((ga < gb) || (r.grade() != (ga - gb)))
					return new BasisBlade();
				else
					return r;
			case HESTENES_INNER_PRODUCT :
				if ((ga == 0) || (gb == 0))
					return new BasisBlade();
				// drop through to MODIFIED_HESTENES_INNER_PRODUCT
			case MODIFIED_HESTENES_INNER_PRODUCT :
				if (Math.abs(ga - gb) == r.grade())
					return r;
				else
					return new BasisBlade();
			default :
				return null;
		}
	}

	static private class simplifyComperator implements java.util.Comparator {
		public int compare(Object o1, Object o2) {
			BasisBlade B1 = (BasisBlade) o1;
			BasisBlade B2 = (BasisBlade) o2;
			return ((B1.bitmap < B2.bitmap) ? -1 : ((B1.bitmap > B2.bitmap)
					? 1
					: Double.compare(B1.scale, B2.scale)));
		}
	}

	/** simplifies an arraylist (sum) of BasisBlades (destroys ordering of A!) */
	static protected ArrayList simplify(ArrayList A) {
		if (A.size() == 0)
			return A;

		java.util.Collections.sort(A, new simplifyComperator());
		ArrayList result = new ArrayList();
		BasisBlade current = ((BasisBlade) A.get(0)).copy();
		for (int i = 1; i < A.size(); i++) {
			BasisBlade b = (BasisBlade) A.get(i);
			if (b.bitmap == current.bitmap)
				current.scale += b.scale;
			else {
				if (current.scale != 0.0)
					result.add(current);
				current = b.copy();
			}
		}
		if (current.scale != 0.0)
			result.add(current);

		return result;
	}

	/**
	 * Rounds the scalar part of <code>this</code> to the nearest multiple X of <code>multipleOf</code>,
	 * if |X - what| <= epsilon. This is useful when eigenbasis is used to perform products in arbitrary
	 * metric, which leads to small roundof errors. You don't want to keep these roundof errors if your
	 * are computing a multiplication table.
	 *
	 * @returns a new basis blade if a change is required.
	 */
	public BasisBlade round(double multipleOf, double epsilon) {
		double a = DoubleU.round(scale, multipleOf, epsilon);
		if (a != scale)
			return new BasisBlade(bitmap, a);
		else
			return this;
	}

	// *!*HTML_TAG*!* storage
	/**
	 * This bitmap specifies what basis vectors are
	 * present in this basis blade
	 */
	public int bitmap;

	/**
	 * The scale of the basis blade is represented by this double
	 */
	public double scale;

} // end of class BasisBlade

// leave this comment such that when view in HTML, the final part of the class will display properly
