package com.serialmmf.Anbattery.util;

import com.serialmmf.Anbattery.model.Task;

import java.util.Comparator;

public class AppShort implements Comparator {

	public int compare(Task info1, Task info2) {
		byte b = -1;
		if (info1 != null) {
			if (info2 == null) {
				b = 1;
			} else {
				long l = info1.mem;
				long ls = info2.mem;
				if (l <= ls)
					b = 1;
			}
		}
		return b;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		Task info1 = (Task) obj1;
		Task info2 = (Task) obj2;
		return compare(info1, info2);
	}

}
