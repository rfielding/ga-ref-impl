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
 * MetricException.java
 *
 * Created on April 28, 2005, 12:59 PM
 *
 * Copyright Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.metric;

/**
 *
 * @author  fontijne
 */
public class MetricException extends java.lang.Exception {

	/**
	 * Creates a new instance of <code>MetricException</code> without detail message.
	 */
	public MetricException() {
	}

	/**
	 * Constructs an instance of <code>MetricException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public MetricException(String msg) {
		super(msg);
	}
}
