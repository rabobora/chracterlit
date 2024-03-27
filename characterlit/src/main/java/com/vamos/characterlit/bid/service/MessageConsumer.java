package com.vamos.characterlit.bid.service;

import com.rabbitmq.client.Channel;
import com.vamos.characterlit.bid.request.BidMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageConsumer {
    private final NowbidService nowbidService;

    @RabbitListener(queues = {"durable_queue_1"}, concurrency = "1", ackMode = "MANUAL")
    public void getMessage1(BidMessageDTO messageDTO, Channel channel,
                            @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.info("Received Message queue_1");
        try {
            nowbidService.update(messageDTO);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            try {
                log.error("Error processing message", e);
                channel.basicAck(tag, false);
            } catch (IOException ex) {
                log.error("Error acknowledging message", ex);
            }
        }
    }

    @RabbitListener(queues = {"durable_queue_2"}, concurrency = "1", ackMode = "MANUAL")
    public void getMessage2(BidMessageDTO messageDTO, Channel channel,
                            @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.info("Received Message queue_2");
        try {
            nowbidService.update(messageDTO);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            try {
                log.error("Error processing message", e);
                channel.basicAck(tag, false);
            } catch (IOException ex) {
                log.error("Error acknowledging message", ex);
            }
        }
    }

}
