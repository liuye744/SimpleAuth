# SimpleAuth
👉[English Document](/README.md)
# 特性

这是一款基于SpringBoot的轻量化的权限校验和访问控制的框架。适用于轻量级以及渐进式的项目。


# 限制访问频率-用法

可以通过注解快速实现类似10分钟内仅可以搜索3次，以及N分钟尝试登陆多次即被禁止一小时等类似功能。

## 第一步：添加Maven依赖

```xml
<dependency>
    <groupId>io.github.liuye744</groupId>
    <artifactId>simpleAuth-spring-boot-starter</artifactId>
    <version>0.3.8.RELEASE</version>
</dependency>
```

## 第二步：为Controller添加注解

可以为整个Controller添加，也可以为Controller中单个方法添加。访问失败则会抛出AccessIsRestrictedException

### 注解参数说明

```java
public @interface IsLimit {
    //限制访问次数
    int value() default 100;

    //记录限制的时间
    int seconds() default 300;

    //超过访问限制之后被禁止的时间
    int ban() default 0;

    //用户标志的生成策略
    Class<? extends SignStrategic> signStrategic() default DefaultSignStrategic.class;

    //访问控制的接口标志的名称(默认为接口的全限定名)
    String item() default "";

    //此次访问是否有效（是否被记录）
    Class<? extends EffectiveStrategic> effectiveStrategic() default DefaultEffectiveStrategic.class;

    //是否在Controller返回后运行effectiveStrategic
    boolean judgeAfterReturn() default true;
}
```

### 用例1：10分钟内只允许访问5次，超过之后将会被禁止10分钟

```java
@RestController
public class MyController {
    @GetMapping("say")
    @IsLimit(value = 5, seconds = 600, ban = 600)
    public String say(){
        return "Hello World";
    }
}
```

### 用例2：同一个接口传递不同的参数访问次数分别计算
传递的参数不同访问限制不同(例如想要每隔一段时间每个资源只能点赞N次)

```java
@RestController
public class MyController {
    @GetMapping("say")
    @IsLimit(signStrategic = MySignStrategic.class)
    public String say(String str){
        return "Hello World";
    }
}
public class MySignStrategic extends SignStrategic {
    @Override
    public String sign(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        final Object[] args = joinPoint.getArgs();
        final Signature signature = joinPoint.getSignature();
        //将参数拼接到用户sign中，保证每个用户传递不同的参数标志不相同
        StringBuilder sb = new StringBuilder();
        sb.append(signature);
        for (Object arg : args) {
            sb.append(arg.toString());
        }
        System.out.println(sb);
        return sb.toString();
    }
}
```

### 用例3：当返回“success”时才记录数据

```java
@RestController
public class MyController {
    @GetMapping("say")
    @IsLimit(effectiveStrategic = MyEffectiveStrategic.class)
    public String say(String str){
        if (str.length()>3 && str.length()<12){
            return "success";
        }else {
            return "fail";
        }
    }
}

public class MyEffectiveStrategic extends EffectiveStrategic {
    @Override
    public Boolean effective(HttpServletRequest request, ProceedingJoinPoint joinPoint, Object result) {
        String myResult = (String)result;
        return "success".equals(myResult);
    }
}
```

### 用例4：自定义访问控制

```java
//全局访问控制
@Component
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String addr = request.getRemoteAddr();
        //以用户的地址作为标志，每5分钟(300s)只允许访问2次，超过之后被禁止10分钟
        //addRecord方法调用后可以访问则返回true,禁止访问返回false
        return LimitInfoUtil.addRecord("GLOBAL_ACCESS_CONTROL", addr, 2, 300, 600);
    }
}
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/*");
    }
}
```

# 权限校验-用法

当你想要控制Controller中所有的方法或其中一个，可以添加`@IsAuthor`注解在方法或Controller类上。

## 第一步：添加maven依赖

```xml
<dependency>
    <groupId>io.github.liuye744</groupId>
    <artifactId>simpleAuth-spring-boot-starter</artifactId>
    <version>0.3.8.RELEASE</version>
</dependency>
```

## 第二步：验证权限

### 用例1：通过HttpServlertRequest对象验证参数

创建一个类继承AutoAuthHandler，并重写IsAuthor方法

```java
//验证请求参数中是否携带key为114514的参数. 
//返回true则表示验证成功，返回false表示验证失败将会抛出PermissionsException
//当然你也可以进行更复杂的操作
@Component
public class KeyAutoAuthHandler extends AutoAuthHandler {
   @Override
   public boolean isAuthor(HttpServletRequest request, String permission) {
       final String key = request.getParameter("key");
       if ("114514".equals(key)){
           return true;
       }
       return false;
   }
}
```

然后将`@IsAuthor `注释添加到 Controller 或其中的方法。然后，将检查 Controller 中的所有请求或带注释的请求是否具有相关权限。

```java
@Controller
@IsAuthor(authentication = KeyAutoAuthHandler.class)
public class MyController {
}
```

注意: 如果你有多个` AutoAuthHandler`，你可以像这样写注释`@IsAuth (authentication = { KeyAutoAuthHandler1.class，KeyAutoAuthHandler2.class })`。authentication参数也可以写 Bean 名称。这些类将按顺序执行权限检查。或者创建 继承`AutoAuthHandlerChain` 的类，并将所有 Handler 添加到该类中。

```java
@Component
public class MyHandlerChain extends AutoAuthHandlerChain {
   @Override
   public void addChain() {
       addLast(KeyAutoAuthHandler1.class);
       addLast(KeyAutoAuthHandler2.class);
       addLast(KeyAutoAuthHandler3.class);
   }
}
//添加注解时使用 @isAuthor(authentications = MyHandlerChain.class)
```

### 用例2：基于角色的权限校验

```java
@RestController
@IsAuthor(authentication = AddPermissionKeyHandler.class)
public class MyController {
   @IsAuthor("visitor")
   @GetMapping("say")
   public String say(){
       return "Hello World";
   }
   @IsAuthor("vip")
   @GetMapping("eat")
   public String eat(){
       return "eat";
   }
}
@Component
public class AddPermissionKeyHandler extends AutoAuthHandler {
   @Override
   public boolean isAuthor(HttpServletRequest request, String permission) {
       ArrayList<String> permissions = new ArrayList<>();
       //或者查询数据库为当前请求添加角色key
       permissions.add("visitor");
       this.setPermissions(request,permissions);
       //查询成功，放行
       return true;
   }
}
```

当请求`/say`时，由于注释`@IsAutor` 被添加到 MyController类上，`AddPermisonKeyHandler` 中的 IsAuthor 方法将首先运行。在这个方法中，字符串“visitor”被添加到用户的权限中，因此它将会验证通过并且您可以接收 HelloWorld。
当请求`/say`时，由于权限列表中没有“vip”则会请求失败，抛出`PermissionsException`异常，可以通过全局异常处理完成权限校验