package main;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ChainedHashTable<Key, Value> implements HashTable<Key, Value> {

	LinkedList<Entry<Key, Value>>[] hashTable;
	int PRIME_NUMBER = 11;
	int capacity = PRIME_NUMBER;
	int size = 0;

	public ChainedHashTable() {
		hashTable = (LinkedList<Entry<Key, Value>>[]) new Object[capacity];
	}

	public ChainedHashTable(int capacity) {
		this.capacity = capacity;
		hashTable = (LinkedList<Entry<Key, Value>>[]) new Object[capacity];
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
		return hashCode % capacity;
	}

	@Override
	public void put(Key key, Value val) {

		int cellNo = hash(key);

		if (hashTable[cellNo] == null)
			hashTable[cellNo] = new LinkedList<>();

		Entry<Key, Value> entry = new Entry<>(key, val);

		for (Entry<Key, Value> presentEntry : hashTable[cellNo]) {
			if (presentEntry.getKey().equals(key)) {
				presentEntry.setValue(val);
				return;
			}

		}

		hashTable[cellNo].add(entry);

		size++;

		if (size * 1.0 / capacity > 0.7)
			resize(capacity * 2);

	}

	private void resize(int capacity) {
		ChainedHashTable<Key, Value> resizedTable = new ChainedHashTable<>(
				capacity);
		for (int i = 0; i < hashTable.length; i++) {
			List<Entry<Key, Value>> iLink = hashTable[i];
			if (iLink == null)
				continue;

			for (Entry<Key, Value> entry : iLink) {
				resizedTable.put(entry.getKey(), entry.getValue());
			}
		}

		this.capacity = resizedTable.capacity;
		this.hashTable = resizedTable.hashTable;
		this.size = resizedTable.size;

	}

	@Override
	public Value get(Key key) {
		if (key == null)
			throw new IllegalArgumentException();
		int cellNo = hash(key);

		if (hashTable[cellNo] == null)
			return null;

		for (Entry<Key, Value> entry : hashTable[cellNo])
			if (entry.getKey().equals(key))
				return entry.getValue();

		return null;
	}

	@Override
	public void remove(Key key) {
		if (key == null)
			throw new IllegalArgumentException();

		int cellNo = hash(key);

		if (hashTable[cellNo] == null)
			return;

		Entry<Key, Value> iEntry;
		for (Iterator<Entry<Key, Value>> it = hashTable[cellNo].iterator(); it
				.hasNext();) {
			iEntry = it.next();

			if (iEntry.getKey().equals(key)) {
				it.remove();
				size--;
				return;
			}
		}

		if (size * 1.0 / capacity < 0.12)
			resize(capacity / 3);

	}

}
