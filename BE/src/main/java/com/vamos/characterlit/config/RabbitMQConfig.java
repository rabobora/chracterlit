package com.vamos.characterlit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
public class RabbitMQConfig {
    // 큐 이름 정의
    public static final String QUEUE_NAME_1 = "durable_queue_1";
    public static final String QUEUE_NAME_2 = "durable_queue_2";

    // 교환기 이름
    public static final String EXCHANGE_NAME = "direct_exchange";

    // 라우팅 키
    public static final String ROUTING_KEY_1 = "queue_1_routing_key";
    public static final String ROUTING_KEY_2 = "queue_2_routing_key";

    @Bean
    Queue queue1() {
        return new Queue(QUEUE_NAME_1, true, false, false);
    }

    @Bean
    Queue queue2() {
        return new Queue(QUEUE_NAME_2, true, false, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding1(DirectExchange exchange, Queue queue1) {
        return BindingBuilder.bind(queue1).to(exchange).with(ROUTING_KEY_1);
    }

    @Bean
    Binding binding2(DirectExchange exchange, Queue queue2) {
        return BindingBuilder.bind(queue2).to(exchange).with(ROUTING_KEY_2);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(EXCHANGE_NAME);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
