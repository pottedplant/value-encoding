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
