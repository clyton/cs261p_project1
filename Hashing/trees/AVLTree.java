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
		root = deleteNode(root, key);

		return root != null;
	}

	private AVLTree<E>.Node deleteNode(AVLTree<E>.Node root, E key) {

		if (root == null)
			return root;

		// If the key to be deleted is smaller than
		// the root's key, then it lies in left subtree
		if (key.compareTo(root.val) < 0)
			root.left = deleteNode(root.left, key);

		// If the key to be deleted is greater than the
		// root's key, then it lies in right subtree
		else if (key.compareTo(root.val) > 0)
			root.right = deleteNode(root.right, key);

		// if key is same as root's key, then this is the node
		// to be deleted
		else {

			// node with only one child or no child
			if ((root.left == null) || (root.right == null)) {
				Node temp = null;
				if (temp == root.left)
					temp = root.right;
				else
					temp = root.left;

				// No child case
				if (temp == null) {
					temp = root;
					root = null;
				} else {
//					root = temp;
					root.val = temp.val; // Copy contents of non-empty child
					root.left = temp.left;
					temp.left = null;
					root.right = temp.right;
					temp.right = null;
				}
			} else {

				// node with two children: Get the inorder
				// successor (smallest in the right subtree)
				Node temp = findPredecessor(root.right);

				// Copy the inorder successor's data to this node
				root.val = temp.val;

				// Delete the inorder successor
				root.right = deleteNode(root.right, temp.val);
			}
		}

		// If the tree had only one node then return
		if (root == null)
			return root;

		// STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
		root.height = Math.max(height(root.left), height(root.right)) + 1;

		// STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
		// this node became unbalanced)
		int balance = balanceFactor(root);

		// If this node becomes unbalanced, then there are 4 cases
		// Left Left Case
		if (balance > 1 && balanceFactor(root.left) >= 0)
			return rightRotate(root);

		// Left Right Case
		if (balance > 1 && balanceFactor(root.left) < 0) {
			root.left = leftRotate(root.left);
			return rightRotate(root);
		}

		// Right Right Case
		if (balance < -1 && balanceFactor(root.right) <= 0)
			return leftRotate(root);

		// Right Left Case
		if (balance < -1 && balanceFactor(root.right) > 0) {
			root.right = rightRotate(root.right);
			return leftRotate(root);
		}

		return root;

	}

	private AVLTree<E>.Node findPredecessor(AVLTree<E>.Node node) {
		Node pred = node;
		while (pred.left != null) {
			pred = pred.left;
		}
		return pred;
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
		if (h == 0) {
			System.out.println("null");
			return;
		}
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AVLTree<Integer> bt = new AVLTree<>();
		for (int i = 0; i < 7; i++)
			insert(bt, i);
		bt.print();
		for (int i = 0; i < 7; i++) {
			System.out.println("Delete i = " + i);
			delete(bt, i);
			System.out.println(bt.search(i));
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
