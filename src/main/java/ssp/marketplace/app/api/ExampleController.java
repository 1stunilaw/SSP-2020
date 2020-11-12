package ssp.marketplace.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ssp.marketplace.app.service.UserService;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1")
public class ExampleController {


    public RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    public ExampleController(
            UserService userService, RequestMappingHandlerMapping requestMappingHandlerMapping
    ) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @GetMapping("/home")
    public String home(){
        return "Home";
    }

    @GetMapping("/user")
    public String user(){
        return "User";
    }

    @GetMapping("/admin")
    public String admin(){
        return "Admin";
    }

    @RequestMapping("/endpoints")
    @ResponseBody
    public Object showEndpointsAction() throws SQLException
    {
        return requestMappingHandlerMapping.getHandlerMethods().keySet().stream().map(t ->
                (t.getMethodsCondition().getMethods().isEmpty() ? "GET" : t.getMethodsCondition().getMethods().toArray()[0]) + " " +
                        t.getPatternsCondition().getPatterns().toArray()[0]
        ).toArray();
    }
}
