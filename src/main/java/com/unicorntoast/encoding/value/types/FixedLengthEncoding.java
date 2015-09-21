package com.unicorntoast.encoding.value.types;

import com.unicorntoast.encoding.value.base.ValueBaseEncoding;
import com.unicorntoast.encoding.value.util.Charsets;
import com.unicorntoast.encoding.value.util.Verify;

public class FixedLengthEncoding {

	// state

	private final ValueBaseEncoding encoding;

	public final int bytes;
	public final int symbols;

	// impl

	public FixedLengthEncoding(ValueBaseEncoding encoding,int bytes) {
		this.encoding = Verify.notNull(encoding);
		Verify.verify(bytes>0);
		this.bytes = bytes;
		this.symbols = encoding.symbolsForBytes(bytes);
	}

	public String encode(byte[] bytes) {
		Verify.notNull(bytes);
		Verify.verify(bytes.length==this.bytes);
		return encoding.encode(bytes).toString();
	}

	public byte[] decode(String value) {
		char[] chars = Verify.notNull(value).toCharArray();
		Verify.verify(chars.length==symbols);
		return encoding.decode(chars);
	}

	public String encode(String value) { return encode(Verify.notNull(value).getBytes(Charsets.utf8)); }
	public String decodeToString(String value) { return new String(decode(value),Charsets.utf8); }

}
