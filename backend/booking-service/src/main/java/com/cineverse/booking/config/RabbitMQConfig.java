package com.cineverse.booking.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BOOKING_QUEUE      = "booking.notification.queue";
    public static final String EXCHANGE           = "cineverse.exchange";
    public static final String BOOKING_ROUTING_KEY = "booking.confirmed";

    @Bean
    public Queue bookingNotificationQueue() {
        return new Queue(BOOKING_QUEUE, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding bookingBinding(Queue bookingNotificationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(bookingNotificationQueue).to(exchange).with(BOOKING_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
