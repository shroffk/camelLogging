package camel;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class CSStudioLog {

	public static void main(String[] args) {
		CamelContext context = new DefaultCamelContext();
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://beast.cs.nsls2.local:61616");

		context.addComponent("cs-studio-log", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
		try {
			context.addRoutes(new RouteBuilder() {

				public void configure() {
					// Topic
					from("cs-studio-log:topic:cs-studio_log").to(
							"elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=cs-studio_log&indexType=cs-studio_log");
				}

			});
			context.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
