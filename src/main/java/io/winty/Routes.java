package io.winty;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import io.winty.dto.Request;

public class Routes extends RouteBuilder {
    
    private final Set<Request> data = Collections.synchronizedSet(new LinkedHashSet<>());
    
    public Routes() {
        data.add(new Request("1", "teste"));
        data.add(new Request("2", "teste2"));
    }

    @Override
    public void configure() throws Exception {
        
        restConfiguration().bindingMode(RestBindingMode.auto);
        
        rest("/users")
            .get()
                .to("direct:listUsers")
            
            .post()
                .type(Request.class)
                .to("direct:addUser");
            
        from("direct:listUsers")
            .setBody().constant(data);
        
        from("direct:addUser")
            .process()
                .body(Request.class, data::add)
            .setBody()
                .constant(data);
            
    }
    
}
