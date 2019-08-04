package com.leyou.goodsweb.listener;

import com.leyou.goodsweb.service.GoodHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {
    @Autowired
    private GoodHtmlService goodHtmlService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "leyou.create.web.queue",declare = "true"),
                    exchange = @Exchange(
                            value = "leyou.item.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = {"item.insert","item.update"}
            )
    )
    public void listenCreate(Long id) throws Exception{
        if(null==id){
            return;
        }
        this.goodHtmlService.createHtml(id);
    }


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "leyou.create.web.queue",declare = "true"),
                    exchange = @Exchange(
                            value = "leyou.item.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = "item.delete"
            )
    )
    public void listenDelete(Long id){
        if(null==id){
            return;
        }
        this.goodHtmlService.deleteHtml(id);
    }
}
