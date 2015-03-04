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
 * MeetJoin.java
 *
 * Created on October 12, 2005, 2:10 PM
 *
 * Copyright 2005-2007 Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.basis;

/**
 * Computes the meet and join.
 * Usage:
 * new MeetJoin(a, b).getMeet();
 * Or:
 * MeetJoin MJ = new MeetJoin(a, b);
 * Multivector M = MJ.getMeet();
 * Multivector J = MJ.getJoin();
 *
 * @author  fontijne
 */
public class MeetJoin implements InnerProductTypes {
    protected Multivector m_meet;
    protected Multivector m_join;


    public static void main(String args[]) {
		// test code for meet / join
		int dim = 7;

		for (int i = 0; i < 1000; i++) {
			Multivector a = Multivector.getRandomBlade(dim, (int)(0.6 * Math.random() * dim), 1.0);
			Multivector b = Multivector.getRandomBlade(dim, (int)(0.6 * Math.random() * dim), 1.0);

			if (Math.random() < 0.25) {
			// make a subspace of b:
			b = a.op(Multivector.getRandomBlade(dim, (int)(0.5 * Math.random() * dim + 0.99), 1.0));
			}

			if (Math.random() < 0.25) {
			// use basis elements for 'a'
			a = new Multivector(new BasisBlade((int)(Math.random() * ((1 << 7) - 1)), 1.0));
			}
			if (Math.random() < 0.25) {
			// use basis elements for 'b'
			b = new Multivector(new BasisBlade((int)(Math.random() * ((1 << 7) - 1)), 1.0));
			}

			if (Math.random() < 0.5) {
			// swap a & b:
			Multivector tmp = b;
			b = a;
			a = tmp;
			}

			if (a.isNull(1e-3)) continue;
			if (b.isNull(1e-3)) continue;

	//	    System.out.println("a = " + a + ",");
	//	    System.out.println("b = " + b + ",");

			MeetJoin MJ = null;
			try {
			MJ = new MeetJoin(a, b);
			} catch (Exception ex) {
			MJ = new MeetJoin(a, b);
			}

			Multivector T;
			try {
			if ((T = MJ.getMeet().ip(a, LEFT_CONTRACTION).compress(1e-7)).isNull())
				throw new java.lang.Exception("Meet is not subspace of 'a'");
			if ((T = MJ.getMeet().ip(b, LEFT_CONTRACTION).compress(1e-7)).isNull())
				throw new java.lang.Exception("Meet is not subspace of 'b'");

			if ((T = a.ip(MJ.getJoin(), LEFT_CONTRACTION).compress(1e-7)).isNull())
				throw new java.lang.Exception("'a' is not subspace of join");
			if ((T = b.ip(MJ.getJoin(), LEFT_CONTRACTION).compress(1e-7)).isNull())
				throw new java.lang.Exception("'b' is not subspace of join");
			} catch (Exception ex)  {
			MJ = new MeetJoin(a, b);
			}

			System.out.println("Loop " + i + " ");
		}

    }

    /** Creates a new instance of MeetJoin */
    public MeetJoin(Multivector a, Multivector b) {
		computeMeetJoin(a, b);
    }

    public Multivector getMeet() {
		return m_meet;
    }
    public Multivector meet() {
		return m_meet;
    }

    public Multivector getJoin() {
		return m_join;
    }
    public Multivector join() {
		return m_join;
    }

    /** @return the delta product of 'a' and 'b' */
    public static Multivector deltaProduct(Multivector a, Multivector b) {
		Multivector D = a.gp(b);
		D = D.compress();
		return D.extractGrade(D.topGradeIndex());
    }

