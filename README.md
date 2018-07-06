# sqlCleric
SQL shouldn't be generated by software like orm! And, what's more, a proper helper is needed for languages is needed to reduce lot of transfer faults.

Now has published to maven central repository
```grovvy
    compile 'com.clearso:sql-cleric:1.0.3'
```

## Update Log(v1.0.3):

- add clone method to HermesDbUnit, make it available to create instance by offset.

```java
HermesDbUnit du = HermesDbTaskBuilder.defaultConf()
                    .Get(TABLE_OFFSET)
                    .clone();
```

## Update Log(v1.0.2):

- replace return value of nullCond from void to T(Orignal CURD), So stream usage is supported.

```java
String sql = Hh.retrive().Src(...).Col(...).NullCond().toSql();
```


