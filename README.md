# SimpleAuth
👉[中文文档](/SimpleAuth/blob/master/README.zh.md)
## FEATURES
This is a lightweight framework for permission validation and access control based on SpringBoot. It is suitable for lightweight and progressive projects.

## Usage - Access Rate Limiting
You can quickly implement features like allowing only 3 searches within 10 minutes, or blocking access for an hour after multiple login attempts within N minutes using annotations.

### Step 1: Add Maven Dependency
```xml
<dependency>
    <groupId>io.github.liuye744</groupId>
    <artifactId>simpleAuth-spring-boot-starter</artifactId>
    <version>0.3.8.RELEASE</version>
</dependency>
```
### Step 2: Add Annotations to Controllers
Annotations can be added to the entire controller or individual methods within the controller. Access failures will result in an AccessIsRestrictedException being thrown.
#### Annotation Parameter Explanation
```java
public @interface IsLimit {
 // Limit of access count
 int value() default 100;
 // Time for which restrictions are recorded
 int seconds() default 300;
 // Time for which access is banned after exceeding limits
 int ban() default 0;
 // User sign generation strategy
 Class<? extends SignStrategic> signStrategic() default DefaultSignStrategic.class;
 // Name of the interface flag for access control (default is fully qualified name of the interface)
 String item() default "";
 // Whether the access for this request is valid (whether it is recorded)
 Class<? extends EffectiveStrategic> effectiveStrategic() default DefaultEffectiveStrategic.class;
 // Validate access records after Controller returns
 boolean judgeAfterReturn() default true;
}
```
#### Example 1: Allowing only 5 accesses within 10 minutes, followed by a 10-minute ban if exceeded
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
#### Example 2: Counting accesses separately for the same endpoint with different parameters
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
#### Example 3: Recording data only when request is successful
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
#### Example 4: Custom Access Control
```java
// Global access control
@Component
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String addr = request.getRemoteAddr();
        // Allowing only 2 accesses every 5 minutes (300s) based on user's address, followed by a 10-minute ban if exceeded
        // Return true from addRecord method indicates access is allowed, return false indicates access is denied
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

## Usage - Authorization Validation
When you want to control all methods within a Controller or specific methods, you can add the @IsAuthor annotation to the method or the Controller class.

### Step 1: Add Maven Dependency
```xml
<dependency>
    <groupId>io.github.liuye744</groupId>
    <artifactId>simpleAuth-spring-boot-starter</artifactId>
    <version>0.3.8.RELEASE</version>
</dependency>
```
### Step 2: Validate Permissions
#### 1.You can do this when you just want to verify the parameters in HeetServlertRequest.
Create a class that extends AutoAuthHandler and override the isAuthor method
 ```java
// Validate whether the request parameter contains a key of 114514.
// Return true for successful validation, return false to throw PermissionsException
// You can perform more complex operations as well
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
Then add the @IsAuthor annotation to the Controller or its methods. It will then check whether all requests within the Controller or annotated requests have the required permissions.
 ```java
@Controller
@IsAuthor(authentication = KeyAutoAuthHandler.class)
public class MyController {
}
 ```
Note: If you have multiple AutoAuthHandlers, you can write the annotation like this: @IsAuth (authentication = { KeyAutoAuthHandler1.class, KeyAutoAuthHandler2.class }). The authentication parameter can also be the name of a bean. These classes will perform permission checks in the order they are defined. Alternatively, you can create a class that extends AutoAuthHandlerChain and add all handlers to that class.
 ```java
 public class MyHandlerChain extends AutoAuthHandlerChain {
    @Override
    public void addChain() {
        addLast(KeyAutoAuthHandler1.class);
        addLast(KeyAutoAuthHandler2.class);
    }
}
//Use @isAuthor (authentications = MyHandlerChain.class) when adding comments.
 ```
 
 ### 2.Role-Based Access Control
 ```java
@RestController
@IsAuthor(authentication = AddPermissionKeyHandler.class)
public class MyController {
    @IsAuthor("visitor")
    @GetMapping("say")
    public String say(){
        return "Hello World";
    }
}
@Component
public class AddPermissionKeyHandler extends AutoAuthHandler {
    @Override
    public boolean isAuthor(HttpServletRequest request, String permission) {
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("visitor");
        this.setPermissions(request,permissions);
        return true;
    }
}
 ```
When requesting /say, since the @IsAuthor annotation is added to the MyController class, the isAuthor method in AddPermisonKeyHandler will run first. In this method, the string "visitor" is added to the user's permissions, so it will be validated successfully and you will receive "Hello World". When requesting /eat, since the permission list does not contain "vip", the request will fail and throw a PermissionsException which can be handled through global exception handling to complete the authorization validation.
