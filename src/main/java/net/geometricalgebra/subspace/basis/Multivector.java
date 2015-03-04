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
 * Multivector.java
 *
 * Created on October 10, 2005, 7:37 PM
 *
 * Copyright 2005-2007 Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.basis;

import java.util.*;
import net.geometricalgebra.subspace.util.*;

/**
 * This class implements a sample multivector class along with
 * some basic GA operations. Very low performance.
 *
 * <p>mutable :( Should have made it immutable . . .
 * @author  fontijne
 */
public class Multivector implements Cloneable, InnerProductTypes {
    public static void main(String[] args) {
	// setup conformal algebra:
	String[] bvNames = {"no", "e1", "e2", "e3", "ni"};
	double[][] m = new double[][]{
	    {0.0, 0.0, 0.0, 0.0, -1.0},
	    {0.0, 1.0, 0.0, 0.0, 0.0},
	    {0.0, 0.0, 1.0, 0.0, 0.0},
	    {0.0, 0.0, 0.0 ,1.0, 0.0},
	    {-1.0, 0.0, 0.0 , 0.0, 0.0}
	};

	Metric M = null;
	try {
	    M = new Metric(m);
	} catch (Exception ex) {}

	Multivector no = getBasisVector(0);
	Multivector ni = getBasisVector(4);
	Multivector e1 = getBasisVector(1);
	Multivector e2 = getBasisVector(2);
	Multivector e3 = getBasisVector(3);

	Multivector A = e1.add(e2.op(e3).op(e1));
	System.out.println("A = " + A);
	System.out.println(new MultivectorType(A));

	/*
	Multivector a = e1.add(e2.op(e3));
	Multivector ai1 = a.generalInverse(M);
	Multivector T1 = ai1.gp(a, M).subtract(1.0).compress(1e-7);
	Multivector T2 = a.gp(ai1, M).subtract(1.0).compress(1e-7);
	if (!T1.isNull()) {
	    System.out.println("More Wha 1 !!!");
	}
	if (!T2.isNull()) {
	    System.out.println("More Wha 2!!!");
	}*/


	/*int dim = 5;
	for (int i = 0; i < 1000; i++) {
	    if ((i % 10) == 0) System.out.println("i = " + i);
	    Multivector a = Multivector.getRandomVersor(dim, (int)(Math.random() * dim), 1.0);
	    Multivector ai1 = null;
	    Multivector ai2 = null;
	    try {
		ai1 = a.generalInverse(M);
	    } catch (Exception ex) {
	    }
	    try {
		ai2 = a.versorInverse(M);
	    } catch (Exception ex) {
	    }
	    if ((ai1 == null) ^ (ai2 == null)) {
		System.out.println("Wha!!!");
	    }
	    if (ai1 != null) {
		Multivector T1 = ai1.gp(a, M).subtract(1.0).compress(1e-7);
		Multivector T2 = a.gp(ai1, M).subtract(1.0).compress(1e-7);
		if (!T1.isNull()) {
		    Multivector X = a.gp(ai1, M).subtract(1.0).compress(1e-7);
		    System.out.println("More Wha 1 !!!");
		}
		if (!T2.isNull()) {
		    System.out.println("More Wha 2!!!");
		}
//		System.out.println("T1: " + T1.toString(bvNames));
//		System.out.println("T2: " + T2.toString(bvNames));

	    }

	}*/

	/*
	Multivector B = new Multivector(-1.45);
	Multivector R = B.cos(M);
	Multivector R2 = B.cosSeries(M, 24);

	System.out.println("B = " + B.toString(bvNames) + ",");
	System.out.println("R1 = " + R.toString(bvNames) + ",");
	System.out.println("R2 = " + R2.toString(bvNames) + ",");

	B = e1.op(e3).gp(1.33334);
	R = B.cos(M);
	R2 = B.cosSeries(M, 24);

	System.out.println("B = " + B.toString(bvNames) + ",");
	System.out.println("R1 = " + R.toString(bvNames) + ",");
	System.out.println("R2 = " + R2.toString(bvNames) + ",");
	 */

    }


    /** Creates a new instance of Multivector */
    public Multivector() {
		blades = new ArrayList();
    }

    /** Creates a new instance of Multivector */
    public Multivector(double s) {
		this(new BasisBlade(s));
    }

    /** do not modify 'B' for it is not copied */
    public Multivector(ArrayList B) {
		blades = B;
    }

    /** do not modify 'B' for it is not copied */
    public Multivector(BasisBlade B) {
		blades = ArrayListU.newArrayList(B);
    }

