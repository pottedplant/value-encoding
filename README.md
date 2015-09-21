# value-encoding

```java
UUIDEncoding encoding = UUIDEncoding.base58_ripple;

UUID uuid = UUID.fromString("c36f12cf-082d-493e-a412-79c8d8b75183");
String encoded = encoding.encode(uuid);
UUID decoded = encoding.decode(encoded);

System.out.println(uuid);
System.out.println(encoded);
System.out.println(decoded);
```

```console
c36f12cf-082d-493e-a412-79c8d8b75183
R35ScGfuZRQ77FnX8maSHx
c36f12cf-082d-493e-a412-79c8d8b75183
```

```java
FixedLengthEncoding encoding = new FixedLengthEncoding(ValueBaseEncoding.base58_ripple,20);

byte[] hash = MessageDigest.getInstance("SHA1")
	.digest("Hello World!".getBytes("UTF-8"))
;

String encoded = encoding.encode(hash);
byte[] decoded = encoding.decode(encoded);

System.out.println(BaseEncoding.base16().encode(hash));
System.out.println(encoded);
System.out.println(Arrays.equals(hash,decoded));
```

```console
2EF7BDE608CE5404E97D5F042F95F89F1C232871
rexUWNCZknktz16YY5u1ygscsvf2
true
```
