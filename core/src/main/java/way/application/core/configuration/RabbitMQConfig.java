package way.application.core.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

	private static final String CHAT_QUEUE_NAME = "chat.queue";
	private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
	private static final String ROUTING_KEY = "*.room.*";

	@Value("${spring.rabbitmq.username}")
	private String rabbitUser;

	@Value("${spring.rabbitmq.password}")
	private String rabbitPw;

	@Value("${spring.rabbitmq.host}")
	private String rabbitHost;

	@Value("${spring.rabbitmq.virtual-host}")
	private String rabbitVh;

	//Queue 등록
	@Bean
	public Queue queue() {
		return new Queue(CHAT_QUEUE_NAME, true);
	}

	//Exchange 등록
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(CHAT_EXCHANGE_NAME);
	}

	// Exchange와 Queue바인딩
	@Bean
	public Binding binding(Queue queue, TopicExchange exchange){
		return BindingBuilder
			.bind(queue)
			.to(exchange)
			.with(ROUTING_KEY);
	}

	// RabbitMQ와의 메시지 통신을 담당하는 클래스
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}

	// RabbitMQ와의 연결을 관리하는 클래스
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setHost(rabbitHost);
		factory.setPort(5672);
		factory.setVirtualHost(rabbitVh);
		factory.setUsername(rabbitUser);
		factory.setPassword(rabbitPw);
		return factory;
	}

	// 메시지를 JSON형식으로 직렬화하고 역직렬화하는데 사용되는 변환기
	// RabbitMQ 메시지를 JSON형식으로 보내고 받을 수 있음
	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter(){
		return new Jackson2JsonMessageConverter();
	}
}