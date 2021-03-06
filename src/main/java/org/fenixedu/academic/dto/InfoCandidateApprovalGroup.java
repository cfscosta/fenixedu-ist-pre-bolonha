/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoCandidateApprovalGroup extends InfoObject {

    protected String situationName;

    protected List candidates;

    public InfoCandidateApprovalGroup() {
        this.candidates = new ArrayList();
    }

    /**
     * @return
     */
    public List getCandidates() {
        return candidates;
    }

    /**
     * @return
     */
    public String getSituationName() {
        return situationName;
    }

    /**
     * @param list
     */
    public void setCandidates(List list) {
        candidates = list;
    }

    /**
     * @param string
     */
    public void setSituationName(String string) {
        situationName = string;
    }

}