public class AVLTree<E extends Comparable<E>> implements Tree<E> {
	private class Node {
		E val;
		Node left;
		Node right;
		int height;

		Node(E val) {
			this.val = val;
			height = 1;
		}

	}

	Node root = null;
	int size = 0;

	@Override
	public void create() {
		// TODO : create AVL tree code
	}

	private int height(Node root) {
		if (root == null)
			return 0;
		return root.height;
	}

	private int balanceFactor(Node root) {
		if (root == null)
			return 0;
		return height(root.left) - height(root.right);
	}

	private Node insert(Node root, E key) {
		if (root == null)
			return new Node(key);

		if (root.val.compareTo(key) > 0) {
			root.left = insert(root.left, key);
		} else if (root.val.compareTo(key) < 0) {
			root.right = insert(root.right, key);
		} else {
			return root;
		}

		root.height = 1 + Math.max(height(root.left), height(root.right));
		int balance = balanceFactor(root);

		// left left
		if (balance > 1 && root.left.val.compareTo(key) > 0) {
			return rightRotate(root);
		}
		// left right
		if (balance > 1 && root.left.val.compareTo(key) < 0) {
			root.left = leftRotate(root.left);
			return rightRotate(root);
		}

		// right right
		if (balance < -1 && root.right.val.compareTo(key) < 0) {
			return leftRotate(root);
		}

		// right left
		if (balance < -1 && root.right.val.compareTo(key) < 0) {
			root.right = rightRotate(root.right);
			return leftRotate(root);
		}
		return root;
	}

	private Node leftRotate(AVLTree<E>.Node root) {
		if (root == null)
			return null;

		Node right = root.right;
		Node leftOfRight = right.left;

		root.right = leftOfRight;
		right.left = root;

		root.height = 1 + Math.max(height(root.left), height(root.right));
		right.height = 1 + Math.max(height(right.left), height(right.right));

		return right;
	}

	private Node rightRotate(AVLTree<E>.Node root) {
		if (root == null)
			return null;

		Node left = root.left;
		Node leftOfRight = left.right;

		root.left = leftOfRight;
		left.right = root;

		root.height = 1 + Math.max(height(root.left), height(root.right));
		left.height = 1 + Math.max(height(left.left), height(left.right));

		return left;
	}

	@Override
	public void insert(E value) {

		root = insert(root, value);
		size++;
	}

	@Override
	public boolean delete(E key) {
		boolean foundAndDeleted = delete(key, root);
		return false;
	}

	private boolean delete(E key, AVLTree<E>.Node node) {
		if (node == null)
			return false;

		Node parent = new Node(null);
		parent.left = root;

		while (node != null && node.val.compareTo(key) != 0) {
			if (node.val.compareTo(key) < 0) {
				parent = node;
				node = node.right;
			} else {
				parent = node;
				node = node.left;
			}
		}
		if (node == null)
			return false;

		boolean status = true;
		if (node.left == null && node.right == null) {
			parent.left = parent.left == node ? null : parent.left;
			parent.right = parent.right == node ? null : parent.right;
		}

		else if (node.right == null) {
			parent.left = parent.left == node ? node.left : parent.left;
			parent.right = parent.right == node ? node.left : parent.right;
		}

		else if (node.left == null) {
			parent.left = parent.left == node ? node.right : parent.left;
			parent.right = parent.right == node ? node.right : parent.right;
		}

		else {
			Node[] predecessor = findPredecessor(node);
			Node pparent = predecessor[0];
			Node pred = predecessor[1];
			// unlink the predecessor from its parent;
			pparent.left = pred == pparent.left ? pred.right : pparent.left;
			pparent.right = pred == pparent.right ? pred.right : pparent.right;
			// unlink the node to delete from its children
			// link the predecessor to children of node to delete
			pred.left = node.left;
			pred.right = node.right;
			// link the parent of node to delete to pred
			parent.left = parent.left == node ? pred : parent.left;
			parent.right = parent.right == node ? pred : parent.right;
		}

		return status;
	}

	private AVLTree<E>.Node[] findPredecessor(AVLTree<E>.Node node) {
		Node parent = node;
		Node pred = node.right;
		while (pred.left == null) {
			parent = pred;
			pred = pred.left;
		}
		return (Node[]) new Object[] { parent, pred };
	}

	@Override
	public E search(E key) {
		// TODO Auto-generated method stub
		Node node = search(key, root);
		if (node == null)
			return null;
		else
			return node.val;
	}

	private Node search(E key, AVLTree<E>.Node node) {
		if (node == null)
			return null;

		if (node.val.compareTo(key) == 0)
			return node;

		if (node.val.compareTo(key) < 0)
			return search(key, node.right);
		else
			return search(key, node.left);
	}

	public void print() {
		int h = height(root);
		AVLTree<E>.Node[] tree = (AVLTree<E>.Node[]) new AVLTree<?>.Node[h * h
				- 1];
		print(tree, root, 0);
		for (Node node : tree) {
			if (node == null)
				System.out.print(node + ",");
			else
				System.out.print(node.val + ", ");
		}
		System.out.println();
	}

	public void print(Node[] tree, Node root, int i) {
		if (i >= tree.length || root == null)
			return;
		tree[i] = root;
		print(tree, root.left, i * 2 + 1);
		print(tree, root.right, i * 2 + 2);
	}

	private int isBalanced(Node root) {
		if (root == null)
			return 0;

		int leftHeight = isBalanced(root.left);
		int rightHeight = isBalanced(root.right);

		if (leftHeight == -1 || rightHeight == -1)
			return -1;

		int balanceFactor = Math.abs(leftHeight - rightHeight);

		if (balanceFactor < 2)
			return Math.min(leftHeight, rightHeight) + 1;

		else
			return -1;

	}

	private static <N extends Comparable<N>> boolean insert(AVLTree<N> bt,
			N i) {
		bt.insert(i);
		boolean isBalanced = bt.isBalanced(bt.root) != -1;
		if (!isBalanced)
			System.out.println("Not balanced at " + i);
		return isBalanced;
	}

	private static <N extends Comparable<N>> boolean delete(AVLTree<N> bt,
			N i) {
		bt.delete(i);
		boolean isBalanced = bt.isBalanced(bt.root) != -1;
		if (!isBalanced)
			System.out.println("Not balanced at " + i);
		return isBalanced;
	}

	public static void main(String[] args) {
		AVLTree<Integer> bt = new AVLTree<>();
		for (int i = 0; i < 7; i++)
			insert(bt, i);
		for (int i = 0; i < 7; i++) {
			delete(bt, i);
			bt.print();
		}
//		insert(bt, 1);
//		insert(bt, 3);
//		insert(bt, 4);
//		insert(bt, 5);
//		insert(bt, 10);
//		insert(bt, 57);
//		insert(bt, 2);
		bt.print();
	}

}
