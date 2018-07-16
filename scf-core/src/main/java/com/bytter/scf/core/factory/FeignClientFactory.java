package com.bytter.scf.core.factory;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 提供自定义URI回调Feign客户端的工厂
 */
@Component
public class FeignClientFactory {

    private static final ConcurrentMap<String, Object> client_caches = new ConcurrentHashMap<>();


    @Autowired
    private FeignContext feignContext;

    protected <T> T get(FeignContext context, Class<T> type) {
        T instance = context.getInstance(type.getSimpleName(), type);
        if (instance == null) {
            throw new IllegalStateException("No bean found of type " + type + " for " + type.getSimpleName());
        }
        return instance;
    }

    protected <T> Feign.Builder feign(FeignContext context, Class<T> type, Map<String, Object> attributes) {
        FeignLoggerFactory loggerFactory = get(context, FeignLoggerFactory.class);
        Logger logger = loggerFactory.create(type);

        // @formatter:off
        Feign.Builder builder = get(context, Feign.Builder.class)
                // required values
                .logger(logger)
                .encoder(get(context, Encoder.class))
                .decoder(get(context, Decoder.class))
                .contract(get(context, Contract.class));
        // @formatter:on

//        configureFeign(context, builder);
        configureUsingConfiguration(context, builder, attributes);
        return builder;
    }

    protected void configureUsingConfiguration(FeignContext context, Feign.Builder builder, Map<String, Object> attributes) {
        Logger.Level level = getOptional(context, Logger.Level.class);
        if (level != null) {
            builder.logLevel(level);
        }
        Retryer retryer = getOptional(context, Retryer.class);
        if (retryer != null) {
            builder.retryer(retryer);
        }
        ErrorDecoder errorDecoder = getOptional(context, ErrorDecoder.class);
        if (errorDecoder != null) {
            builder.errorDecoder(errorDecoder);
        }
        Request.Options options = getOptional(context, Request.Options.class);
        if (options != null) {
            builder.options(options);
        }
        Map<String, RequestInterceptor> requestInterceptors = context.getInstances((String) attributes.get("name"), RequestInterceptor.class);
        if (requestInterceptors != null) {
            builder.requestInterceptors(requestInterceptors.values());
        }

        Boolean decode404 = (Boolean) attributes.get("decode404");
        if (decode404) {
            builder.decode404();
        }
    }

    protected <T> T getOptional(FeignContext context, Class<T> type) {
        return context.getInstance(type.getSimpleName(), type);
    }


    private <T> String getCacheKey(Class<T> type, String uri) {
        return type.getName() + ":" + (new BASE64Encoder()).encodeBuffer(uri.getBytes());
    }

    /**
     * 指定url调用接口
     *
     * @param type client接口
     * @param uri  指定的接口uri地址
     * @param <T>  返回泛型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type, String uri) {
        Assert.isTrue(StringUtils.hasLength(uri), "uri 不能为空！");
        String cacheKey = getCacheKey(type, uri);
        Object client = client_caches.get(cacheKey);
        if (client != null) {
            return (T) client;
        }
        AnnotatedGenericBeanDefinition annotatedGenericBeanDefinition = new AnnotatedGenericBeanDefinition(type);
        AnnotationMetadata annotationMetadata = annotatedGenericBeanDefinition.getMetadata();
        Assert.isTrue(annotationMetadata.isInterface(), "@FeignClient can only be specified on an interface");
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(FeignClient.class.getCanonicalName());
        Feign.Builder feign = feign(feignContext, type, attributes);
        T target = feign.target(type, uri);
        client_caches.put(cacheKey, target);
        return target;
    }

}
