package com.monglife.mongs.module.mqtt.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.mongs.module.mqtt.annotation.MqttConsumer;
import com.monglife.mongs.module.mqtt.annotation.MqttConsumerAdvice;
import com.monglife.mongs.module.mqtt.annotation.MqttMapping;
import com.monglife.mongs.module.mqtt.annotation.MqttPayload;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Slf4j
@Component
public class MqttMappingHandler implements InitializingBean {

    @Value("${spring.mqtt.base-topic}")
    private String BASE_TOPIC;

    private static final String WILD_CARD_WORD = "+";

    private final ApplicationContext applicationContext;

    private final Map<String, TopicMethod> mqttMethodMapping;

    private final Map<Class<? extends Throwable>, Method> mqttExceptionHandlerMapping;

    private final ObjectMapper objectMapper;


    @Autowired
    public MqttMappingHandler(ApplicationContext applicationContext, ObjectMapper objectMapper) {
        this.applicationContext = applicationContext;
        this.mqttMethodMapping = new HashMap<>();
        this.mqttExceptionHandlerMapping = new HashMap<>();
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() {
        this.mqttMappingScan();
        this.mqttExceptionScan();
    }

    /**
     * mqtt 매핑 메서드 스캔
     */
    private void mqttMappingScan() {

        String[] beanNames = applicationContext.getBeanNamesForAnnotation(MqttConsumer.class);

        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            Class<?> beanClass = AopProxyUtils.ultimateTargetClass(bean);

            MqttMapping mqttMapping = beanClass.getAnnotation(MqttMapping.class);

            String clazzTopic = mqttMapping == null ? "" : mqttMapping.value();
            if (clazzTopic.startsWith("/")) clazzTopic = clazzTopic.substring(1);
            if (clazzTopic.endsWith("/")) clazzTopic = clazzTopic.substring(0, clazzTopic.length() - 1);

            for (Method method : beanClass.getDeclaredMethods()) {
                // 메서드 파라미터에서 MqttPayload 가 다건인지 확인
                if (Arrays.stream(method.getParameters()).filter(parameter -> parameter.isAnnotationPresent(MqttPayload.class)).toList().size() > 1) {
                    throw new RuntimeException(beanClass.getName() + "#" + method.getName() + " : Too many @MqttPayload");
                }

                if (method.isAnnotationPresent(MqttMapping.class)) {

                    mqttMapping = method.getAnnotation(MqttMapping.class);

                    String methodTopic = mqttMapping.value();
                    if (methodTopic.startsWith("/")) methodTopic = methodTopic.substring(1);
                    if (methodTopic.endsWith("/")) methodTopic = methodTopic.substring(0, methodTopic.length() - 1);

                    String topic = String.format("%s/%s/%s", BASE_TOPIC, clazzTopic, methodTopic);
                    String key = topic.replaceAll("\\{[a-zA-Z0-9]+}", WILD_CARD_WORD);

                    if (mqttMethodMapping.containsKey(key)) {
                        throw new RuntimeException(beanClass.getName() + "#" + method.getName() + " : Duplicate mapping methods");
                    } else {
                        mqttMethodMapping.put(key, TopicMethod.builder()
                                .method(method)
                                .mapping(topic)
                                .wildMapping(key)
                                .build());
                    }
                }
            }
        }
    }

