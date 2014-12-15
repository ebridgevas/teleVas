package com.ebridge.vas.menu;

import com.ebridge.vas.model.TreeNode;
import com.ebridge.vas.util.NodeTypeNotFoundException;

/**
 * @author david@tekeshe.com
 */
public interface MenuDataWrapper<T> {

    public TreeNode<T> load() throws NodeTypeNotFoundException;
}
