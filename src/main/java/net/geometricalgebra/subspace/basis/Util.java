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
 * Util.java
 *
 * Created on March 1, 2005, 9:09 PM
 *
 * Copyright 2005-2007 Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.basis;

import java.util.*;
import cern.colt.matrix.*;
import cern.colt.matrix.linalg.*;
import net.geometricalgebra.subspace.util.*;

/**
 *
 * @author  fontijne
 */
public class Util implements InnerProductTypes {


//    static String[] bvNames = {"no", "e1", "e2", "e3", "e4", "ni"};
    static String[] bvNames = {"no", "e1", "e2", "e3", "ni"};
    public static void main(String[] args) {
		// setup conformal algebra:
		//String[] bvNames = {"no", "e1", "e2", "e3", "e4", "ni"};
		double[][] m = new double[][]{
			{0.0, 0.0, 0.0, 0.0, -1.0},
			{0.0, 1.0, 0.0, 0.0, 0.0},
			{0.0, 0.0, 1.0, 0.0, 0.0},
			{0.0, 0.0, 0.0, 1.0, 0.0},
			{-1.0, 0.0, 0.0 , 0.0, 0.0}
		};

		Metric M = null;
		try {
			M = new Metric(m);
		} catch (Exception ex) {}


		Multivector no = Multivector.getBasisVector(0);
		Multivector e1 = Multivector.getBasisVector(1);
		Multivector e2 = Multivector.getBasisVector(2);
		Multivector e3 = Multivector.getBasisVector(3);
	//	Multivector e4 = Multivector.getBasisVector(4);
		Multivector ni = Multivector.getBasisVector(4);

		long t = System.currentTimeMillis();
		// test code for factorization
		int dim = 8;
		double[] scale = new double[1];
                ArrayList[] SSS = new ArrayList[dim+1];
                for (int i = 0; i <= dim; i++)
                    SSS[i] = new ArrayList();
                
		for (int i = 0; i < 1; i++) {
			Multivector B = Multivector.getRandomBlade(dim, (int)(Math.random() * (dim + 0.49)), 1.0);
                        
                        ArrayList BL = new ArrayList();
                        BL.add(new BasisBlade(30, -0.662244));
                        BL.add(new BasisBlade(29, -0.391495));
                        BL.add(new BasisBlade(27, -0.430912));
                        BL.add(new BasisBlade(23, 0.218277));
                        BL.add(new BasisBlade(15, -0.213881));
                        B = new Multivector(BL);
                        
			Multivector[] f = factorizeBlade(B, scale);
			Multivector R = new Multivector(scale[0]);
			for (int g = 0; g < f.length; g++)
			R = R.op(f[g]);
                        
                        Multivector[] fAltFast = factorizeBladeAltFast(B, scale);
			Multivector RaltFast = new Multivector(scale[0]);
			for (int g = 0; g < fAltFast.length; g++) {
//                            System.out.println("f: " + fAltFast[g]);
                            RaltFast = RaltFast.op(fAltFast[g]);
                        }
                        
                        B = B.unit_e();
                        R = R.unit_e();
                        RaltFast = RaltFast.unit_e();
                        
                        int k = B.grade();
                        
                        double checkScale = R.gp(RaltFast.versorInverse()).scalarPart();
                        if (checkScale < 0)
                            System.out.println("Whaaaaa!\n");
                            
                            
                        SSS[B.grade()].add(
                            (checkScale < 0) ? "-" : "+");


                         System.out.println("B  = " + B + ",");
                        System.out.println("R  = " + R + ",");
                        System.out.println("Ra = " + RaltFast + ",");
/*                        
			if ((i % 100) == 0)
			System.out.println("I: " + i);*/

		}
                for (int i = 0; i <= dim; i++)
                   System.out.println("Dim " + i + " = " + SSS[i].toString());
		System.out.println("Done!" + (System.currentTimeMillis() - t));
		 
                
                
		//Multivector V = e1.add(e2).gp(no.add(ni)).gp(e3);
		//V = e1.add(e2).gp(no.add(ni));
		/*for (int i = 0; i < 100; i++) {
			int dim = M.getEigenMetric().length;
			Multivector V = Multivector.getRandomVersor(dim, (int)(Math.random() * (dim + 0.49)), 1.0, M);
			//System.out.println("V = " + V.toString(bvNames));
			factorizeVersor(V, M);
		}*/


		 /*
		double alpha = 0.1 * Math.PI / 2;
		double[][] m = new double[][]{
			new double[]{Math.cos(alpha), Math.sin(alpha), 0.0},
			new double[]{-Math.sin(alpha), Math.cos(alpha), 0.0},
			new double[]{0.0, 0.0, 1.0}
		};*/
		/*
		double[][] m = new double[][]{
			new double[]{1.0, 0.0, 0.0},
			new double[]{0.0, 1.0, 0.0},
			new double[]{0.0, 0.0, 1.0}
		};
		 */
		/*
		Multivector R = rotationMatrixToRotor(m, e1, e2, e3);
		Multivector Ralt = rotationMatrixToRotorAlt(m, e1, e2, e3);

		System.out.println("R = " + R + ",");
		System.out.println("Ralt = " + Ralt + ",");
		 */

    }


