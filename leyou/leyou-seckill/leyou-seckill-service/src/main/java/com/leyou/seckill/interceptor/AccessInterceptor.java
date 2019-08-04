package com.leyou.seckill.interceptor;

import com.leyou.auth.entity.UserInfo;
import com.leyou.common.response.CodeMsg;
import com.leyou.common.utils.JsonUtils;
import com.leyou.seckill.access.AccessLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessList = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (null == accessList)
                return false;
            UserInfo userInfo = LoginInterceptor.getLoginUser();
            int second = accessList.seconds();
            int maxCount = accessList.maxCount();
            boolean needLogin = accessList.needLogin();
            String key = request.getRequestURI();
            if(needLogin){
                if(null == userInfo)
                    render(response, CodeMsg.LOGIN_ERROR);
                return false;
            }
            String count = redisTemplate.opsForValue().get(key);
            if(null==count)
                redisTemplate.opsForValue().set(key,"1",second, TimeUnit.SECONDS);
            else if(Integer.valueOf(count)<maxCount)
                redisTemplate.opsForValue().increment(key,1);
            else
                render(response,CodeMsg.ACCESS_LIMIT_REACHED);
            return true;
        }
        return super.preHandle(request,response,handler);
    }

    private void render(HttpServletResponse response,CodeMsg message) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        OutputStream out = response.getOutputStream();
        String str = JsonUtils.serialize(message);
        out.write(str.getBytes("utf-8"));
        out.flush();
        out.close();
    }
}
