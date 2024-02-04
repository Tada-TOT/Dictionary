public class AVLTree<T extends Comparable<? super T>> extends BST<T> {

	protected int height;

	public AVLTree() {
		super();
		height = -1;
	}

	public AVLTree(BSTNode<T> root) {
		super(root);
		height = -1;
	}

	public int getHeight() {
		return getHeight(root);
	}

	private int getHeight(BSTNode<T> node) {
		if (node == null)
			return -1;
		else
			return 1 + Math.max(getHeight(node.left), getHeight(node.right));
	}

	private AVLTree<T> getLeftAVL() {
		AVLTree<T> leftSubTree = new AVLTree<T>(root.left);
		return leftSubTree;
	}

	private AVLTree<T> getRightAVL() {
		AVLTree<T> rightSubTree = new AVLTree<T>(root.right);
		return rightSubTree;
	}

	protected int getBalanceFactor() {
		if (isEmpty())
			return 0;
		else
			return getRightAVL().getHeight() - getLeftAVL().getHeight();
	}

	public void insertAVL(T el) {
		super.insert(el);
		this.balance();
	}

	public boolean insertAVLNoDup(T el) {
		BSTNode<T> p = root, prev = null;
		boolean duplicate = false;
		while ((p != null) & !duplicate) { // find a place for inserting new node;
			prev = p;
			if (el.compareTo(p.el) < 0)
				p = p.left;
			else if (el.compareTo(p.el) > 0)
				p = p.right;
			else
				duplicate = true;
		}
		if (root == null) // tree is empty;
			root = new BSTNode<T>(el);
		else if (el.compareTo(prev.el) < 0)
			prev.left = new BSTNode<T>(el);
		else if (el.compareTo(prev.el) > 0)
			prev.right = new BSTNode<T>(el);
		this.balance();
		return duplicate;
	}

	public boolean deleteAVL(T el) {
		// Q1
		boolean result = super.deleteByCopying(el);// Using the super class BST delete method.
		if (result)
			balance(); // Balance the tree after deleting.
		return result;
	}

	protected void balance() {
		if (!isEmpty()) {
			getLeftAVL().balance();
			getRightAVL().balance();

			adjustHeight();

			int balanceFactor = getBalanceFactor();

			if (balanceFactor == -2) {
				if (getLeftAVL().getBalanceFactor() < 0)
					rotateRight();
				else
					rotateLeftRight();
			}

			else if (balanceFactor == 2) {
				if (getRightAVL().getBalanceFactor() > 0)
					rotateLeft();
				else
					rotateRightLeft();
			}
		}
	}

	protected void adjustHeight() {
		if (isEmpty())
			height = -1;
		else
			height = 1 + Math.max(getLeftAVL().getHeight(), getRightAVL().getHeight());
	}

	protected void rotateRight() {
		// Q1
		BSTNode<T> tempNode = root.right; // Variable to hold the right-subtree.
		root.right = root.left; // assigning the left-subtree to the right.
		root.left = root.right.left; // assigning the left-subtree to be the child of old left-subtree.
		root.right.left = root.right.right; // assigning the left-subtree of old left-subtree to be the right-subtree of
											// old left-subtree.
		root.right.right = tempNode; // assigning the right of old left-subtree to be the old right-subtree.
		// The following three lines are to swap the value of the nodes.
		T val = (T) root.el;
		root.el = root.right.el;
		root.right.el = val;
		// The following two lines are to adjust the height factor of the tree.
		getRightAVL().adjustHeight();
		adjustHeight();
	}

	protected void rotateLeft() {
		BSTNode<T> tempNode = root.left;
		root.left = root.right;
		root.right = root.left.right;
		root.left.right = root.left.left;
		root.left.left = tempNode;

		T val = (T) root.el;
		root.el = root.left.el;
		root.left.el = val;

		getLeftAVL().adjustHeight();
		adjustHeight();
	}

	protected void rotateLeftRight() {
		// Q1
		getLeftAVL().rotateLeft(); // rotate the lift-subtree to left.
		getLeftAVL().adjustHeight(); // adjust the height factor of the subtree.
		rotateRight(); // Rotate the current node to right.
		adjustHeight(); // adjust the height factor of the current node.
	}

	protected void rotateRightLeft() {
		getRightAVL().rotateRight();
		getRightAVL().adjustHeight();
		this.rotateLeft();
		this.adjustHeight();
	}

	public void mergeTrees(AVLTree<T> tree){
		BSTNode<T> p = tree.root;
		Stack<BSTNode<T>> travStack = new Stack<BSTNode<T>>();
		while (p != null) {
			while(p != null) {               // stack the right child (if any)
				if (p.right != null)        // and the node itself when going
					travStack.push(p.right); // to the left;
				travStack.push(p);
				p = p.left;
			}
			p = travStack.pop();             // pop a node with no left child
			while (!travStack.isEmpty() && p.right == null) { // visit it and all
				this.insertAVLNoDup(p.el);                   // nodes with no right child;
				p =  travStack.pop();
			}
			this.insertAVLNoDup(p.el);                           // visit also the first node with
			if (!travStack.isEmpty())        // a right child (if any);
				p = travStack.pop();
			else p = null;
		}
	}
}