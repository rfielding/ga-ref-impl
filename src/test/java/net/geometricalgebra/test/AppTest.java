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
 * test.java
 *
 * Very simple test just to see if all .java files are present in distribution.
 *
 * Copyright Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */
package net.geometricalgebra.test;

import net.geometricalgebra.subspace.basis.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {
	public AppTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public static void testApp() {
		// setup conformal algebra:
		String[] bvNames = {"no", "e1", "e2", "e3", "ni"};
		double[][] m = new double[][]{{0.0, 0.0, 0.0, 0.0, -1.0},
				{0.0, 1.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0, 0.0},
				{0.0, 0.0, 0.0, 1.0, 0.0}, {-1.0, 0.0, 0.0, 0.0, 0.0}};

		Metric M = null;
		try {
			M = new Metric(m);
		} catch (Exception ex) {
		}

		// get basis vectors
		Multivector no = Multivector.getBasisVector(0);
		Multivector e1 = Multivector.getBasisVector(1);
		Multivector e2 = Multivector.getBasisVector(2);
		Multivector e3 = Multivector.getBasisVector(3);
		Multivector ni = Multivector.getBasisVector(4);

		{ // do simple test with outer product and addition (subspace.basis.Multivector.java)
			Multivector A = e1.add(e2.op(e3).op(e1));
			//			System.out.println("A = " + A.toString(bvNames));
			//			System.out.println(new MultivectorType(A));
		}

		{ // test meet & join (subspace.basis.MeetJoin.java)
			Multivector A = e2.op(e3).op(e1);
			Multivector B = e2.op(no);
			MeetJoin MJ = new MeetJoin(A, B);
		}

		{ // test factorization (subspace.basis.Util.java)
			Multivector A = e2.op(e3).op(e1);
			double[] scale = new double[1];
			Multivector[] F = Util.factorizeBlade(A, scale);
		}

		{ // InnerProductTypes.java
			int i = InnerProductTypes.LEFT_CONTRACTION;
		}
	}
}
