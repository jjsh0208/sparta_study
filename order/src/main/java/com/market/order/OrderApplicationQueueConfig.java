package com.market.order;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderApplicationQueueConfig {

    @Value("${message.exchange}")
    private String exchange;

    @Value("${message.queue.product}")
    private String queueProduct;

    @Value("${message.queue.payment}")
    private String queuePayment;

    @Bean public TopicExchange exchange(){ return  new TopicExchange(exchange);}

    @Bean public Queue queueProduct() { return  new Queue(queueProduct);}

    @Bean public Queue queuePayment() { return  new Queue(queuePayment);}

    // bind 어디로 어느 큐롷 보낼것인지
    // to  exchange
    // with에는 바인딩의 이름
    // 바인딩의 이름은 큐에 이름과 동일하게 사용하는것이 좋다.

    @Bean public Binding bindingProduct() { return BindingBuilder.bind(queueProduct()).to(exchange()).with(queueProduct);}
    @Bean public Binding bindingPayment() { return BindingBuilder.bind(queuePayment()).to(exchange()).with(queuePayment);}
}
