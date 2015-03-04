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
 * InnerProductTypes.java
 *
 * Created on October 11, 2005, 8:10 PM
 *
 * Copyright 2005-2007 Daniel Fontijne, University of Amsterdam
 * fontijne@science.uva.nl
 *
 */

package net.geometricalgebra.subspace.basis;

/**
 *
 * @author  fontijne
 */
public interface InnerProductTypes {
    public final static int LEFT_CONTRACTION = 1;
    public final static int RIGHT_CONTRACTION = 2;
    public final static int HESTENES_INNER_PRODUCT = 3;
    public final static int MODIFIED_HESTENES_INNER_PRODUCT = 4;

    public final static int LC = LEFT_CONTRACTION;
    public final static int RC = RIGHT_CONTRACTION;
    public final static int HIP = HESTENES_INNER_PRODUCT;
    public final static int MHIP = MODIFIED_HESTENES_INNER_PRODUCT;

}
