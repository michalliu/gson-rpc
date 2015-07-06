A simple json rpc implementation besed on google gson.
```
public class StudentService {

   public Student getStudent(long id) {
       // ignore implementation 
   }

   public void addStudent(Student student) {
       // ignore implementation 
   }
}

public class Student {
   private String name;
   private Integer age;

   // get & set
}
```

Client code:
```
String url = "http://yourapp/services/studentService.getStudent";
JsonInvoker invoker = new SimpleJsonInvoker();
Student student = invoker.invoke(url, 100L, Student.class);

Gson gson = // .. use your gson to convert Json & Object.
JsonInvoker invoker = new SimpleJsonInvoker(new SimpleGsonConverter(gson));

Teacher teacher = invoker.invoke("http://any.url.you.wanted", someObject, Teacher.class);
// convert 'someObject' to json by the Gson you provided, 
// post it to the specified url, and convert the response json to Teacher.
```
Server code:
```
<servlet>
   <servlet-name>gsonExporter</servlet-name>
   <servlet-class>com.google.code.gson.rpc.SpringMultiJsonServiceExporter</servlet-class>
</servlet>
<servlet-mapping>
   <servlet-name>gsonExporter</servlet-name>
   <url-pattern>/services/*</url-pattern>
</servlet-mapping>

// your spring context
<bean id="jsonConverter" class="com.google.code.gson.rpc.GsonConverter" />
<bean id="studentService" class="your.StudentService"/ >
```


