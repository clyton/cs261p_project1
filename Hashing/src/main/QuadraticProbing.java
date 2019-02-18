package main;

public class QuadraticProbing<Key, Value> implements HashTable<Key, Value> {

	int PRIME_NUMBER = 11;
	int capacity = PRIME_NUMBER;
	int size = 0;
	Entry<Key, Value> hashTable[];

	public QuadraticProbing() {
		hashTable = (Entry<Key, Value>[]) new Entry<?, ?>[capacity];

	}

	public QuadraticProbing(int capacity) {
		this.capacity = capacity;
		hashTable = (Entry<Key, Value>[]) new Entry<?, ?>[capacity];

	}

	public int size() {
		return size;
	}

	public int capacity() {
		return capacity;
	}

	private int hash(Key key, int count) {
		if (key == null)
			throw new IllegalArgumentException();

		int hashCode = key.hashCode();
		hashCode = hashCode < 0 ? -1 * hashCode : hashCode;
		hashCode = hashCode % capacity;
		return (hashCode + (count * count) % capacity) % capacity;
	}

	@Override
	public void put(Key key, Value val) {
		if (key == null)
			throw new IllegalArgumentException();
		int count = 0;
		int cellNo = 0;
		// https://stackoverflow.com/questions/12121217/limit-for-quadratic-probing-a-hash-table
		while ((cellNo = hash(key, count)) >= 0 && hashTable[cellNo] != null
				&& !key.equals(hashTable[cellNo].getKey())
				&& count < capacity) {
			count++;
		}

		if (count >= capacity) {
			resize(2 * capacity);
			put(key, val);
			return;
		}

		if (hashTable[cellNo] == null) { // not the same key
			size++;
		}

		Entry<Key, Value> iEntry = new Entry<>(key, val);
		hashTable[cellNo] = iEntry;

		if (loadFactor() > 0.70)
			resize(2 * capacity);

	}

	private void resize(int capacity) {
		this.capacity = capacity;

		QuadraticProbing<Key, Value> resizedTable = new QuadraticProbing<>(
				capacity);

		for (int i = 0; i < hashTable.length; i++) {
			if (hashTable[i] != null)
				resizedTable.put(hashTable[i].getKey(),
						hashTable[i].getValue());
		}

		this.capacity = resizedTable.capacity;
		this.hashTable = resizedTable.hashTable;
		this.size = resizedTable.size;

	}

	@Override
	public Value get(Key key) {
		int cell = getIndex(key);
		return cell == -1 ? null : hashTable[cell].getValue();
	}

	/**
	 * gets the index where the key is stored
	 * 
	 * @param key
	 * @return
	 */
	private int getIndex(Key key) {
		if (key == null)
			throw new IllegalArgumentException();

		int count = 0;
		int cellNo = hash(key, count);
		int startCell = cellNo;

		while (hashTable[cellNo] != null
				&& !hashTable[cellNo].getKey().equals(key)
				&& count < capacity) {

			cellNo = hash(key, ++count);
		}

		if (count >= capacity || hashTable[cellNo] == null)
			return -1;

		return cellNo;

	}

	@Override
	public void remove(Key key) {

		int count = 0;
		int cell = hash(key, count);

		while (hashTable[cell] != null && !key.equals(hashTable[cell].getKey())
				&& count < capacity) {
			cell = hash(key, ++count);
		}

		if (hashTable[cell] == null || count >= capacity)
			return;

		hashTable[cell] = null;
		// matching key found
		int nextCell;
		for (nextCell = hash(key, ++count); hashTable[nextCell] != null
				&& count < capacity; nextCell = hash(key, ++count)) {
			if (hash(hashTable[nextCell].getKey(), 0) == hash(key, 0)) {
				hashTable[cell] = hashTable[nextCell];
				cell = nextCell;
			}
		}

		hashTable[cell] = null;

		size--;
		if (loadFactor() < 0.1)
			resize(capacity / 2);

	}

	public double loadFactor() {
		return size * 1.0 / capacity;
	}

	public String toString() {
		StringBuffer repr = new StringBuffer("[");
		for (int i = 0; i < hashTable.length; i++) {
			if (hashTable[i] != null)
				repr.append(String.format("{%s,%s}", hashTable[i].getKey(),
						hashTable[i].getValue()));
			if (i != hashTable.length - 1)
				repr.append(", ");
		}
		repr.append("]");
		return repr.toString();
	}

}
