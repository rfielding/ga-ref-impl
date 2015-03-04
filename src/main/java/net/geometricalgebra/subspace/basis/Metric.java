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
 * Metric.java
 *
 * Created on February 1, 2005, 12:20 PM
 *
 * Copyright 2005-2007 Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.basis;

import java.util.ArrayList;
import cern.colt.matrix.*;
import cern.colt.matrix.linalg.*;
import net.geometricalgebra.subspace.metric.*;

/**
 * A class for representing totally arbitrary metric. Metric is given
 * in the form of a (symmetric) matrix. The eigenvectors of the matrix
 * are computed since these are used for computing (geometric) products
 * in this metric.
 *
 * @author  fontijne
 */
public class Metric implements MetricI {
	public static void main(String args[]) {
		double[][] m = new double[][]{{1.0, 0.0, 0.0}, {0.0, 0.0, -1.0},
				{0.0, -1.0, 0.0}};

		try {
			Metric M = new Metric(m);
		} catch (MetricException E) {
		}

	}

	/** Creates a new instance of Metric */
	public Metric(double[][] m) throws MetricException {
		this(DoubleFactory2D.dense.make(m));
	}

	// *!*HTML_TAG*!* arbitrary_metric_constructor
	/** Creates a new instance of Metric */
	public Metric(DoubleMatrix2D m) throws MetricException {
		m_matrix = m.copy();
		if (!Property.ZERO.isSymmetric(m_matrix))
			throw new MetricException("The metric matrix must be symmetric");

		// compute eigen value decomposition
		m_eig = new EigenvalueDecomposition(m_matrix);

		m_invEigMatrix = Algebra.ZERO.transpose(m_eig.getV());

		m_eigenMetric = new double[m_eig.getRealEigenvalues().size()];
		for (int i = 0; i < m_eigenMetric.length; i++)
			m_eigenMetric[i] = m_eig.getRealEigenvalues().get(i);

		m_isDiagonal = cern.colt.matrix.linalg.Property.ZERO
				.isDiagonal(m_matrix);
		if (!m_isDiagonal) {
			m_isEuclidean = m_isAntiEuclidean = false;
		} else {
			m_isEuclidean = m_isAntiEuclidean = true;
			for (int i = 0; i < m_matrix.columns(); i++) {
				if (m_matrix.getQuick(i, i) != 1.0)
					m_isEuclidean = false;
				if (m_matrix.getQuick(i, i) != -1.0)
					m_isAntiEuclidean = false;
			}
		}
	}

	/** transforms a to the eigen basis (a must be on metric basis) */
	public ArrayList toEigenbasis(BasisBlade a) {
		return transform(a, m_invEigMatrix);
	}

	/** transforms A to the eigen basis (A must be on metric basis) */
	public ArrayList toEigenbasis(ArrayList a) {
		ArrayList result = new ArrayList();
		for (int i = 0; i < a.size(); i++) {
			ArrayList tmp = toEigenbasis((BasisBlade) a.get(i));
			result.addAll(tmp);
		}
		return BasisBlade.simplify(result);
	}

	/** transforms a to the metric basis (A must be on eigenbasis) */
	public ArrayList toMetricBasis(BasisBlade a) {
		return transform(a, m_eig.getV());
	}

	/** transforms A to the metric basis (a must be on eigenbasis) */
	public ArrayList toMetricBasis(ArrayList a) {
		ArrayList result = new ArrayList();
		for (int i = 0; i < a.size(); i++) {
			ArrayList tmp = toMetricBasis((BasisBlade) a.get(i));
			result.addAll(tmp);
		}
		return BasisBlade.simplify(result);
	}

	/** transforms a BasisBlade to a new basis */
	protected ArrayList transform(BasisBlade a, DoubleMatrix2D M) {
		ArrayList A = new ArrayList();
		A.add(new BasisBlade(a.scale)); // start with just scalar;

		// for each 1 bit: convert to list of blades
		int i = 0;
		int b = a.bitmap;
		while (b != 0) {
			if ((b & 1) != 0) {
				// take column 'i' out of the matrix, wedge it to 'A'
				ArrayList tmp = new ArrayList();
				for (int j = 0; j < M.rows(); j++) {
					if (M.get(j, i) != 0) {
						double m = M.get(j, i);
						for (int k = 0; k < A.size(); k++)
							tmp.add(BasisBlade.op((BasisBlade) A.get(k),
									new BasisBlade((1 << j), m)));
					}
				}
				A = tmp;
			}

			b >>= 1;
			i++;
		}
		return A;
	}

	/** returns metric when in 'eigenspace'. Do not modify the array! */
	public double[] getEigenMetric() {
		return m_eigenMetric;
	}

	public double getBasisVectorIp(int idx) {
		return getBasisVectorIp(idx, idx);
	}

	public double getBasisVectorIp(int idx1, int idx2) {
		return m_matrix.get(idx1, idx2);
	}

	public cern.colt.matrix.DoubleMatrix2D getInnerProductMatrix() {
		return m_matrix;
	}

	public cern.colt.matrix.linalg.EigenvalueDecomposition getInnerProductMatrixEigenvalueDecomposition() {
		return m_eig;
	}

	public boolean isAntiEuclidean() {
		return m_isAntiEuclidean;
	}

	public boolean isEuclidean() {
		return m_isEuclidean;
	}

	public boolean isDiagonal() {
		return m_isDiagonal;
	}

	/** the metric (symmetric matrix) */
	private DoubleMatrix2D m_matrix;

	/** the eigenvectors matrix & eigenvalues of m_matrix */
	protected EigenvalueDecomposition m_eig;

	/** inverse of the eigenmatrix */
	protected DoubleMatrix2D m_invEigMatrix;

	protected double[] m_eigenMetric;

	protected boolean m_isDiagonal;
	protected boolean m_isEuclidean;
	protected boolean m_isAntiEuclidean;
}
