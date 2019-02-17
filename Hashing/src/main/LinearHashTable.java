package main;

import java.util.NoSuchElementException;

public class LinearHashTable<Key, Value> implements HashTable<Key, Value> {
	private Key[] keys;
	private Value[] values;
	private final int INITIAL_CAPACITY = 13;
	private int capacity = INITIAL_CAPACITY;
	private int size = 0;

	@SuppressWarnings("unchecked")
	public LinearHashTable() {
		keys = (Key[]) new Object[INITIAL_CAPACITY];
		values = (Value[]) new Object[INITIAL_CAPACITY];
	}

	@SuppressWarnings("unchecked")
	public LinearHashTable(int capacity) {
		this.capacity = capacity;
		keys = (Key[]) new Object[capacity];
		values = (Value[]) new Object[capacity];
	}

	public int size() {
		return this.size;
	}

	public int capacity() {
		return capacity;
	}

	private int hash(Key key) {
		if (key instanceof Integer) {
			int k = ((Integer) key).intValue();
			return k % capacity;
		}
		if (key == null)
			throw new IllegalArgumentException();

		int hashCode = key.hashCode();
		hashCode = hashCode < 0 ? -1 * hashCode : hashCode;
		return hashCode % capacity();
	}

	private void resize(int capacity) {
		this.capacity = capacity;

		LinearHashTable<Key, Value> temp = new LinearHashTable<>(capacity);

		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null)
				temp.put(keys[i], values[i]);
		}

		keys = temp.keys;
		values = temp.values;
		this.capacity = temp.capacity;
		this.size = temp.size;

	}

	@Override
	public void put(Key key, Value val) {
		int cellNo = hash(key);

		while (keys[cellNo] != null && !keys[cellNo].equals(key))
			cellNo = (cellNo + 1) % capacity();

		if (keys[cellNo] == null)
			size++;

		keys[cellNo] = key;
		values[cellNo] = val;

		if (loadFactor() > 0.7)
			resize(2 * capacity);
	}

	@Override
	public Value get(Key key) {
		int cellNo = hash(key);

		while (keys[cellNo] != null && !keys[cellNo].equals(key))
			cellNo = (cellNo + 1) % capacity();

		if (keys[cellNo] == null)
			return null;

		return values[cellNo];
	}

	@Override
	public void remove(Key key) {
		int cellNo = hash(key);

		while (keys[cellNo] != null && !keys[cellNo].equals(key))
			cellNo = (cellNo + 1) % capacity();

		if (keys[cellNo] == null)
			throw new NoSuchElementException(
					String.format("Element with key %s not found", key));

		keys[cellNo] = null;
		values[cellNo] = null;

		int cellToRehash = (cellNo + 1) % capacity();

		while (keys[cellToRehash] != null) {
			Key keyToRehash = keys[cellToRehash];
			Value valueToRehash = values[cellToRehash];

			int cellStartRange = hash(keyToRehash);
			if (!isInCircularRange(cellStartRange, cellNo, cellToRehash)) {
				keys[cellNo] = keyToRehash;
				values[cellNo] = valueToRehash;

				keys[cellToRehash] = null;
				values[cellToRehash] = null;

				cellNo = cellToRehash;
			}
			cellToRehash = (cellToRehash + 1) % capacity();
		}

		size--;
		if (loadFactor() < 0.1)
			resize(capacity / 2);
	}

	private boolean isInCircularRange(int index, int start, int end) {
//		System.out.println();
		if (start <= end) {
			if (index > start && index <= end)
				return true;
			else
				return false;
		} else {
			if (index > end && index <= start)
				return false;
			else
				return true;
		}
	}

	public Key[] getKeySet() {
		return keys;
	}

	public Value[] getValueSet() {
		return values;
	}

	public String toString() {
		StringBuffer repr = new StringBuffer("[");
		for (int i = 0; i < keys.length; i++) {
			repr.append(String.format("{%s,%s}", keys[i], values[i]));
			if (i != keys.length - 1)
				repr.append(", ");
		}

		return repr.toString();
	}

	public double loadFactor() {
		return size * 1.0 / capacity;
	}

}