    /**
     * 예외 처리 메서드 스캔
     */
    private void mqttExceptionScan() {

        String[] beanNames = applicationContext.getBeanNamesForAnnotation(MqttConsumerAdvice.class);

        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            Class<?> beanClass = AopProxyUtils.ultimateTargetClass(bean);

            for (Method method : beanClass.getDeclaredMethods()) {

                if (method.isAnnotationPresent(ExceptionHandler.class)) {
                    ExceptionHandler exceptionHandler = method.getAnnotation(ExceptionHandler.class);
                    Class<? extends Throwable>[] exceptions = exceptionHandler.value();

                    for (Class<? extends Throwable> exception : exceptions) {
                        mqttExceptionHandlerMapping.put(exception, method);
                    }
                }
            }
        }
    }

    /**
     * 매서드 매핑 및 실행
     * @param topic topic
     * @param payload payload
     */
    public void invokeMappingMethod(String topic, String payload) {

        if (topic.startsWith("/")) topic = topic.substring(1);
        if (topic.endsWith("/")) topic = topic.substring(0, topic.length() - 1);

        List<TopicMethod> topicMappingMethods = this.getTopicMappingMethods(topic);

        if (topicMappingMethods.isEmpty()) {
            // topic 과 매칭되는 메서드가 없는 경우
            log.debug("{} : not match method", topic);
        } else if (topicMappingMethods.size() == 1) {
            // topic 과 매칭되는 메서드가 1개인 경우
            for (TopicMethod topicMappingMethod : topicMappingMethods) {

                Method method = topicMappingMethod.getMethod();
                Object[] parameters = this.getParameters(topic, payload, topicMappingMethod);

                log.debug("{} => method: {}#{}, parameters: {}", topic, method.getDeclaringClass(), method.getName(), parameters);

                this.invokeMappingMethod(method, parameters);
            }
        } else {
            // topic 과 매칭되는 메서드가 다수인 경우
            log.error("{} : Too many mapping methods", topic);
        }
    }

    /**
     * 메서드 실행
     * @param method 메서드
     * @param parameters 메서드 파라 미터
     */
    private void invokeMappingMethod(Method method, Object[] parameters) {
        try {
            Class<?> methodClazz = method.getDeclaringClass();
            Object methodClazzBean = applicationContext.getBean(methodClazz);
            method.setAccessible(true);
            method.invoke(methodClazzBean, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            if (e instanceof InvocationTargetException invocationTargetException) {
                for (Class<?> exceptionMappingClazz : mqttExceptionHandlerMapping.keySet()) {
                    Throwable exception = invocationTargetException.getTargetException();
                    if (exceptionMappingClazz.isAssignableFrom(exception.getClass())) {
                        Method exceptionHandlerMethod = mqttExceptionHandlerMapping.get(exceptionMappingClazz);
                        this.invokeExceptionMappingMethod(exceptionHandlerMethod, Collections.singletonList(exception).toArray());
                        return;
                    }
                }
            }

            log.error("invoke mapping method error");
        }
    }

    /**
     * 예외 처리 메서드 실행
     * @param method 예외 처리 메서드
     * @param parameters 예외 클래스
     */
    private void invokeExceptionMappingMethod(Method method, Object[] parameters) {
        try {
            Class<?> methodClazz = method.getDeclaringClass();
            Object exceptionHandlerClazzBean = applicationContext.getBean(methodClazz);
            method.setAccessible(true);
            method.invoke(exceptionHandlerClazzBean, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("invoke exception handler method error");
        }
    }

    /**
     * 메서드 파라 미터 정리
     * @param topic 토픽
     * @param topicMappingMethod 매핑 메서드 Dto
     * @return 메서드 파라미터 순서에 맞는 Object 배열
     */
    private Object[] getParameters(String topic, String payload, TopicMethod topicMappingMethod) {
        List<Object> parameters = new ArrayList<>();

        List<String> topicSplit = Arrays.stream(topic.split("/")).toList();

        Method method = topicMappingMethod.getMethod();
        String[] mappings = topicMappingMethod.getMappings();

        // topic 에서 파라 미터 값 추출
        Map<String, String> topicParameters = new HashMap<>(); // <파라 미터 명, 파라 미터 값>
        for (int parameterIndex : topicMappingMethod.getParameterIndexes()) {
            String fieldName = mappings[parameterIndex];

            fieldName = fieldName.replace("{", "");
            fieldName = fieldName.replace("}", "");

            topicParameters.put(fieldName, topicSplit.get(parameterIndex));
        }

        for (Parameter parameter : method.getParameters()) {

            Class<?> parameterType = parameter.getType();
            String parameterName = parameter.getName();

            if (parameter.isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);

                parameterName = pathVariable.value().isEmpty() ? parameterName : pathVariable.value();
                String parameterValueStr = topicParameters.get(parameterName);

                Object parameterValue = null;
                if (parameterType.equals(short.class) || parameterType.equals(Short.class)) {
                    parameterValue = Short.parseShort(parameterValueStr);
                } else if (parameterType.equals(int.class) || parameterType.equals(Integer.class)) {
                    parameterValue = Integer.parseInt(parameterValueStr);
                } else if (parameterType.equals(long.class) || parameterType.equals(Long.class)) {
                    parameterValue = Long.parseLong(parameterValueStr);
                } else if (parameterType.equals(float.class) || parameterType.equals(Float.class)) {
                    parameterValue = Float.parseFloat(parameterValueStr);
                } else if (parameterType.equals(double.class) || parameterType.equals(Double.class)) {
                    parameterValue = Double.parseDouble(parameterValueStr);
                } else if (parameterType.equals(String.class)) {
                    parameterValue = parameterValueStr;
                }

                parameters.add(parameterValue);

            } else if (parameter.isAnnotationPresent(MqttPayload.class)) {
                try {
                    // @NoArgConstructor 가 필요 -> ObjectMapper 는 기본 생성자가 없으면 파싱이 불가능
                    Object parameterValue = objectMapper.readValue(payload, parameterType);
                    parameters.add(parameterValue);
                } catch (JsonProcessingException e) {
                    parameters.add(null);
                }
            } else {
                parameters.add(null);
            }
        }

        return parameters.toArray();
    }

    /**
     * 매핑 메서드 탐색
     * @param topic 토픽
     * @return 매칭된 매핑 메서드 리스트
     */
    private List<TopicMethod> getTopicMappingMethods(String topic) {
        List<TopicMethod> topicMappingMethods = new ArrayList<>();
        List<String> topicSplit = Arrays.stream(topic.split("/")).toList();

        for (String wildMapping : this.mqttMethodMapping.keySet()) {
            List<String> wildMappingSplit = Arrays.stream(wildMapping.split("/")).toList();

            if (topicSplit.size() == wildMappingSplit.size()) {
                boolean isAllMatch = true;
                for (int index = 0; index < topicSplit.size(); index++) {

                    String topicTemp = topicSplit.get(index);
                    String wildMappingTemp = wildMappingSplit.get(index);

                    if (WILD_CARD_WORD.equals(wildMappingTemp)) continue;

                    if (!topicTemp.equals(wildMappingTemp)) {
                        isAllMatch = false;
                        break;
                    }
                }
                if (isAllMatch) topicMappingMethods.add(this.mqttMethodMapping.get(wildMapping));
            }
        }
        return topicMappingMethods;
    }

    /**
     * Mqtt Mapping Method Dto
     */
    public static class TopicMethod {

        @Getter
        private final Method method;

        @Getter
        private final String wildMapping;

        private final String[] wildMappings;

        @Getter
        private final String mapping;

        @Getter
        private final String[] mappings;

        @Builder
        public TopicMethod(Method method, String wildMapping, String mapping) {
            this.method = method;
            this.wildMapping = wildMapping;
            this.wildMappings = wildMapping.split("/");
            this.mapping = mapping;
            this.mappings = mapping.split("/");
        }

        public List<Integer> getParameterIndexes() {
            List<Integer> parameterIndexes = new ArrayList<>();
            for (int index = 0; index < this.wildMappings.length; index++) {
                if (WILD_CARD_WORD.equals(this.wildMappings[index])) {
                    parameterIndexes.add(index);
                }
            }
            return parameterIndexes;
        }
    }
}


