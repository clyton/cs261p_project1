package main;

import java.util.Arrays;
import java.util.Random;

public class QuadraticProbingTest {
//	QuadraticProbing<Integer, Integer> ht = new QuadraticProbing<>();
//	LinearHashTable<Integer, Integer> ht = new LinearHashTable<>();
//	ChainedHashTable<Integer, Integer> ht = new ChainedHashTable<>();
//	CuckooHashTable<Integer, Integer> ht = new CuckooHashTable<>();
//	HashTable<Integer, Integer> ht = new CuckooHashTable<>();
//	HashTable<Integer, Integer> ht = new ChainedHashTable<>();
//	HashTable<Integer, Integer> ht = new LinearHashTable<>();
	HashTable<Integer, Integer> ht = new QuadraticProbing<>();

	public void generateRandomKeyValue(int[] key, int[] value) {
		for (int i = 0; i < key.length; i++) {
			key[i] = i;
			value[i] = key.length - i;
		}
//		Random random = new Random(3483578L);
		Random random = new Random();
//		System.out.println(3483578L);
		for (int i = 1; i < key.length; i++) {
			int randomIndex = random.nextInt(i);
			int temp = key[randomIndex];
			key[randomIndex] = key[i];
			key[i] = temp;

			temp = value[randomIndex];
			value[randomIndex] = value[i];
			value[i] = temp;
		}

	}

	public void populate(int key[], int value[], int start, int end) {
		System.out.printf("OPERATION, TIME , SIZE , CAPACITY , LOAD_FACTOR\n");

		for (int i = start; i < end; i++) {
			long startTime = System.nanoTime();
			ht.put(key[i], value[i]);
			System.out.printf("PUT, %d, %d, %d, %f\n",
					(System.nanoTime() - startTime), ht.size(), ht.capacity(),
					ht.loadFactor());

		}
	}

	/**
	 * loads table till load factor reaches the level given or all keys are
	 * exhausted
	 * 
	 * @param key
	 * @param value
	 * @param start
	 * @param loadFactor
	 * @return if {@code index} is the last element inserted, return index+1
	 */
	public int loadTable(int key[], int value[], int start, double loadFactor) {
		int i = 0;
		for (i = start; ht.loadFactor() < loadFactor && i < key.length; i++) {
			ht.put(key[i], value[i]);
		}
		return i;
	}

	public void getAllKeys(int[] key, int[] value) {

		for (int i = 0; i < key.length; i++) {
			Integer r = ht.get(key[i]);
			if (r == null || r != value[i])
				System.out.print("Expected value but got " + r);
		}
	}

	public void removeKeys(int[] key, int start, int end, int step) {
		for (int i = start; i < end && i < key.length; i += step) {
			ht.remove(key[i]);
		}
	}

	public void getKeysShouldPass(int[] key, int[] value, int start, int end,
			int step) {
		for (int i = start; i < end; i += step) {
			Integer r = ht.get(key[i]);
			if (r == null || r != value[i])
				System.out.println("Expected value but got " + r);
		}
	}

	public void getKeysShouldReturnNull(int[] key, int start, int end,
			int step) {
		for (int i = start; i < end; i += step) {
			Integer r = ht.get(key[i]);
			if (r != null)
				System.out.println("Expected null but got value " + r);
		}
	}

	public void test1() {

		final int testSize = 10;
		int key[] = new int[testSize];
		int value[] = new int[testSize];
		generateRandomKeyValue(key, value);
		populate(key, value, 0, key.length);
		System.out.println("Get keys at load factor = " + ht.loadFactor());
		getAllKeys(key, value);
		removeKeys(key, 0, key.length - 3, 1);
		getKeysShouldPass(key, value, key.length - 3, key.length, 1);
		getKeysShouldReturnNull(key, 0, key.length - 3, 1);
	}

	public void test2() {

		final int testSize = 100;
		int key[] = new int[testSize];
		int value[] = new int[testSize];
		generateRandomKeyValue(key, value);
//		populate(key, value, 0, key.length);
		int end = loadTable(key, value, 0, 0.69);
		System.out.println("Get keys at load factor = " + ht.loadFactor());
//		getAllKeys(key, value);
		System.out
				.println("load factor = " + ht.loadFactor() + " end = " + end);
		getKeysShouldPass(key, value, 0, end, 1);
		System.out.println(Arrays.toString(key));
		System.out.println(Arrays.toString(value));
		removeKeys(key, 0, end - 3, 1);
		System.out.println("Get keys at load factor = " + ht.loadFactor());
		getKeysShouldPass(key, value, end - 3, end, 1);
		System.out.println("Get keys at load factor = " + ht.loadFactor());
		getKeysShouldReturnNull(key, 0, end - 3, 1);
		System.out.println(ht);

	}

	// performance testing load, get and remove successful operations
	public void test3() {

		final int testSize = 1000;
		int key[] = new int[testSize];
		int value[] = new int[testSize];
		generateRandomKeyValue(key, value);
		populate(key, value, 0, key.length);
		getAndRemoveSuccessfully(key, value);
	}

	private void getAndRemoveSuccessfully(int[] key, int[] value) {

		System.out.printf("OPERATION, TIME , SIZE , CAPACITY , LOAD_FACTOR\n");
		for (int i = 0; i < key.length; i++) {

			double lf = ht.loadFactor();
			int capacity = ht.capacity();
			int size = ht.size();

			long start = System.nanoTime();
			Integer val = ht.get(key[i]);
			long getDuration = System.nanoTime() - start;
			System.out.printf("GET, %d, %d, %d, %f\n", getDuration, size,
					capacity, lf);

			start = System.nanoTime();
			ht.remove(key[i]);
			long removeDuration = System.nanoTime() - start;

			System.out.printf("REMOVE, %d, %d, %d, %f\n", removeDuration, size,
					capacity, lf);
		}

	}

	public static void main(String[] args) {
		QuadraticProbingTest qt = new QuadraticProbingTest();
//		qt.test1();
		qt.test2();

//		qt.test3();
	}

}
