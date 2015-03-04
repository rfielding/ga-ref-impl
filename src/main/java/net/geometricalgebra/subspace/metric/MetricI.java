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
 * Created on April 28, 2005, 10:50 AM
 *
 * Copyright Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.metric;

import cern.colt.matrix.linalg.*;
import cern.colt.matrix.*;

/**
 *
 * @author  fontijne
 */
public interface MetricI {

	/**
	 * @return true if this metric is Euclidean
	 *
	 * <p>There is no tolerance. The metric must be exactly Euclidean.
	 */
	public boolean isEuclidean();

	/**
	 * @return true if this metric is anti-Euclidean
	 *
	 * <p>There is no tolerance. The metric must be exactly Anti-Euclidean.
	 */
	public boolean isAntiEuclidean();

	/**
	 * @return true if this metric non-zero entries has only only
	 * on the diagonal of it's matrix. 
	 *
	 * <p>In other words: {e_i . e_j = 0} if (i != j)
	 *
	 * <p>There is no tolerance. Even the smallest non zero off-diagonal entry
	 * will cause this method to return false.
	 */
	public boolean isDiagonal();

	/**
	 * Returns the symmetric, real matrix representing the metric.
	 *
	 * <p>Each entry M[i, j] holds the value of e_i . e_j
	 *
	 * <p>This matrix may or may not be created from scratch on each
	 * call (so don't count on it being efficient, although it is easy
	 * to 'cache' the result of the first call to this method when efficiency
	 * is required).
	 *
	 * <p>Do not modify the returned matrix.
	 *
	 */
	public DoubleMatrix2D getInnerProductMatrix();

	/**
	 * @return the value of the inner product: e_idx .e_idx
	 */
	public double getBasisVectorIp(int idx);

	/**
	 * @return the value of the inner product: e_idx1 .e_idx2
	 */
	public double getBasisVectorIp(int idx1, int idx2);

	/**
	 * Returns the eigenvalue decomposition of the symmetric, real matrix representing the metric.
	 *
	 * <p>This EigenvalueDecomposition may or may not be created from scratch on each
	 * call (so don't count on it being efficient, although it is easy
	 * to 'cache' the result of the first call to this method when efficiency
	 * is required).
	 *
	 * <p>Do not modify the returned EigenvalueDecomposition.
	 */
	public EigenvalueDecomposition getInnerProductMatrixEigenvalueDecomposition();
}
