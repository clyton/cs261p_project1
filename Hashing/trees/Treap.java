import java.util.Arrays;
import java.util.Random;

public class Treap<E extends Comparable<E>> implements Tree<E> {

	class Node {
		E key;
		int priority;
		Node left;
		Node right;

		Node(E key, int priority) {
			this.key = key;
			this.priority = priority;
		}

		Node(E key) {
			Random r = new Random();
			int p = r.nextInt(100_000_000);
			this.key = key;
			this.priority = p;
		}
	}

	Node root;

	@Override
	public void create() {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(E key) {
		// TODO Auto-generated method stub

		root = insert(root, key);

	}

	private Node leftRotate(Node parent) {
		Node newParent = parent.right;
		Node lChild = newParent.left;

		parent.right = lChild;
		newParent.left = parent;
		return newParent;
	}

	private Node rightRotate(Node parent) {
		Node newParent = parent.left;
		Node rChild = newParent.right;

		parent.left = rChild;
		newParent.right = parent;
		return newParent;
	}

	private Node insert(Node node, E key) {
		if (node == null) {
			return new Node(key);
		}

		if (node.key.compareTo(key) > 0) {
			node.left = insert(node.left, key);
			if (node.priority > node.left.priority) {
				Node rotatedNode = rightRotate(node);
				node = rotatedNode;
			}
			return node;
		}

		else {
			node.right = insert(node.right, key);
			if (node.priority > node.right.priority) {
				Node rotatedNode = leftRotate(node);
				node = rotatedNode;
			}
			return node;
		}

	}

	private double balanceFactor() {
		int maxHeight = maxHeight(root);
		int minHeight = minHeight(root);
		if (minHeight == 0)
			return 1;
		double balanceFactor = maxHeight / minHeight;
		return balanceFactor;
	}

	private int minHeight(Treap<E>.Node node) {
		if (node == null)
			return 0;
		return 1 + Math.min(maxHeight(node.left), maxHeight(node.right));
	}

	private int maxHeight(Treap<E>.Node node) {
		if (node == null)
			return 0;
		return 1 + Math.max(maxHeight(node.left), maxHeight(node.right));
	}

	@Override
	public boolean delete(E key) {
		root = delete(key, root);
		return false;
	}

	private Node delete(E key, Node node) {
		if (node == null)
			return null;

		if (node.key.compareTo(key) < 0) {
			node.right = delete(key, node.right);
			return node;
		} else if (node.key.compareTo(key) > 0) {
			node.left = delete(key, node.left);
			return node;
		} else {
			if (!(node.left == null && node.right == null)) {
				if (node.left == null) {
					Node rotatedNode = leftRotate(node);
					rotatedNode.left = delete(key, node);
					return rotatedNode;
				} else if (node.right == null
						|| (node.left.priority < node.right.priority)) {
					Node rotatedNode = rightRotate(node);
					rotatedNode.right = delete(key, node);
					return rotatedNode;
				} else {
					Node rotatedNode = leftRotate(node);
					rotatedNode.left = delete(key, node);
					return rotatedNode;
				}
			} else {
				return null;
			}
		}
	}

	@Override
	public E search(E key) {
		Node target = search(root, key);
		if (target == null)
			return null;
		else
			return target.key;
	}

	private Node search(Treap<E>.Node node, E key) {
		if (node == null)
			return null;
		if (node.key.compareTo(key) == 0)
			return node;

		if (node.key.compareTo(key) > 0)
			return search(node.left, key);

		else
			return search(node.right, key);
	}

	public String print() {
		StringBuffer sb = new StringBuffer();
		// print(root, sb);
		print(root, sb, 0, 10);
		sb.append("\n_______________________________________________");
		return sb.toString();
	}

	private void print(Treap<E>.Node node, StringBuffer sb, int space,
			int count) {

		if (node == null)
			return;

		space += count;
		print(node.right, sb, space, count);

		for (int i = count; i < space; i++) {
			sb.append(" ");
		}
		sb.append("(").append(node.key).append(",").append(node.priority)
				.append(")").append("\n");

		print(node.left, sb, space, count);
	}

	private boolean isValidBST(Node root) {

		if (root == null)
			return true;

		if (root.left == null && root.right == null)
			return true;

		return isValidBST(root, null, null);

	}

	private boolean isValidBST(Node node, E max, E min) {
		if (node == null)
			return true;
		if ((min != null && node.key.compareTo(min) < 0)
				|| (max != null && node.key.compareTo(max) > 0))
			return false;

		return isValidBST(node.left, node.key, min)
				&& isValidBST(node.right, max, node.key);

	}

	public static void main(String[] args) {

		Treap<Integer> t = new Treap<>();
		int size = 10;
		int[] arr = new int[size];
		for (int i = 0; i < size; i++) {
			arr[i] = i;
		}
		Random rand = new Random();
		for (int i = 1; i < size; i++) {
			int j = rand.nextInt(i);
			int temp = arr[j];
			arr[j] = arr[i];
			arr[i] = temp;
		}
		System.out.println(Arrays.toString(arr));
		for (int i = 0; i < size; i++) {

			t.insert(arr[i]);
			if (!t.isValidBST(t.root))
				System.out.println("Fail at " + arr[i]);

			System.out.println("Balance Factor : " + t.balanceFactor());
			System.out.println(t.print());
		}

		for (int i = 0; i < size; i++) {
//			for (int i = size - 1; i >= 0; i--) {
//			for (int i = 0, count = 0; count < size; i = (i + count + 1) % size, count++) {
			System.out.println("deleting " + arr[i]);
			t.delete(arr[i]);
			if (!t.isValidBST(t.root))
				System.out.println("Fail at " + arr[i]);
			System.out.println("Balance Factor : " + t.balanceFactor());
			System.out.println(t.print());
		}
	}

}