    /** Creates a new instance of Util */
    public Util() {
    }

    /**
     * Invokes <code>round(multipleOf, epsilon)</code> on each entry in <code>A</code>
     */
    public static ArrayList round(ArrayList A, double multipleOf, double epsilon) {
		ArrayList L = new ArrayList();
		for (int i = 0; i < A.size(); i++) {
			L.add(((BasisBlade)A.get(i)).round(multipleOf, epsilon));
		}
		return L;
    }

    // *!*HTML_TAG*!* factorize_blade
    /**
     * Factorizes a blade B.
     *
     * @returns the k unit factors of the blade. Optionally returns the scale of the blade
     * in <code>scale[0]</code>. Returns null if B is a null blade.
     */
    public static Multivector[] factorizeBlade(Multivector B, double[] scale) {
		// get scale of blade, grade of blade
		int k = B.grade();
		double s = (k == 0) ? B.scalarPart() : B.norm_e();

		// detect non-blades
		if (k < 0) throw new java.lang.RuntimeException("Not a blade: " + B);

		// set scale of output, no matter what:
		if ((scale != null) && (scale.length >= 1)) scale[0] = s;

		// detect null-blades, scalar-blades
		if ((s == 0.0) || (k == 0))
			return new Multivector[0];


		// get largest basis blade, basis vectors
		BasisBlade E = B.largestBasisBlade();
		BasisBlade[] e = new BasisBlade[k];
		int idx = 0;
		for (int g = 0; g < B.spaceDim(); g++)
			if ((E.bitmap & (1 << g)) != 0)
			e[idx++] = new BasisBlade((1 << g), 1.0);

		// setup the 'current input blade'
		Multivector Bc = B.gp(1.0 / s);

		Multivector[] f = new Multivector[k];

		for (int i = 0; i < (k-1); i++) {
			// project basis vector e[i]:
			f[i] = new Multivector(e[i]).ip(Bc, LEFT_CONTRACTION).ip(Bc, LEFT_CONTRACTION); // no inverse required, since Bc is always unit

			// normalize f[i]
			f[i] = f[i].unit_e();

			// remove f[i] from Bc
			Bc = f[i].ip(Bc, LEFT_CONTRACTION); // no f[i].versorInverse() required, since f[i] is already unit
		}

		// last factor = what is left of the input blade
		f[k-1] = Bc.unit_e(); // already normalized, but renormalize to remove any FP round-off error

		return f;
    }

