package com.leyou.user.service;

import com.leyou.common.utils.CodeGenerator;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static String KEY_PREFIX="user:code:phone:";

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                return null;
        }
        return this.userMapper.selectCount(user)==0;
    }

    /**
     * 发送消息到短信服务并将验证码保存在redis
     * @param phone
     * @return
     */
    public Boolean sendVerifyCode(String phone){
        String code = CodeGenerator.generateCode(6);
        try {
            Map<String,String> msg = new HashMap<>();
            msg.put("phone",phone);
            msg.put("code",code);
            amqpTemplate.convertAndSend("leyou.sms.exchange","sms.verify.code",msg);
            redisTemplate.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);
            return true;
        } catch (AmqpException e) {
            logger.error("短信发送失败，phone:{},code:{}",phone,code);
            return false;
        }
    }

    public Boolean register(User user,String code){
        String key = KEY_PREFIX + user.getPhone();
        // 从redis取出验证码
        String codeCache = (String) redisTemplate.opsForValue().get(key);
        // 检查验证码是否正确
        if (!code.equals(codeCache)) {
            // 不正确，返回
            return false;
        }
        user.setId(null);
        user.setCreated(new Date());
        // 生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        // 对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        // 写入数据库
        boolean boo = this.userMapper.insertSelective(user) == 1;

        // 如果注册成功，删除redis中的code
        if (boo) {
            try {
                this.redisTemplate.delete(key);
            } catch (Exception e) {
                logger.error("删除缓存验证码失败，code：{}", code, e);
            }
        }
        return boo;
    }

    /***
     * 根据用户名获取用户
     * @param name
     * @param passwd
     * @return
     */
    public User queryUser(String name,String passwd){
        User record = new User();
        record.setUsername(name);

        User user = this.userMapper.selectOne(record);
        if(null==user)
            return null;
        if(!user.getPassword().equals(CodecUtils.md5Hex(passwd,user.getSalt())))
            return null;
        return user;
    }
}
