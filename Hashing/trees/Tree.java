public interface Tree<E extends Comparable<E>> {
	public void create();

	public void insert(E value);

	public boolean delete(E key);

	public E search(E key);
}