    /**
     * Alternative factorization of blade B (slower).
     *
     * @returns the k unit factors of the blade. Optionally returns the scale of the blade
     * in <code>scale[0]</code>. Returns null if B is a null blade.
     */
    public static Multivector[] factorizeBladeAlt(Multivector B, double[] scale) {
		// get scale of blade, grade of blade
		int k = B.grade();
		double s = (k == 0) ? B.scalarPart() : B.norm_e();

		// detect non-blades
		if (k < 0) throw new java.lang.RuntimeException("Not a blade: " + B);

		// set scale of output, no matter what:
		if ((scale != null) && (scale.length >= 1)) scale[0] = s;

		// detect null-blades, scalar-blades
		if ((s == 0.0) || (k == 0))
			return new Multivector[0];	BasisBlade E = B.largestBasisBlade();

			BasisBlade[] e = new BasisBlade[k];
			int idx = 0;
			for (int g = 0; g < B.spaceDim(); g++)
			if ((E.bitmap & (1 << g)) != 0)
				e[idx++] = new BasisBlade((1 << g), 1.0);

			Multivector R2 = B.gp((new Multivector(E)).versorInverse());

			Multivector[] f = new Multivector[k];
			Multivector F = null;
			for (int i = 0; i < e.length; i++) {
			Multivector e_i = new Multivector(e[i]);
			f[i] = R2.gp(e_i).gp(R2.versorInverse()).add(e_i).unit_e();
			if (i  == 0) F = f[i];
			else F = F.op(f[i]);
			}

	    if ((scale != null) && (scale.length >= 1))
		scale[0] = B.scp(F.versorInverse());


	    return f;
    }

    
    /**
     * Test for alternative blade factorization, which could be faster than
     * current algorithm (if implemented properly).
     *
     * @returns the k unit factors of the blade. Optionally returns the scale of the blade
     * in <code>scale[0]</code>. Returns null if B is a null blade.
     *
     * In Leo's Notes, look on 20070130 for the sign issues.
     * In Leo's Notes, look on 20070130 for the sign issues.
     * In Leo's Notes, look on 20070130 for the sign issues.
     * In Leo's Notes, look on 20070130 for the sign issues.
     *
     * Set k = grade(B)
     * Then multiply sign with (((k % 4) == 2) ? -1 : 1)
     * If scale of largest basis blade < 0, then multiply sign with (-1)^{k}
     *
     * Global scale is not fixed yet. Perhaps just put factors for
     * largest basis blade in k by k matrix, compute determinant, and compare.
     * (this could also fix sign issues . . .)
     *
     * Also needs a QR decomposition for orthogonalization . . .
     *
     * But the rest should be written in C++ for benchmarking against
     * current best implementation . . .
     *
     * Initial C++ implementation in factor.cpp
     */
    public static Multivector[] factorizeBladeAltFast(Multivector B, double[] scale) {
        // get scale of blade, grade of blade
        int k = B.grade();
        double s = (k == 0) ? B.scalarPart() : B.norm_e();

        // detect non-blades
        if (k < 0) throw new java.lang.RuntimeException("Not a blade: " + B);

        // set scale of output, no matter what:
        if ((scale != null) && (scale.length >= 1)) scale[0] = s;

        // detect null-blades, scalar-blades
        if ((s == 0.0) || (k == 0))
            return new Multivector[0];

        // get largest basis blade, basis vectors
        BasisBlade E = B.largestBasisBlade();
        
        int lowestBit = Bits.lowestOneBit(E.bitmap);
        int highestBit = Bits.highestOneBit(E.bitmap);
        
        Multivector[] f = new Multivector[k];
        if (k == 1) {
            f[0] = B.unit_e();
            return f;
        }
        
        if (E.scale < 0) { // we need positive scale for blade (can fix also with sign?(
            scale[0] *= -1.0;
            
            // take care of orientation of blade:
            if ((k & 1) == 1) scale[0] *= -1.0;
        }
        
        // fix sign issues (see Leo's notes)
        if ((k % 4) == 2) scale[0] *= -1.0;
        
        ArrayList bladesB = B.getBlades();
        
        int fIdx = 0;
        for (int i = lowestBit; i <= highestBit; i++) {
            // check if basis vector is present:
            if ((E.bitmap & (1 << i)) == 0) continue; // if not, no inner product
            
            // create bitmap for basis blade:
            int bb = E.bitmap ^ (1 << i);
            
            // compute inner product
            ArrayList L = new ArrayList();
            for (int j = 0; j < bladesB.size(); j++) {
                BasisBlade bladesBj = (BasisBlade)bladesB.get(j);
                if ((bladesBj.bitmap & bb) == bb) {
                    int vecBM = bladesBj.bitmap ^ bb;
                    double sc = 
                        bladesBj.scale * 
                        BasisBlade.canonicalReorderingSign(bb, bladesBj.bitmap);
                    
                    L.add(new BasisBlade(vecBM, sc));
                }
            }
            f[fIdx++] = new Multivector(L);
        }
        
        return f;
    }
    
    
    /**
     * This is a TEST for an algorithm that should factorize a versor V.
     *
     * @returns the k unit factors of the versor
     */
    public static Multivector[] factorizeVersor(Multivector V, Metric M) {
		int k = V.topGradeIndex();
		if (k == 0) return null;
		double VapplyMul = ((k & 1) == 0) ? 1.0 : -1.0; // even versor are applied differently from odd versors
		//Multivector[] f = new Multivector[k];

		// f-non-orthogonal (they are orthogonal in Euclidean metric, not in metric M)
		Multivector[] fno = factorizeBlade(V.extractGrade(k), null);

		//for (int i = 0; i < k; i++) {
		//    System.out.println("f" + i + " = " + fno[i].toString(bvNames) + ",");
		//}

		// orthogonalize:
		Multivector[] f = null; // orthogonal factors go here:
		if (M.isEuclidean() || M.isAntiEuclidean()) f = fno;
		else f = orthogonalize(fno, M, null);

		//for (int i = 0; i < k; i++) {
		//    System.out.println("fo" + i + " = " + f[i].toString(bvNames) + ",");
		//}

		// compute 'reciprocal' frame w.r.t to full space:
		Multivector I = new Multivector(1.0).dual(M);
		for (int i = 0; i < k; i++) {
			f[i] = f[i].ip(I, M, InnerProductTypes.LEFT_CONTRACTION).ip(I, M, InnerProductTypes.LEFT_CONTRACTION);
		}

		//for (int i = 0; i < k; i++) {
		//    System.out.println("fr" + i + " = " + f[i].toString(bvNames) + ",");
		//}

		// compute reflectors:
		Multivector[] R = new Multivector[k];
		Multivector Rall = new Multivector(1.0);
		Multivector Vi = V.versorInverse(M);
		for (int i = 0; i < k; i++) {
			Multivector source = Rall.gp(f[i], M).gp(Rall.versorInverse(M), M).gp(((i&1) == 0) ? 1.0 : -1.0);
			Multivector target = V.gp(f[i], M).gp(Vi, M).gp(VapplyMul);
			R[i] = source.subtract(target);
			//System.out.println("R" + i + " = " + R[i].toString(bvNames) + ";");
			Rall = R[i].gp(Rall, M);
		}

		// fix scale (todo: improve: scale of factors should be unit, and overall scale should be returned as in factorizeBlade):
		double mul = V.gp(Rall.versorInverse(M), M).scalarPart();
		Rall = Rall.gp(mul);

		System.out.println("Div: " + Rall.gp(V.versorInverse(M), M).compress(1e-6));

		return R;
    }


