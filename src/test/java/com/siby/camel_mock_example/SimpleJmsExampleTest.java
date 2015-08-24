package com.siby.camel_mock_example;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
//TODO fix it
public class SimpleJmsExampleTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:direct:start")
    protected MockEndpoint directStart;

    @EndpointInject(uri = "mock:direct:foo")
    protected MockEndpoint directFoo;

    @EndpointInject(uri = "mock:log:foo")
    protected MockEndpoint logFoo;

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint result;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    private CamelContext context = new DefaultCamelContext();
    private SimpleJmsExample simpleJmsExample;

    @Before
    public void init() throws Exception {
        simpleJmsExample = new SimpleJmsExample(context);
    }

    @Test
    public void testAdvisedMockEndpoints() throws Exception {
        // advice the first route using the inlined AdviceWith route builder
        // which has extended capabilities than the regular route builder
        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                // mock all endpoints
                mockEndpoints();
            }
        });

        directStart.expectedBodiesReceived("Hello World");
        directFoo.expectedBodiesReceived("Hello World");
        logFoo.expectedBodiesReceived("Bye World");
        result.expectedBodiesReceived("Bye World");

        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();

        // additional test to ensure correct endpoints in registry
        assertNotNull(context.hasEndpoint("direct:start"));
        assertNotNull(context.hasEndpoint("direct:foo"));
        assertNotNull(context.hasEndpoint("log:foo"));
        assertNotNull(context.hasEndpoint("mock:result"));
        // all the endpoints was mocked
        assertNotNull(context.hasEndpoint("mock:direct:start"));
        assertNotNull(context.hasEndpoint("mock:direct:foo"));
        assertNotNull(context.hasEndpoint("mock:log:foo"));
    }

//    private MockEndpoint getMockEndpoint(String route) {
//        MockEndpoint resultEndpoint = context.resolveEndpoint(route, MockEndpoint.class);
//        resultEndpoint.setAssertPeriod(5000);
//        return resultEndpoint;
//    }
}