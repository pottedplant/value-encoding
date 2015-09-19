package com.unicorntoast.encoding.value.util;

public abstract class Chars {

	public static int indexOf(char[] array,char value) {
		for(int i=0;i<array.length;++i)
			if( array[i]==value )
				return i;
		
		return -1;
	}

}
