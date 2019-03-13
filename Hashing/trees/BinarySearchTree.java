import java.util.Comparator;

public class BinarySearchTree<E extends Comparator<E>> implements Tree<E> {
	private class Node<E> {
		E val;
		Node<E> left;
		Node<E> right;

		Node(E value) {
			this.val = value;
		}

	}

	Node<E> root = null;

	@Override
	public void create() {

	}

	private void insert(Node<E> root, Node<E> e) {
		if (root == null)
			return;
		if ()
	}

	@Override
	public void insert(E value) {
		Node<E> temp = root;
		Node<E> iNode = new Node<>(value);

		insert(temp, iNode);
	}

	@Override
	public boolean delete(E key) {
		return false;
	}

	@Override
	public E search(E key) {
		return 0;
	}

}
