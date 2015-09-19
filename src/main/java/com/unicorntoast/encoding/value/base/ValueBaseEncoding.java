package com.unicorntoast.encoding.value.base;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

import com.unicorntoast.encoding.value.util.Chars;
import com.unicorntoast.encoding.value.util.Charsets;
import com.unicorntoast.encoding.value.util.Verify;

public class ValueBaseEncoding {
	
	// defs
	
	private static final BigInteger bi_256 = BigInteger.valueOf(256);
	
	public static final String symbols_base58_bitcoin = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
	public static final String symbols_base58_ripple = "rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz";
	public static final String symbols_base58_flickr = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
	
	public static ValueBaseEncoding base58_bitcoin = new ValueBaseEncoding(symbols_base58_bitcoin.toCharArray());
	public static ValueBaseEncoding base58_ripple = new ValueBaseEncoding(symbols_base58_ripple.toCharArray());
	public static ValueBaseEncoding base58_flickr = new ValueBaseEncoding(symbols_base58_flickr.toCharArray());
	
	// state
	
	private final char[] symbols;
	private final BigInteger bi_base;
	
	public final int base;
	 
	// impl
	
	public ValueBaseEncoding(char[] symbols) {
		this.symbols = Verify.notNull(symbols);
		Verify.verify(symbols.length>1 && symbols.length<256);
		this.base = symbols.length;
		this.bi_base = BigInteger.valueOf(base);
	}
	
	public int symbolsForBytes(int bytes) {
		return logCeil(bi_256.pow(bytes),bi_base);
	}

	public int bytesForSymbols(int symbols) {
		return logCeil(bi_base.pow(symbols),bi_256);
	}

	public int completeBytesForSymbols(int symbols) {
		if( symbols==0 ) return 0;
		return logFloor(bi_base.pow(symbols),bi_256);
	}
	
	public EncodedValue encode(byte[] src,int minSymbols) {
		Verify.notNull(src);
		Verify.verify(minSymbols>=0);
		
		int[] b = new int[minSymbols];
		
		for(int i=0;i<src.length;++i) {
			int v = src[i]&0xff;
			
			for(int j=0;j<b.length||v>0;++j) {
				if( j==b.length )
					b = Arrays.copyOf(b,b.length+1);
				
				v += b[j]*256;
				b[j] = v%base;
				v /= base;
			}
		}
		
		char[] r = new char[b.length];
		for(int i=0;i<b.length;++i)
			r[r.length-i-1] = symbols[b[i]];
		
		return new EncodedValue(symbols,r);
	}
	
	public byte[] decode(char[] src,int minBytes) {
		Verify.notNull(src);
		Verify.verify(minBytes>=0);
		
		int[] b = new int[minBytes];
		
		for(int i=0;i<src.length;++i) {
			int v = lookup(src[i]);
			
			for(int j=0;j<b.length||v>0;++j) {
				if( j==b.length )
					b = Arrays.copyOf(b,b.length+1);
				
				v += b[j]*base;
				b[j] = v%256;
				v /= 256;
			}
		}
		
		byte[] r = new byte[b.length];
		for(int i=0;i<b.length;++i)
			r[r.length-i-1] = (byte)b[i];

		return r;
	}
	
	public EncodedValue encode(byte[] src) { return encode(src,1); }
	public EncodedValue encode(String src) { return encode(Verify.notNull(src).getBytes(Charsets.utf8)); }
	
	public byte[] decode(char[] src) { return decode(src,0); }
	public byte[] decode(String src) { return decode(Verify.notNull(src).toCharArray()); }
	
	// structs
	
	public static class EncodedValue {
		
		private final char[] symbols;
		private final char[] encoded;
		
		private EncodedValue(char[] symbols,char[] encoded) {
			this.symbols = symbols;
			this.encoded = encoded;
		}
		
		public char[] chars() {
			return Arrays.copyOf(encoded,encoded.length);
		}
		
		public byte[] bytes() {
			return new String(encoded).getBytes(Charsets.utf8);
		}
		
		public EncodedValue stripLeadingZeros() {
			int i=0;
			
			while( i<(encoded.length-1) && encoded[i]==symbols[0] )
				++i;
			
			if( i==0 )
				return this;
			
			return new EncodedValue(symbols,Arrays.copyOfRange(encoded,i,encoded.length));
		}
		
		@Override
		public String toString() {
			return new String(encoded);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(encoded,symbols);
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this == obj)
				return true;
			if(obj == null)
				return false;
			if(getClass() != obj.getClass())
				return false;
			EncodedValue other = (EncodedValue)obj;
			if(!Arrays.equals(encoded,other.encoded))
				return false;
			if(!Arrays.equals(symbols,other.symbols))
				return false;
			return true;
		}
		
	}

	// pimpl
	
	private int lookup(char symbol) {
		int i = Chars.indexOf(symbols,symbol);
		if( i==-1 ) throw new IllegalArgumentException();
		return i;
	}
	
	private static int logCeil(BigInteger value,BigInteger base) {
		if( value.compareTo(BigInteger.ONE)<=0 ) return 0;
		
		int r=0;
		boolean roundUp = false;
		
		
		while( value.compareTo(base)>=0 ) {
			BigInteger[] t = value.divideAndRemainder(base);
			value = t[0];
			if( !roundUp && t[1].compareTo(BigInteger.ZERO)>0 )
				roundUp = true;
			
			++r;
		}
		
		if( roundUp || value.compareTo(BigInteger.ZERO)>0 )
			++r;
		
		return r;
	}
	
	private static int logFloor(BigInteger value,BigInteger base) {
		if( value.compareTo(BigInteger.ONE)<=0 ) return 0;
		
		int r=0;
		while( value.compareTo(base)>=0 ) {
			value = value.divide(base);
			++r;
		}
		
		return r;
	}
	
}
