## ognl-demo

ognl demo演示Arthas里的 watch表达式工作流程。用户可以参考修改，自己测试ognl表达式。


### 演示正常运行

```bash
./mvnw compile exec:java
```

结果：

```bash
AtEnter, conditionExpress: params[0] > 1, conditionResult: true
@ArrayList[
    @TestService[
    ],
    @Object[][
        @Integer[1000],
        @String[hello],
        @Student[
            id=@Long[1],
            name=@String[tom],
        ],
    ],
]
AtExceptionExit, conditionExpress: params[0] > 1, conditionResult: true
@ArrayList[
    @TestService[
    ],
    @Object[][
        @Integer[1000],
        @String[hello],
        @Student[com.example.ognl.Student@542a65b5],
    ],
    java.lang.IllegalArgumentException: error
	at com.example.ognl.TestService.test(TestService.java:12)
	at com.example.ognl.Demo.test(Demo.java:43)
	at com.example.ognl.Demo.main(Demo.java:20)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:254)
	at java.lang.Thread.run(Thread.java:748)
,
]
```

### 演示抛出异常

```bash
./mvnw compile exec:java -DexceptionCase=true
```

结果：

```bash
AtEnter, conditionExpress: params[0] > 1, conditionResult: true
@ArrayList[
    @TestService[
    ],
    @Object[][
        @Integer[1000],
        @String[hello],
        @Student[
            id=@Long[1],
            name=@String[tom],
        ],
    ],
]
AtExceptionExit, conditionExpress: params[0] > 1, conditionResult: true
@ArrayList[
    @TestService[
    ],
    @Object[][
        @Integer[1000],
        @String[hello],
        @Student[com.example.ognl.Student@bcb83b],
    ],
    java.lang.IllegalArgumentException: error
	at com.example.ognl.TestService.test(TestService.java:12)
	at com.example.ognl.Demo.test(Demo.java:43)
	at com.example.ognl.Demo.main(Demo.java:20)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:254)
	at java.lang.Thread.run(Thread.java:748)
,
]
```