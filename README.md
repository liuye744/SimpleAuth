# SimpleAuth
## FEATURES
This is a lightweight authority authentication plug-in based on SpringBoot. Suitable for lightweight projects and gradual projects.

## USAGE
When you want to control all the methods or one of the methods in a Controller, add the annotation @IsAuthor to the Controller or one of the methods.
The whole authority check plug-in is based on @IsAuthor
@IsAuthor mainly has two parameters, value and authentication, which are the permission value to enter the method and the class (which can be multiple functions) for permission verification.
```
<dependency>
    <groupId>io.github.liuye744</groupId>
    <artifactId>simpleAuth-spring-boot-starter</artifactId>
    <version>0.1.1.RELEASE</version>
</dependency>
```
### 1.You can do this when you just want to verify the parameters in HeetServlertRequest.
 Create a class extends AutoAuthHandler.
 ```
 //The following procedure is to verify whether the key parameter in the request is equal to 114514. 
 //Returns true to prove that the permissions are verified, and returns false to throw PermissionsException.
 //Of course, you can do more complicated operations.
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
 Then add the @IsAuthor annotation to the Controller, or the method in it. 
 Then all requests in the Controller, or annotated requests, will be checked for related permissions.
 ```
@Controller
@IsAuthor(authentication = KeyAutoAuthHandler.class)
public class MyController {
}
 ```
 Note: If you have more than one AutoAuthHandler, you can write the comment @IsAuth (authentication = {KeyAutoAuthHandler1.class, KeyAutoAuthHandler2.class}) like this.
 These classes will run permission checks in sequence.
 Or add a class that inherits from AutoAuthHandlerChain and add all Handler to it.
 ```
 public class MyHandlerChain extends AutoAuthHandlerChain {
    @Override
    public void addChain() {
        addLast(KeyAutoAuthHandler1.class);
        addLast(KeyAutoAuthHandler2.class);
    }
}
//Use @isAuthor (authentications = MyHandlerChain.class) when adding comments.
 ```
 
 ### 2.When you want to add permission values to a Controller or method
 ```
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
When running to the say () method, because the annotation @IsAutor is added to MyController, the IsAuthor method in AddPermissionKeyHandler will be run first. 
In this method, the string "visitor" is added to the user's permission, so it will run through and you can receive Hello World.
Permission list added in AddPermissionKeyHandler can be added through database query, so this plug-in can use RBAC for permission control. 
You can also use simple authentication parameters in the test phase to seamlessly access RBAC later.

Note: You can use this.getPermissions(request) when you want to get Permisson List in Handler.
