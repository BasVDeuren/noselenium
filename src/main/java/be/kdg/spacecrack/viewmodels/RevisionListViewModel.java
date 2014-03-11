package be.kdg.spacecrack.viewmodels;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import java.util.ArrayList;
import java.util.List;

public class RevisionListViewModel {
    List<Integer> revisions = new ArrayList<Integer>();

    public List<Integer> getRevisions() {
        return revisions;
    }

    public void setRevisions(List<Integer> revisions) {
        this.revisions = revisions;
    }
}
