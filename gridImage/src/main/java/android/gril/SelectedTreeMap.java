package android.gril;

import android.net.Uri;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * User: tc
 * Date: 13-7-10
 * Time: 下午3:27
 */
public class SelectedTreeMap implements Serializable {
    private TreeMap<Long, Uri> treeMap;

    public TreeMap<Long, Uri> getTreeMap() {
        return treeMap;
    }

    public void setTreeMap(TreeMap<Long, Uri> treeMap) {
        this.treeMap = treeMap;
    }
}
