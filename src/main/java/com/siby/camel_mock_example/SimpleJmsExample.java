package com.siby.camel_mock_example;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

public class SimpleJmsExample {

    private final CamelContext context;

    public SimpleJmsExample(CamelContext context) throws Exception {
        this.context = context;
        context.addRoutes(createRouteBuilder());
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").to("direct:foo").to("log:foo").to("mock:result");
                from("direct:foo").transform(constant("Bye World"));
            }
        };
    }
}