    /**
     * Orthogonalizes array of vectors @param fno
     *
     * @returns factors, orthogonal in metric 'M'
     *
     * Also returns new scale, if requested. The scale argument
     * should be the output of factorizeBlade()
     **/
    public static Multivector[] orthogonalize(Multivector[] fno, Metric M, double[] scale) {
		int k = fno.length;
		Multivector[] f = new Multivector[k];

		// compute metric matrix:
		DoubleMatrix2D MM = DoubleFactory2D.dense.make(k, k);
		for (int i = 0; i < k; i++) {
			for (int j = i; j < k; j++) {
			double val = fno[i].scp(fno[j], M);
			MM.setQuick(i, j, val);
			MM.setQuick(j, i, val);
			}
		}

		// compute eigenvalue decomposition
		EigenvalueDecomposition eig = new EigenvalueDecomposition(MM);
		DoubleMatrix2D EM = eig.getV();

		// orthogonalize factors (in metric M)
		for (int i = 0; i < k; i++) {
			Multivector factor = new Multivector(0.0);
			for (int j = 0; j < k; j++) {
			double m = EM.getQuick(j, i);
			if (m != 0)
				factor = factor.add(fno[j].gp(m));
			}
			f[i] = factor;
	//	    System.out.println("g" + i + " = " + f[i].toString(bvNames) + ",");
		   /* for (int j = 0; j <= i; j++) {
			System.out.println(" " + i + "." + j + " = " + f[i].scp(f[j], M));
			}*/
		}

		if (scale != null) {
			Multivector opFno = new Multivector(1.0);
			Multivector opF = new Multivector(1.0);

			for (int i = 0; i < k; i++) {
			opFno = opFno.op(fno[i]);
			opF = opF.op(f[i]);
			}

			double s = opF.gp(opFno.versorInverse()).scalarPart();
			scale[0] *= s;
		}

		return f;
    }