    public Multivector copy() {
		return (Multivector)clone();
    }

    public Object clone() {
		Multivector M = new Multivector((ArrayList)blades.clone());
		M.bladesSorted = this.bladesSorted;
		return M;
    }

    public boolean equals(Object O) {
		if (O instanceof Multivector) {
			Multivector B = (Multivector)O;
			Multivector zero = subtract(B);
			return (zero.blades.size() == 0);
		}
		else return false;
    }

    public int hashCode() {
		int hc = 0;
		for (int i = 0; i < blades.size(); i++)
			hc ^= blades.get(i).hashCode();
		return hc;
    }

    public String toString() {
		return toString(null);
    }

    /**
     * @param bvNames The names of the basis vector (e1, e2, e3) are used when
     * not available
     */
    public String toString(String[] bvNames) {
		if (blades.size() == 0) return "0";
		else {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < blades.size(); i++) {
				BasisBlade b = (BasisBlade)blades.get(i);
				String S = b.toString(bvNames);
				if (i == 0) result.append(S);
				else if (S.charAt(0) == '-') {
					result.append(" - ");
					result.append(S.substring(1));
				}
				else {
					result.append(" + ");
					result.append(S);
				}
			}
			return result.toString();
		}
    }

    /** @return basis vector 'idx' range [0 ... dim)*/
    public static Multivector getBasisVector(int idx) {
		return new Multivector(new BasisBlade(1 << idx));
    }

    /** @return 'dim'-dimensional random vector with coordinates in range [-scale, scale] */
    public static Multivector getRandomVector(int dim, double scale) {
		ArrayList result = new ArrayList(dim);
		for (int i = 0; i < dim; i++)
			result.add(new BasisBlade(1 << i, 2 * scale * (Math.random() - 0.5)));
		return new Multivector(result);
    }

    /**
     * @return 'dim'-dimensional random blade with coordinates in range [-scale, scale]
     */
    public static Multivector getRandomBlade(int dim, int grade, double scale) {
		Multivector result = new Multivector(2 * scale * (Math.random() - 0.5));
		for (int i = 1; i <= grade; i++)
			result = result.op(getRandomVector(dim, scale));
		return result;
    }

    /**
     * @return 'dim'-dimensional random blade with coordinates in range [-scale, scale]
     * @param metric can either be null, Metric or double[]
     */
    public static Multivector getRandomVersor(int dim, int grade, double scale) {
		return getRandomVersor(dim, grade, scale, null);
    }

    /**
     * @return 'dim'-dimensional random blade with coordinates in range [-scale, scale]
     * @param metric can either be null, Metric or double[]
     */
    public static Multivector getRandomVersor(int dim, int grade, double scale, Object metric) {
		Multivector result = new Multivector(2 * scale * (Math.random() - 0.5));
		for (int i = 1; i <= grade; i++)
			result = result._gp(getRandomVector(dim, scale), metric);
		return result;
    }

    /** @return geometric product of this with a scalar */
    public Multivector gp(double a) {
		if (a == 0.0) return new Multivector();
		else {
			ArrayList result = new ArrayList(blades.size());
			for (int i = 0; i < blades.size(); i++) {
			BasisBlade b = (BasisBlade)blades.get(i);
			result.add(new BasisBlade(b.bitmap, b.scale * a));
			}
			return new Multivector(result);
		}
    }

    // *!*HTML_TAG*!* gp
    /** @return geometric product of this with a 'x' */
    public Multivector gp(Multivector x) {
		ArrayList result = new ArrayList(blades.size() * x.blades.size());

		// loop over basis blade of 'this'
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade B1 = (BasisBlade)blades.get(i);

			// loop over basis blade of 'x'
			for (int j = 0; j < x.blades.size(); j++) {
			BasisBlade B2 = (BasisBlade)x.blades.get(j);
			result.add(BasisBlade.gp(B1, B2));
			}
		}
		return new Multivector(simplify(result));
    }

    /** @return geometric product of this with a 'x' using metric 'M' */
    public Multivector gp(Multivector x, Metric M) {
		ArrayList result = new ArrayList(blades.size() * x.blades.size());
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade B1 = (BasisBlade)blades.get(i);
			for (int j = 0; j < x.blades.size(); j++) {
			BasisBlade B2 = (BasisBlade)x.blades.get(j);
			result.addAll(BasisBlade.gp(B1, B2, M));
			}
		}
		return new Multivector(simplify(result));
    }

    /** @return geometric product of this with a 'x' using metric 'm' */
    public Multivector gp(Multivector x, double[] m) {
		ArrayList result = new ArrayList(blades.size() * x.blades.size());
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade B1 = (BasisBlade)blades.get(i);
			for (int j = 0; j < x.blades.size(); j++) {
			BasisBlade B2 = (BasisBlade)x.blades.get(j);
			result.add(BasisBlade.gp(B1, B2, m));
			}
		}
		return new Multivector(simplify(result));
    }

    // *!*HTML_TAG*!* op
    /** @return outer product of this with 'x' */
    public Multivector op(Multivector x) {
		ArrayList result = new ArrayList(blades.size() * x.blades.size());
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade B1 = (BasisBlade)blades.get(i);
			for (int j = 0; j < x.blades.size(); j++) {
			BasisBlade B2 = (BasisBlade)x.blades.get(j);
			result.add(BasisBlade.op(B1, B2));
			}
		}
		return new Multivector(simplify(result));
    }

    /** @return inner product of this with a 'x'
     * @param type gives the type of inner product:
     * LEFT_CONTRACTION,
     * RIGHT_CONTRACTION,
     * HESTENES_INNER_PRODUCT or
     * MODIFIED_HESTENES_INNER_PRODUCT.
     */
    public Multivector ip(Multivector x, int type) {
		ArrayList result = new ArrayList(blades.size() * x.blades.size());
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade B1 = (BasisBlade)blades.get(i);
			for (int j = 0; j < x.blades.size(); j++) {
			BasisBlade B2 = (BasisBlade)x.blades.get(j);
			result.add(BasisBlade.ip(B1, B2, type));
			}
		}
		return new Multivector(simplify(result));
    }

    // *!*HTML_TAG*!* ip
    /** @return inner product of this with a 'x' using metric 'M'
     * @param type gives the type of inner product:
     * LEFT_CONTRACTION,
     * RIGHT_CONTRACTION,
     * HESTENES_INNER_PRODUCT or
     * MODIFIED_HESTENES_INNER_PRODUCT.
     */
    public Multivector ip(Multivector x, Metric M, int type) {
		ArrayList result = new ArrayList(blades.size() * x.blades.size());
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade B1 = (BasisBlade)blades.get(i);
			for (int j = 0; j < x.blades.size(); j++) {
			BasisBlade B2 = (BasisBlade)x.blades.get(j);
			result.addAll(BasisBlade.ip(B1, B2, M, type));
			}
		}
		return new Multivector(simplify(result));
    }

    /** @return inner product of this with a 'x' using metric 'm'
     * @param type gives the type of inner product:
     * LEFT_CONTRACTION,
     * RIGHT_CONTRACTION,
     * HESTENES_INNER_PRODUCT or
     * MODIFIED_HESTENES_INNER_PRODUCT.
     */
    public Multivector ip(Multivector x, double[] m, int type) {
		ArrayList result = new ArrayList(blades.size() * x.blades.size());
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade B1 = (BasisBlade)blades.get(i);
			for (int j = 0; j < x.blades.size(); j++) {
			BasisBlade B2 = (BasisBlade)x.blades.get(j);
			result.add(BasisBlade.ip(B1, B2, m, type));
			}
		}
		return new Multivector(simplify(result));
    }

    /** @return scalar product of this with a 'x' */
    public double scalarProduct(Multivector x) {
		return ip(x, LEFT_CONTRACTION).scalarPart();
    }

    /** @return scalar product of this with a 'x' using metric 'm' */
    public double scalarProduct(Multivector x, double[] m) {
		return ip(x, m, LEFT_CONTRACTION).scalarPart();
    }

    /** @return scalar product of this with a 'x' using metric 'M' */
    public double scalarProduct(Multivector x, Metric M) {
		return ip(x, M, LEFT_CONTRACTION).scalarPart();
    }

    /** shortcut to scalarProduct(...) */
    public double scp(Multivector x) {
		return scalarProduct(x);
    }

    /** shortcut to scalarProduct(...) */
    public double scp(Multivector x, double[] m) {
		return scalarProduct(x, m);
    }

    /** shortcut to scalarProduct(...) */
    public double scp(Multivector x, Metric M) {
		return scalarProduct(x, M);
    }

    // *!*HTML_TAG*!* add
    /** @return sum of this with scalar 'a' */
    public Multivector add(double a) {
		ArrayList result = new ArrayList(blades.size() + 1);

		result.addAll(blades);
		result.add(new BasisBlade(a));

		return new Multivector(simplify(cloneArrayElements(result)));
    }

    /** @return sum of this with 'x' */
    public Multivector add(Multivector x) {
		ArrayList result = new ArrayList(blades.size() + x.blades.size());

		result.addAll(blades);
		result.addAll(x.blades);

		return new Multivector(simplify(cloneArrayElements(result)));
    }

    /** @return this - scalar 'a' */
    public Multivector subtract(double a) {
		return add(-a);
    }

    // *!*HTML_TAG*!* substract
    /** @return this - 'x' */
    public Multivector subtract(Multivector x) {
		ArrayList result = new ArrayList(blades.size() + x.blades.size());

		result.addAll(blades);
		result.addAll(x.gp(-1.0).blades);

		return new Multivector(simplify(cloneArrayElements(result)));
    }


    /** @return exponential of this */
    public Multivector exp() {
		return exp((Object)null, 12);
    }
    /** @return exponential of this under metric 'M' */
    public Multivector exp(Metric M) {
		return exp((Object)M, 12);
    }
    /** @return exponential of this under metric 'm' */
    public Multivector exp(double[] m) {
		return exp((Object)m, 12);
    }

    // *!*HTML_TAG*!* exp
    /** evaluates exp(this) using special cases if possible, using series otherwise */
    protected Multivector exp(Object M, int order) {
		// check out this^2 for special cases
		Multivector A2 = this._gp(this, M).compress();
		if (A2.isNull(1e-8)) {
			// special case A^2 = 0
			return this.add(1);
		}
		else if (A2.isScalar()) {
			double a2 = A2.scalarPart();
			// special case A^2 = +-alpha^2
			if (a2 < 0) {
				double alpha = Math.sqrt(-a2);
				return gp(Math.sin(alpha) / alpha).add(Math.cos(alpha));
			}
			//hey: todo what if a2 == 0?
			else {
				double alpha = Math.sqrt(a2);
				return gp(MathU.sinh(alpha) / alpha).add(MathU.cosh(alpha));
			}
		}
		else return expSeries(M, order);
    }

    // *!*HTML_TAG*!* expSeries
    /** Evaluates exp using series . . .  (== SLOW & INPRECISE!) */
    protected Multivector expSeries(Object M, int order) {
		// first scale by power of 2 so that its norm is ~ 1
		long scale=1; {
			double max = this.norm_e();
			if (max > 1.0) scale <<= 1;
			while (max > 1.0) {
				max = max / 2;
				scale <<= 1;
			}
		}

		Multivector scaled = this.gp(1.0 / scale);

		// taylor approximation
		Multivector result = new Multivector(1.0); {
			Multivector tmp = new Multivector(1.0);

			for (int i = 1; i < order; i++) {
				tmp = tmp._gp(scaled.gp(1.0 / i), M);
				result = result.add(tmp);
			}
		}

		// undo scaling
		while (scale > 1) {
			result = result._gp(result, M);
			scale >>>= 1;
		}

		return result;
    }


    /** @return sin of this */
    public Multivector sin() {
		return sin((Object)null, 12);
    }
    /** @return sin of this under metric 'M' */
    public Multivector sin(Metric M) {
		return sin((Object)M, 12);
    }
    /** @return sin of this under metric 'm' */
    public Multivector sin(double[] m) {
		return sin((Object)m, 12);
    }

    // *!*HTML_TAG*!* sin
    protected Multivector sin(Object M, int order) {
		// check out this^2 for special cases
		Multivector A2 = this._gp(this, M).compress();
		if (A2.isNull(1e-8)) {
			// special case A^2 = 0
			return this;
		}
		else if (A2.isScalar()) {
			double a2 = A2.scalarPart();
			// special case A^2 = +-alpha^2
			if (a2 < 0) {
				double alpha = Math.sqrt(-a2);
				return gp(MathU.sinh(alpha) / alpha);
			}
			//hey: todo what if a2 == 0?
			else {
				double alpha = Math.sqrt(a2);
				return gp(Math.sin(alpha) / alpha);
			}
		}
		else return sinSeries(M, order);
    }

    // *!*HTML_TAG*!* sinSeries
    /** Evaluates sin using series . . .  (== SLOW & INPRECISE!) */
    protected Multivector sinSeries(Object M, int order) {
		Multivector scaled = this;

		// taylor approximation
		Multivector result = scaled;
		{
			Multivector tmp = scaled;

			int sign = -1;
			for (int i = 2; i < order; i ++) {
				tmp = tmp._gp(scaled.gp(1.0 / i), M);
				if ((i & 1) != 0) {// only the odd part of the series
					result = result.add(tmp.gp((double)sign));
					sign *= -1;
				}
			}
		}

		return result;
    }

    /** @return cos of this */
    public Multivector cos() {
		return cos((Object)null, 12);
    }
    /** @return cos of this under metric 'M' */
    public Multivector cos(Metric M) {
		return cos((Object)M, 12);
    }
    /** @return cos of this under metric 'm' */
    public Multivector cos(double[] m) {
		return cos((Object)m, 12);
    }

    // *!*HTML_TAG*!* cos
    protected Multivector cos(Object M, int order) {
		// check out this^2 for special cases
		Multivector A2 = this._gp(this, M).compress();
		if (A2.isNull(1e-8)) {
			// special case A^2 = 0
			return new Multivector(1);
		}
		else if (A2.isScalar()) {
			double a2 = A2.scalarPart();
			// special case A^2 = +-alpha^2
			if (a2 < 0) {
				double alpha = Math.sqrt(-a2);
				return new Multivector(MathU.cosh(alpha));
			}
			//hey: todo what if a2 == 0?
			else {
				double alpha = Math.sqrt(a2);
				return new Multivector(Math.cos(alpha));
			}
		}
		else return cosSeries(M, order);
    }

    // *!*HTML_TAG*!* cosSeries
    /** Evaluates cos using series . . .  (== SLOW & INPRECISE!) */
    protected Multivector cosSeries(Object M, int order) {
		Multivector scaled = this;

		// taylor approximation
		Multivector result = new Multivector(1.0); {
			Multivector tmp = scaled;

			int sign = -1;
			for (int i = 2; i < order; i ++) {
				tmp = tmp._gp(scaled.gp(1.0 / i), M);
				if ((i & 1) == 0) {// only the even part of the series
					result = result.add(tmp.gp((double)sign));
					sign *= -1;
				}
			}
		}

		return result;
    }




    /**
     * Can throw java.lang.ArithmeticException if multivector is null
     * @return unit under Euclidean norm
     */
    public Multivector unit_e() {
		return unit_r();
    }

    public double norm_e() {
		double s = scp(reverse());
		if (s < 0.0) return 0.0; // avoid FP round off causing negative 's'
		else return Math.sqrt(s);
    }

    public double norm_e2() {
		double s = scp(reverse());
		if (s < 0.0) return 0.0; // avoid FP round off causing negative 's'
		return s;
    }

    /**
     * Can throw java.lang.ArithmeticException if multivector is null
     * @return unit under 'reverse' norm (this / sqrt(abs(this.reverse(this))))
     */
    public Multivector unit_r() {
		double s = scp(reverse());
		if (s == 0.0) throw new java.lang.ArithmeticException("null multivector");
		else return this.gp(1 / Math.sqrt(Math.abs(s)));
    }

    /**
     * Can throw java.lang.ArithmeticException if multivector is null
     * @return unit under 'reverse' norm (this / sqrt(abs(this.reverse(this))))
     */
    public Multivector unit_r(double[] m) {
		double s = scp(reverse(), m);
		if (s == 0.0) throw new java.lang.ArithmeticException("null multivector");
		else return this.gp(1 / Math.sqrt(Math.abs(s)));
    }

    /**
     * Can throw java.lang.ArithmeticException if multivector is null
     * @return unit under 'reverse' norm (this / sqrt(abs(this.reverse(this))))
     */
    public Multivector unit_r(Metric M) {
		double s = scp(reverse(), M);
		if (s == 0.0) throw new java.lang.ArithmeticException("null multivector");
		else return this.gp(1 / Math.sqrt(Math.abs(s)));
    }


    /** @return true if this is really 0.0 */
    public boolean isNull() {
		simplify();
		return (blades.size() == 0);
    }

    /** @return true if norm_e2 < epsilon * epsilon*/
    public boolean isNull(double epsilon) {
		double s = norm_e2();
		return (s < epsilon * epsilon);
    }

    /** @return true is this is a scalar (0.0 is also a scalar) */
    public boolean isScalar() {
		if (isNull()) return true;
		else if (blades.size() == 1) {
			BasisBlade b = (BasisBlade)blades.get(0);
			return (b.bitmap == 0);
		}
		else return false;
    }

    // *!*HTML_TAG*!* reverse
    /** @return reverse of this */
    public Multivector reverse() {
		ArrayList result = new ArrayList(blades.size());

		// loop over all basis lades, reverse them, add to result
		for (int i = 0; i < blades.size(); i++)
			result.add(((BasisBlade)blades.get(i)).reverse());

		return new Multivector(result);
    }

    // *!*HTML_TAG*!* grade_inversion
    /** @return grade inversion of this */
    public Multivector gradeInversion() {
		ArrayList result = new ArrayList(blades.size());

		for (int i = 0; i < blades.size(); i++)
			result.add(((BasisBlade)blades.get(i)).gradeInversion());

		return new Multivector(result);
    }

    // *!*HTML_TAG*!* clifford_conjugate
    /** @return clifford conjugate of this */
    public Multivector cliffordConjugate() {
		ArrayList result = new ArrayList(blades.size());

		for (int i = 0; i < blades.size(); i++)
			result.add(((BasisBlade)blades.get(i)).cliffordConjugate());

		return new Multivector(result);
    }

    /**
     * Extracts grade 'g' from this multivector.
     * @return a new multivector of grade 'g'
     */
    public Multivector extractGrade(int g) {
		return extractGrade(new int[]{g});
    }

    /**
     * Extracts grade(s) 'G' from this multivector.
     * @return a new multivector of grade(s) 'G'
     */
    public Multivector extractGrade(int[] G) {
		// what is the maximum grade to be extracted?
		int maxG = 0;
		for (int i = 0; i < G.length; i++)
			if (G[i] > maxG) maxG = G[i];

		// create boolean array of what grade to keep
		boolean[] keep = new boolean[maxG + 1];
		for (int i = 0; i < G.length; i++)
			keep[G[i]] = true;

		// extract the grade, store in result:
		ArrayList result = new ArrayList();
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade b = (BasisBlade)blades.get(i);
			int g = b.grade();
			if (g > maxG) continue;
			else if (keep[g]) result.add(b.copy());
		}

		return new Multivector(result);
    }

    public Multivector dual(int dim) {
		Multivector I = new Multivector(new BasisBlade((1 << dim)-1, 1.0));
		return ip(I.versorInverse(), LEFT_CONTRACTION);
    }

    public Multivector dual(Metric M) {
		Multivector I = new Multivector(new BasisBlade((1 << M.getEigenMetric().length)-1, 1.0));
		return ip(I.versorInverse(), M, LEFT_CONTRACTION);
    }

    public Multivector dual(double[] m) {
		Multivector I = new Multivector(new BasisBlade((1 << m.length)-1, 1.0));
		return ip(I.versorInverse(), m, LEFT_CONTRACTION);
    }

    /** @return scalar part of 'this */
    public double scalarPart() {
		double s = 0.0;
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade b = (BasisBlade)blades.get(i);
			if (b.bitmap == 0) s += b.scale;
		}
		return s;
    }

    // *!*HTML_TAG*!* versor_inverse
    /**
     * Can throw java.lang.ArithmeticException if versor is not invertible
     * @return inverse of this (assuming it is a versor, no check is made!)
     */
    public Multivector versorInverse() {
		Multivector R = reverse();
		double s = scp(R);
		if (s == 0.0) throw new java.lang.ArithmeticException("non-invertible multivector");
		return R.gp(1.0 / s);
    }

    /**
     * Can throw java.lang.ArithmeticException if versor is not invertible
     * @return inverse of this (assuming it is a versor, no check is made!)
     */
    public Multivector versorInverse(Metric M) {
		Multivector R = reverse();
		double s = scp(R, M);
		if (s == 0.0) throw new java.lang.ArithmeticException("non-invertible multivector");
		return R.gp(1.0 / s);
    }

    /**
     * Can throw java.lang.ArithmeticException if versor is not invertible
     * @return inverse of this (assuming it is a versor, no check is made!)
     */
    public Multivector versorInverse(double[] m) {
		Multivector R = reverse();
		double s = scp(R, m);
		if (s == 0.0) throw new java.lang.ArithmeticException("non-invertible multivector");
		return R.gp(1.0 / s);
    }

    // *!*HTML_TAG*!* general_inverse
    /**
     * Can throw java.lang.ArithmeticException if blade is not invertible
     * @return inverse of arbitrary multivector.
     *
     */
    public Multivector generalInverse(Object metric) {
		int dim = spaceDim();

		cern.colt.matrix.DoubleMatrix2D M =
		cern.colt.matrix.DoubleFactory2D.dense.make(1 << dim, 1 << dim);

		// create all unit basis blades for 'dim'
		BasisBlade[] B = new BasisBlade[1 << dim];
		for (int i = 0; i < (1 << dim); i++)
			B[i] = new BasisBlade(i);


		// construct a matrix 'M' such that matrix multiplication of 'M' with
		// the coordinates of another multivector 'x' (stored in a vector)
		// would result in the geometric product of 'M' and 'x'
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade b = (BasisBlade)blades.get(i);
			for (int j = 0; j < (1 << dim); j++) {
			if (metric == null)
				addToMatrix(M, b, B[j], BasisBlade.gp(b, B[j]));
			else if (metric instanceof Metric)
				addToMatrix(M, b, B[j], BasisBlade.gp(b, B[j], (Metric)metric));
			else if (metric instanceof double[])
				addToMatrix(M, b, B[j], BasisBlade.gp(b, B[j], (double[])metric));
			}
		}

		// try to invert matrix (can fail, then we throw an exception)
		cern.colt.matrix.DoubleMatrix2D IM = null;
		try {
			IM = cern.colt.matrix.linalg.Algebra.DEFAULT.inverse(M);
		} catch (Exception ex) {
			throw new java.lang.ArithmeticException("Multivector is not invertible");
		}

		// reconstruct multivector from first column of matrix
		ArrayList result = new ArrayList();
		for (int j = 0; j < (1 << dim); j++) {
			double v = IM.getQuick(j, 0);
			if (v != 0.0) {
			B[j].scale = v;
			result.add(B[j]);
			}
		}
		return new Multivector(result);
    }

    protected static void addToMatrix(cern.colt.matrix.DoubleMatrix2D M,
		BasisBlade alpha, BasisBlade beta, BasisBlade gamma) {
		// add gamma.scale to matrix entry M[gamma.bitmap, beta.bitmap]
		double v = M.getQuick(gamma.bitmap, beta.bitmap);
		M.setQuick(gamma.bitmap, beta.bitmap, v + gamma.scale);
    }

    protected static void addToMatrix(cern.colt.matrix.DoubleMatrix2D M,
		BasisBlade alpha, BasisBlade beta, ArrayList gamma) {
		for (int i = 0; i < gamma.size(); i++)
			addToMatrix(M, alpha, beta, (BasisBlade)gamma.get(i));
    }

    /** @return simplification of this multivector (the same Multivector, but blades array can be changed) */
    public Multivector simplify() {
		simplify(blades);
		return this;
    }


    /** @return abs(largest coordinate) of 'this' */
    public double largestCoordinate() {
		simplify();
		double lc = 0.0;
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade b = (BasisBlade)blades.get(i);
			lc = Math.max(Math.abs(b.scale), lc);
		}
		return lc;
    }

    /** @return abs(largest BasisBlade) of 'this' */
    public BasisBlade largestBasisBlade() {
		simplify();
		BasisBlade bestBlade = null;
		double bestScale = -1.0;
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade b = (BasisBlade)blades.get(i);
			if (Math.abs(b.scale) > bestScale) {
			bestScale = Math.abs(b.scale);
			bestBlade = b;
			}
		}
		return bestBlade;
    }

    /** @return the grade of this if homogeneous, -1 otherwise.
     * 0 is return for null Multivectors.
     */
    public int grade() {
		int g = -1;
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade b = (BasisBlade)blades.get(i);
			if (g < 0) g = b.grade();
			else if (g != b.grade()) return -1;
		}
		return (g < 0) ? 0 : g;
    }


    /** @return bitmap of grades that are in use in 'this'*/
    public int gradeUsage() {
		int gu = 0;
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade b = (BasisBlade)blades.get(i);
			gu |= 1 << b.grade();
		}
		return gu;
    }

    /** @return index of highest grade in use in 'this'*/
    public int topGradeIndex() {
		int maxG = 0;
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade b = (BasisBlade)blades.get(i);
			maxG = Math.max(b.grade(), maxG);
		}
		return maxG;
    }

    /** @return the largest grade part of this */
    public Multivector largestGradePart() {
		simplify();

		Multivector maxGP = null;
		double maxNorm = -1.0;
		int gu = gradeUsage();
		for (int i = 0; i <= topGradeIndex(); i++) {
			if ((gu & (1 << i)) == 0) continue;
			Multivector GP = extractGrade(i);
			double n = GP.norm_e();
			if (n > maxNorm) {
			maxGP = GP;
			maxNorm = n;
			}
		}

		return (maxGP == null) ? new Multivector() : maxGP;
    }

    /** @return dimension of space this blade (apparently) lives in */
    protected int spaceDim() {
		int maxD = 0;
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade b = (BasisBlade)blades.get(i);
			maxD = Math.max(Bits.highestOneBit(b.bitmap), maxD);
		}
		return maxD+1;
    }

    /**
     * Currently removes all basis blades with |scale| less than epsilon
     *
     * Old version did this:
     * Removes basis blades with whose |scale| is less than <code>epsilon * maxMag</code> where
     * maxMag is the |scale| of the largest basis blade.
     *
     * @return 'Compressed' version of this (the same Multivector, but blades array can be changed)
     */
    public Multivector compress(double epsilon) {
		simplify();

		// find maximum magnitude:
		double maxMag = 0.0;
		for (int i = 0; i < blades.size(); i++) {
			BasisBlade b = (BasisBlade)blades.get(i);
			maxMag = Math.max(Math.abs(b.scale), maxMag);
		}
		if (maxMag == 0.0) {
			blades.clear();
		}
		else {
			// premultiply maxMag
			maxMag = epsilon; // used to read *=

			// remove basis blades with too low scale
			for (Iterator I = blades.iterator(); I.hasNext(); ) {
			BasisBlade b = (BasisBlade)I.next();
			if (Math.abs(b.scale) < maxMag)
				I.remove();
			}
		}
		return this;
    }


    /** shortcut to compress(1e-13) */
    public Multivector compress() {
		return compress(1e-13);
    }
    
    public ArrayList getBlades() {
        return (ArrayList)blades.clone();
    }

    /** sorts by bitmap only */
    private static class BladesComperator implements java.util.Comparator {
		public int compare(Object o1, Object o2) {
			BasisBlade b1 = (BasisBlade)o1;
			BasisBlade b2 = (BasisBlade)o2;
			if (b1.bitmap < b2.bitmap) return -1;
			else if (b1.bitmap > b2.bitmap) return 1;
			else return 0;
		}
    }

    // *!*HTML_TAG*!* simplify
    /** simplifies list of basis blades; List is modified in the process */
    protected static ArrayList simplify(ArrayList L) {
		Collections.sort(L, new BladesComperator()); // sort by bitmap only
		BasisBlade prevBlade = null;
		boolean removeNullBlades = false;
		for (Iterator I = L.iterator(); I.hasNext(); ) {
			BasisBlade curBlade = (BasisBlade)I.next();
			if (curBlade.scale == 0.0) {
			I.remove();
			prevBlade = null;
			}
			else if ((prevBlade != null) && (prevBlade.bitmap == curBlade.bitmap)) {
			prevBlade.scale += curBlade.scale;
			I.remove();
			}
			else {
			if ((prevBlade != null) && (prevBlade.scale == 0.0))
				removeNullBlades = true;
			prevBlade = curBlade;
			}
		}

		if (removeNullBlades) {
			for (Iterator I = L.iterator(); I.hasNext(); ) {
			BasisBlade curBlade = (BasisBlade)I.next();
			if (curBlade.scale == 0.0)
				I.remove();
			}
		}

		return L;
    }

    /** replaces all BasisBlades found in array L */
    protected static ArrayList cloneArrayElements(ArrayList L) {
		for (int i = 0; i < L.size(); i++) {
			L.set(i, ((BasisBlade)L.get(i)).clone());
		}
		return L;
    }

    /** sorts the blade in 'blades' based on bitmap only */
    protected void sortBlades() {
		if (bladesSorted) return;
		else {
			Collections.sort(blades, new BladesComperator());
			bladesSorted = true;
		}
    }

    /** For internal use; M can be null, Metric or double[] */
    protected Multivector _gp(Multivector x, Object M) {
		if (M == null) return gp(x);
		else if (M instanceof Metric) return gp(x, (Metric)M);
		else return gp(x, (double[])M);
    }

    /** For internal use; M can be null, Metric or double[] */
    protected double _scp(Multivector x, Object M) {
		if (M == null) return scp(x);
		else if (M instanceof Metric) return scp(x, (Metric)M);
		else return scp(x, (double[])M);
    }

    /** For internal use; M can be null, Metric or double[] */
    protected Multivector _versorInverse(Object M) {
		if (M == null) return versorInverse();
		else if (M instanceof Metric) return versorInverse((Metric)M);
		else return versorInverse((double[])M);
    }
    

    // *!*HTML_TAG*!* storage
    /** list of basis blades */
    protected ArrayList blades = null;

    /** when true, the blades have been sorted on bitmap */
    protected boolean bladesSorted = false;

} // end of class Multivector







































// leave this comment such that when viewed in HTML, the final part of the class will display properly