    // *!*HTML_TAG*!* meet_join
    /**
     * <p>Both meet and join are computed simultaneously by this
     * algorithm by Ian Bell, which turns out to be somewhat
     * more efficient than computing the join and deriving the
     * meet from that.
     *
     * @return the meet and join of this with 'b'
     * The first array element returned is the meet (intersection),
     * the second array element is the join (union).
     */
    protected void computeMeetJoin(Multivector a, Multivector b) {

		double la = a.largestCoordinate();
		double lb = b.largestCoordinate();

		double smallEpsilon = 10e-9;
		double largeEpsilon = 10e-4;

		// step one: check for near-zero input
		if ((la < smallEpsilon) || (lb < smallEpsilon)) {
			m_meet = new Multivector();
			m_join = new Multivector();
		}

		// check grade of input
		int ga = a.grade();
		int gb = b.grade();
		if (ga < 0) { // if we are not handed homogeneous multivectors, take the grade parts with the largest norm
			a = a.largestGradePart();
			ga = a.grade();
		}
		if (gb < 0) {
			b = b.largestGradePart();
			gb = b.grade();
		}

		// normalize (approximately) and swap (optionally)
		Multivector ca, cb;
		if (ga <= gb) {
			// just normalize:
			ca = a.gp(1.0 / la);
			cb = b.gp(1.0 / lb);
		}
		else {
			// also swap:
			ca = b.gp(1.0 / lb);
			cb = a.gp(1.0 / la);

			int tempg = ga;
			ga = gb;
			gb = tempg;
		}

		// compute delta product & 'normalize'
		Multivector d, _d = deltaProduct(ca, cb);
		int gd = _d.grade();
		double ld = _d.largestCoordinate();
		d = _d.gp(1.0 / ld);

	//	System.out.println("Delta = " + d + ",");

		// if delta product is scalar, we're done:
		if (gd == 0) {
			// meet = 1
			m_meet = ca;
			// join = computed from meet
			m_join = ca.op(ca.versorInverse().ip(cb, LEFT_CONTRACTION));

			return;
		}

		// if grade of delta product is equal to ga + gb, we're done, too
		if (gd == ga + gb) {
			// a and b entirely disjoint
			// meet = 1
			m_meet = new Multivector(1.0);
			// join = computed from meet
			m_join = ca.op(cb);

			return;
		}


		// dimension of space we are working in:
		int dim = Math.max(ca.spaceDim(), cb.spaceDim());
		Multivector I = new Multivector(new BasisBlade((1 << dim)-1, 1.0));

		// init join to pseudoscalar
		Multivector j = I;
		int Ej = dim - ((ga + gb + gd) >> 1); // compute excessity of join

		// check join excessity
		if (Ej == 0) {
			// join computed
			m_join = j;
			// meet = computed from join
			m_meet = cb.ip(j.versorInverse(), LEFT_CONTRACTION).ip(ca, LEFT_CONTRACTION);
		}

		// init meet
		Multivector m = new Multivector(1.0);
		int Em = ((ga + gb - gd) >> 1); // compute excessity of meet

		// init s, the dual of the delta product:
		Multivector s = d.ip(I.versorInverse(), LEFT_CONTRACTION);

		// precompute inverse of ca
		Multivector cai = ca.versorInverse();


		// todo: maybe we can improve: search only the largest basis blade of the not-delta product?
		for (int i = 0; i < dim; i++) {
			// compute next factor 'c'
			Multivector c;

			{
			// project 'tmpc' onto 's' (the dual of the delta product)
			// project using MHIP because 's' may just be a scalar some times?
			Multivector tmpc = new Multivector(new BasisBlade(1 << i, 1.0)).ip(s, MHIP);
			c = tmpc.ip(s, MHIP); // no need to invert 's' here
			}

			// todo: then this naughty step could be avoided:

			// check if 'c' is an OK candidate:
			if (c.largestCoordinate() < largeEpsilon)
			continue;

			// compute projection, rejection of 'c' wrt to 'ca'
			Multivector cp, cr; // c projected, c rejected
			Multivector tmpc = c.ip(ca, LEFT_CONTRACTION);
			cp = tmpc.ip(cai, LEFT_CONTRACTION); // use correct inverse because otherwise cr != c - cp
			cr = c.subtract(cp);

			// if 'c' has enough of it in 'ca', then add to meet
			if (cp.largestCoordinate() > largeEpsilon) {
			m = m.op(cp);
			Em--; // reduce excessity of meet
			if (Em == 0) { // has the meet been computed???
				m_meet = m;
				// join = computed from meet
				m_join = ca.op(ca.versorInverse().ip(cb, LEFT_CONTRACTION));
				return;
			}
			}

			if (cr.largestCoordinate() > largeEpsilon) {
			j = cr.ip(j, LEFT_CONTRACTION);
			Ej--; // reduce excessity of join
			if (Ej == 0) { // has the join been computed???
				m_join = j;
				// meet = computed from join
				m_meet = cb.ip(j.versorInverse(), LEFT_CONTRACTION).ip(ca, LEFT_CONTRACTION);
				return;
			}
			}
		}
		throw new java.lang.ArithmeticException("meet & join algorithm failed!");
    }



}
