package camel;

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

public class LogAlarms {

	public static void main(String[] args) {
		CamelContext context = new DefaultCamelContext();
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://beast.cs.nsls2.local:61616");

		context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		try {
			context.addRoutes(new RouteBuilder() {
				public void configure() {
					// Production server
					from("test-jms:topic:NSLS2_ENG_TALK").to(
							"elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=alarms_eng&indexType=BEAST");
					from("test-jms:topic:NSLS2_ENG_SERVER").filter().simple("${body[TEXT]} != \"IDLE\"").filter()
							.simple("${body[TEXT]} != \"IDLE_MAINTENANCE\"")
							.to("elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=alarms_eng&indexType=BEAST");
					from("test-jms:topic:NSLS2_ENG_CLIENT").to(
							"elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=alarms_eng&indexType=BEAST");

					from("test-jms:topic:NSLS2_OPR_TALK").to(
							"elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=alarms_opr&indexType=BEAST");
					from("test-jms:topic:NSLS2_OPR_SERVER").filter().simple("${body[TEXT]} != \"IDLE\"").filter()
							.simple("${body[TEXT]} != \"IDLE_MAINTENANCE\"")
							.to("elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=alarms_opr&indexType=BEAST");
					from("test-jms:topic:NSLS2_OPR_CLIENT").to(
							"elasticsearch://elasticsearch?ip=webdev.cs.nsls2.local&operation=INDEX&indexName=alarms_opr&indexType=BEAST");

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
