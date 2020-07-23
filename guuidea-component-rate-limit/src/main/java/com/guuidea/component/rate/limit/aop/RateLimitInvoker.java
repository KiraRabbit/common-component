package com.guuidea.component.rate.limit.aop;

import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.guuidea.component.rate.limit.annotation.GdRateLimiter;

/**
 * @Author: zhangsongyang
 * @Date: 2020/5/31 9:54
 */
@Component
@Aspect
public class RateLimitInvoker {

    @Value("${rate.limit.path:}")
    private String rateLimitPath ;

    private static final Logger logger = LoggerFactory.getLogger(RateLimitInvoker.class);

    private Map<String,String> pathMap = new HashMap<String, String>();

    /**
     * 令牌桶，key:类名.方法  value：QPS的值
     */
    private ConcurrentMap<String, RateLimiter> limitMap = new ConcurrentHashMap();

    @Around("@annotation(gdRateLimiter)")
    public Object injectLimit(final ProceedingJoinPoint point, GdRateLimiter gdRateLimiter) throws Throwable {
        double num = gdRateLimiter.number();
        //类名和方法名作为key，RateLimiter作为值，1秒中QPS限制为number
        String keyName = getMethod(point);
        RateLimiter limiter = null;
        if(!limitMap.containsKey(keyName)){
            //创建令牌桶
            double number = getNumber(num,keyName);
            limiter = RateLimiter.create(number);
            limitMap.put(keyName,limiter);
            logger.info("请求:{},创建令牌桶容量:{},成功.",keyName,number);
        }
        limiter = limitMap.get(keyName);
        if(!limiter.tryAcquire()){
            //logger.info("请求:{}过于频繁",keyName);
            return "fail";
        }
        return point.proceed();
    }

    private double getNumber(double number, String keyName){
        try {
            double value = pathMap.get(keyName) == null ? number : Double.valueOf((String)pathMap.get(keyName));
            number = value;
        }catch (Exception e){
            logger.error("获取自定义容量错误", e);
        }
        return number;
    }

    @PostConstruct
    public void initPath(){
        if(!StringUtils.isEmpty(rateLimitPath)){
            Map map = (Map)JSON.parse(rateLimitPath);
            if(map!=null){
                pathMap.putAll(map);
            }
        }
    }

    private String getMethod(ProceedingJoinPoint point) {
        Signature sig = point.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        String className = point.getTarget().getClass().getName();
        //Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        return className+"."+msig.getName();
    }
}
