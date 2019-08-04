package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
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
    private SearchService searchService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "leyou.create.index.queue",declare = "true"),
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
        this.searchService.createIndex(id);
    }


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "leyou.create.index.queue",declare = "true"),
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
        this.searchService.deleteIndex(id);
    }
}
