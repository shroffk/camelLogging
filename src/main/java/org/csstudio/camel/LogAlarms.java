package org.csstudio.camel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.Logger;

public class LogAlarms {
	private static final Logger logger = Logger.getLogger(LogAlarms.class.getName());

	public static void main(String[] args) {

		Properties prop = new Properties();
		try {
			prop.load(LogAlarms.class.getResourceAsStream("/log_alarms.properties"));
		} catch (IOException e1) {
			logger.warn("Failed to read configurations", e1);
		}
		String jms_uri = prop.getProperty("best_jms", "tcp://beast.cs.nsls2.local:61616");
		String elastic_uri = prop.getProperty("elastic_uri", "webdev.cs.nsls2.local");
		String component_name = prop.getProperty("component_name", "alarm_jms");
		String topics = prop.getProperty("topic", "NSLS2_ENG");
		List<String> alarm_topics = Arrays.asList(topics.split(",")).stream().map(String::trim).collect(Collectors.toList());
				
		CamelContext context = new DefaultCamelContext();
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jms_uri);

		context.addComponent(component_name, JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		try {
			context.addRoutes(new RouteBuilder() {
				public void configure() {
					// Production server
					for (String alarm_topic : alarm_topics) {
						String from = component_name+":topic:"+alarm_topic;
						String to = "elasticsearch://elasticsearch?ip="+elastic_uri+"&operation=INDEX&indexName="+alarm_topic.toLowerCase()+"&indexType=BEAST";
						// Index the talk messages
						from(from+"_TALK")
						.to(to);
						// Filter and index the server messages
						from(from+"_SERVER")
								.filter().simple("${body[TEXT]} != \"IDLE\"")
								.filter().simple("${body[TEXT]} != \"IDLE_MAINTENANCE\"")
								.to(to);
						// Index the client messages
						from(from+"_CLIENT")
						.to(to);

					}

//					// Test Topics
//					from("test-jms:topic:LOG").filter(body().not().contains("kunal")).to(
//							"elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=topic_log&indexType=topic_log");
//					from("test-jms:topic:HUMAN_SERVER").filter().simple("${body[TEXT]} != \"IDLE\"").to(
//							"elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=topic_human_server&indexType=topic_human");
//					from("test-jms:topic:HUMAN_CLIENT").to(
//							"elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=topic_human_client&indexType=topic_human");
//					from("test-jms:topic:HUMAN_TALK").to(
//							"elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=topic_human_talk&indexType=topic_human");
//					from("test-jms:topic:WRITE").to(
//							"elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=topic_write&indexType=topic_write");

				}
			});
			context.start();

//			// Test Producer
//			ProducerTemplate template = context.createProducerTemplate();
//			for (int i = 0; i < 10; i++) {
//				try {
//					template.sendBody("test-jms:topic:LOG", "{\"kunal\":\"Test Message - " + i + "\"}");
//					template.sendBody("test-jms:topic:LOG", "{\"name\":\"Test Message - " + i + "\"}");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			template.stop();
//			Thread.sleep(1000);
//			context.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
