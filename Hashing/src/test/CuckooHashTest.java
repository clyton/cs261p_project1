package test;

import main.CuckooHashTable;
import main.HashTable;

public class CuckooHashTest {
	public static void main(String[] args) {
		HashTable ht = new CuckooHashTable<Integer, Integer>();

		int keys[] = { 12, 26, 92, 23, 28, 94, 15 };
//		int keys[] = { 12, 26, 92, 23, 28, 94 };

		for (int key : keys) {
			ht.put(key, key);
			System.out.println(ht);
			System.out.println();
		}

		System.out.println(ht);

		ht.remove(12);
		System.out.println(ht);

		ht.remove(92);
		System.out.println(ht);

		ht.remove(23);
		System.out.println(ht);
	}

}
