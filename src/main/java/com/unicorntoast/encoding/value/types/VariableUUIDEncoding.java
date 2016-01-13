package com.unicorntoast.encoding.value.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

import com.unicorntoast.encoding.value.base.ValueBaseEncoding;
import com.unicorntoast.encoding.value.util.Verify;

public class VariableUUIDEncoding {

	// defs

	public static final int bytes = 16;

	public static final VariableUUIDEncoding base58_bitcoin  = new VariableUUIDEncoding(ValueBaseEncoding.base58_bitcoin);
	public static final VariableUUIDEncoding base58_ripple  = new VariableUUIDEncoding(ValueBaseEncoding.base58_ripple);
	public static final VariableUUIDEncoding base58_flickr  = new VariableUUIDEncoding(ValueBaseEncoding.base58_flickr);

	// state

	public final ValueBaseEncoding encoding;
	public final int maxSymbols;

	// impl

	public VariableUUIDEncoding(ValueBaseEncoding encoding) {
		this.encoding = Verify.notNull(encoding);
		this.maxSymbols = encoding.symbolsForBytes(bytes);
	}

	public String encode(UUID value) {
		return encoding.encode(bytes(Verify.notNull(value))).toString();
	}

	public UUID decode(String value) {
		char[] chars = Verify.notNull(value).toCharArray();
		Verify.verify(chars.length<=maxSymbols);
		return uuid(encoding.decode(chars,bytes));
	}

	public static byte[] bytes(UUID value) {
		if( value==null ) return null;
		byte[] r = new byte[bytes];

		ByteBuffer.wrap(r)
			.order(ByteOrder.BIG_ENDIAN)
			.putLong(value.getMostSignificantBits())
			.putLong(value.getLeastSignificantBits())
		;

		return r;
	}

	public static UUID uuid(byte[] value) {
		if( value==null ) return null;
		Verify.verify(value.length==bytes);

		ByteBuffer b = ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN);
		long msb = b.getLong();
		long lsb = b.getLong();

		return new UUID(msb,lsb);
	}

}
