import java.util.Arrays;
import java.util.Random;

public class BinarySearchTree<E extends Comparable<E>> implements Tree<E> {
	private class Node {
		E val;
		Node left;
		Node right;

		Node(E value) {
			this.val = value;
		}

	}

	Node root = null;

	@Override
	public void create() {

	}

	private Node insert(Node node, E e) {
		if (node == null)
			return new Node(e);

		if (node.val.compareTo(e) < 0) {
			node.right = insert(node.right, e);
			return node;
		}
		if (node.val.compareTo(e) > 0) {
			node.left = insert(node.left, e);
			return node;
		} else
			return node; // duplicates not handled
	}

	@Override
	public void insert(E value) {

		root = insert(root, value);
	}

	private Node delete(E key, BinarySearchTree<E>.Node node) {
		if (node == null)
			return node;

		if (node.val.compareTo(key) < 0) {
			node.right = delete(key, node.right);
			return node;
		}

		if (node.val.compareTo(key) > 0) {
			node.left = delete(key, node.left);
			return node;

		}

		else {
			if (node.left == null && node.right == null)
				return null;

			if (node.left == null)
				return node.right;

			if (node.right == null)
				return node.left;

			else {
				Node parent = node;
				Node succ = node.right;
				while (succ.left != null) {
					parent = succ;
					succ = succ.left;
				}

				parent.left = parent.left == succ ? succ.right : parent.left;
				parent.right = parent.right == succ ? succ.right : parent.right;

				succ.left = node.left;
				succ.right = node.right;
				return succ;
			}
		}
	}

	private Node findMin(BinarySearchTree<E>.Node node) {
		Node succ = node;
		while (succ.left != null) {
			succ = succ.left;
		}
		return succ;
	}

	@Override
	public E search(E e) {
		Node temp = root;
		while (temp != null) {
			if (temp.val.compareTo(e) == 0)
				return temp.val;
			else if (temp.val.compareTo(e) > 0)
				temp = temp.left;
			else
				temp = temp.right;
		}
		return null;
	}

	@Override
	public boolean delete(E key) {
		if (search(key) == null)
			return false;
		root = delete(key, root);
		return true;
	}

	public String print() {
		StringBuffer sb = new StringBuffer();
		print(root, sb);
		return sb.toString();
	}

	public void print(Node node, StringBuffer sb) {
		if (node == null) {
			sb.append("null").append(",");
			return;
		} else {
			sb.append(node.val).append(",");
			print(node.left, sb);
			print(node.right, sb);
		}
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
		if ((min != null && node.val.compareTo(min) < 0)
				|| (max != null && node.val.compareTo(max) > 0))
			return false;

		return isValidBST(node.left, node.val, min)
				&& isValidBST(node.right, max, node.val);

	}

	public static void main(String args[]) {
		BinarySearchTree<Integer> t = new BinarySearchTree<>();
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

			System.out.println(t.print());
		}
		for (int i = 0; i < size; i++) {
//		for (int i = size - 1; i >= 0; i--) {
//		for (int i = 0, count = 0; count < size; i = (i + count + 1) % size, count++) {
			System.out.println("deleting " + arr[i]);
			t.delete(arr[i]);

			if (!t.isValidBST(t.root))
				System.out.println("Fail at " + arr[i]);

			System.out.println(t.print());
		}

	}

}
