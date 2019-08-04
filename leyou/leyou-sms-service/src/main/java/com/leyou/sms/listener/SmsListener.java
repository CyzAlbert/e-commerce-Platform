package com.leyou.sms.listener;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.leyou.sms.configuration.SmsProperties;
import com.leyou.sms.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {
    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private SmsProperties prop;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "leyou.sms.queue",declare = "true"),
                    exchange = @Exchange(
                            value = "leyou.sms.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = "sms.verify.code"
            )
    )
    public void listenSms(Map<String,String> msg) throws Exception{
        if(null==msg || msg.size()==0)
            return;
        String phone = msg.get("phone");
        String code = msg.get("code");
        if(StringUtils.isBlank(phone) || StringUtils.isBlank(code))
            return;
        try {
            SendSmsResponse resp = this.smsUtils.sendSms(phone,code,prop.getSignName(),prop.getVerifyCodeTemplate());
        } catch (ClientException e) {
            throw e;
        }
    }
}
