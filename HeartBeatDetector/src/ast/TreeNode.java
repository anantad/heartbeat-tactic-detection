package ast;
 
import java.util.ArrayList;
import java.util.List;


public class TreeNode<T> {

	private T data = null;

	private List<TreeNode<T>> children = new ArrayList<>();

	private List<String> visitedClass = new ArrayList<String>();
	
	private TreeNode<T> parent = null;

	public TreeNode(T data) {
		this.data = data;
	}

	public TreeNode<T> addChild(TreeNode<T> child) {
		child.setParent(this);
		this.children.add(child);
		return child;
	}

	public void addChildren(List<TreeNode<T>> children) {
		children.forEach(each -> each.setParent(this));
		this.children.addAll(children);
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}

	public TreeNode<T> getParent() {
		return parent;
	}
	
	public void deleteNode() {
		if (parent != null) {
			int index = this.parent.getChildren().indexOf(this);
			this.parent.getChildren().remove(this);
			for (TreeNode<T> each : getChildren()) {
				each.setParent(this.parent);
			}
			this.parent.getChildren().addAll(index, this.getChildren());
		} else {
			deleteRootNode();
		}
		this.getChildren().clear();
	}
	 
	 
	 
	public TreeNode<T> deleteRootNode() {
		if (parent != null) {
			throw new IllegalStateException("deleteRootNode not called on root");
		}
		TreeNode<T> newParent = null;
		if (!getChildren().isEmpty()) {
			newParent = getChildren().get(0);
			newParent.setParent(null);
			getChildren().remove(0);
			for (TreeNode<T> each : getChildren()) {
				each.setParent(newParent);
			}
			newParent.getChildren().addAll(getChildren());
		}
		this.getChildren().clear();
		return newParent;
	}

	

	public static void main(String[] args) {
		TreeNode<String> root = createTree();
		printTree(root, " ");
	}
 
	private static TreeNode<String> createTree() {
		TreeNode<String> root = new TreeNode<>("root");
				
		TreeNode<String> node1 = root.addChild(new TreeNode<String>("node 1"));
		
		TreeNode<String> node11 = node1.addChild(new TreeNode<String>("node 11"));
		TreeNode<String> node111 = node11.addChild(new TreeNode<String>("node 111"));
		TreeNode<String> node112 = node11.addChild(new TreeNode<String>("node 112"));
		
		TreeNode<String> node12 = node1.addChild(new TreeNode<String>("node 12"));
		
		TreeNode<String> node2 = root.addChild(new TreeNode<String>("node 2"));
		
		TreeNode<String> node21 = node2.addChild(new TreeNode<String>("node 21"));
		TreeNode<String> node211 = node2.addChild(new TreeNode<String>("node 22"));
		return root;
	}
	
	 private static <T> void printTree(TreeNode<T> node, String appender) {
		  System.out.println(appender + node.getData());
		  node.getChildren().forEach(each ->  printTree(each, appender + appender));
	 }

	public List<String> getVisitedClass() {
		return visitedClass;
	}

	public void setVisitedClass(List<String> visitedClass) {
		this.visitedClass = visitedClass;
	}
	 
}
/*
So as to be able to search the data in tree, here is the method that you can use to search the data. This is not optimised for performance. It is just a modification of the print method.
private static Optional> findDataInTree(Node node, T searchQuery) {
if(node.getData().equals(searchQuery)) {
return Optional.of(node);
}
for(Node each : node.getChildren()) {
System.out.println(each.getData());
Optional> findDataInTree = findDataInTree(each, searchQuery);
if(findDataInTree.isPresent()) {
return findDataInTree;
}
}
return Optional.empty();
}

And you use it like below

public static void main(String[] args) {
Node root = createTree();
printTree(root, " ");
String searchQuery = "node 22";
Optional> findDataInTree = findDataInTree(root, searchQuery);
System.out.println("Node with data \""+ searchQuery +"\" found :" +findDataInTree.isPresent());

}*/