    /**
     * Converts 3x3 rotation matrix to rotor
     *
     * @param m m[i] _row_ 'i' of the matrix
     * @param e1 Your 'e1' basis vector
     * @param e2 Your 'e2' basis vector
     * @param e3 Your 'e3' basis vector
     */
    public static Multivector rotationMatrixToRotor(double m[][],
    Multivector e1, Multivector e2, Multivector e3) {
		double trace = m[0][0] + m[1][1] + m[2][2] + 1.0;
		double qw; // scalar coordinates
		double qx; // coordinate for (-?) e2^e3
		double qy; // coordinate for (-?) e3^e1
		double qz; // coordinate for (-?) e1^e2
		if (trace > 0.00001 ) {
			double s = 0.5 / Math.sqrt(trace);
			qw = 0.25 / s;
			qw = Math.sqrt(trace) * (0.5f);
			qx = (m[2][1] - m[1][2]) * s;
			qy = (m[0][2] - m[2][0]) * s;
			qz = (m[1][0] - m[0][1]) * s;
		}
		else {
			if (m[0][0] > m[1][1] && m[0][0] > m[2][2]) {
			double s = 2.0f * Math.sqrt( 1.0f + m[0][0] - m[1][1] - m[2][2]);
			qx = 0.25 * s;
			qy = (m[0][1] + m[1][0]) / s;
			qz = (m[0][2] + m[2][0]) / s;
			qw = (m[1][2] - m[2][1]) / s;
			}
			else if (m[1][1] > m[2][2]) {
			double s = 2.0 * Math.sqrt( 1.0f + m[1][1] - m[0][0] - m[2][2]);
			qx = (m[0][1] + m[1][0]) / s;
			qy = 0.25 * s;
			qz = (m[1][2] + m[2][1]) / s;
			qw = (m[0][2] - m[2][0]) / s;
			}
			else {
			double s = 2.0 * Math.sqrt( 1.0f + m[2][2] - m[0][0] - m[1][1] );
			qx = (m[0][2] + m[2][0]) / s;
			qy = (m[1][2] + m[2][1]) / s;
			qz = 0.25 * s;
			qw = (m[0][1] - m[1][0]) / s;
			}
		}


		return new Multivector(qw).add(
		e2.op(e3).gp(qx)).add(
		e3.op(e1).gp(qy)).add(
		e1.op(e2).gp(qz));
    }


    protected static Multivector matrixVectorMultiply(double m[][],
    double v[], Multivector e1, Multivector e2, Multivector e3) {
		return e1.gp(m[0][0] * v[0] + m[0][1] * v[1] + m[0][2] * v[2]).add(
		e2.gp(m[1][0] * v[0] + m[1][1] * v[1] + m[1][2] * v[2])).add(
		e3.gp(m[2][0] * v[0] + m[2][1] * v[1] + m[2][2] * v[2]));
		}
		/**
		 * Converts 3x3 rotation matrix to rotor
		 *
		 * @param m m[i] _row_ 'i' of the matrix
		 * @param e1 Your 'e1' basis vector
		 * @param e2 Your 'e2' basis vector
		 * @param e3 Your 'e3' basis vector
		 */
		public static Multivector rotationMatrixToRotorAlt(double m[][],
		Multivector e1, Multivector e2, Multivector e3) {
		Multivector R1 = new Multivector(1.0).add(e1.gp(matrixVectorMultiply(m, new double[]{1.0, 0.0, 0.0}, e1, e2, e3)));
		Multivector v2 = R1.gp(matrixVectorMultiply(m, new double[]{0.0, 1.0, 0.0}, e1, e2, e3)).gp(R1.versorInverse());
		Multivector R2 = new Multivector(1.0).add(e2.gp(v2));
		return R2.gp(R1).unit_e();
    }


}
