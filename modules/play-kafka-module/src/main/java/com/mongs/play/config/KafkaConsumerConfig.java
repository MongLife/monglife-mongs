package com.mongs.play.config;

import com.mongs.play.module.kafka.dto.KafkaEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {


    @Value("${application.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${application.kafka.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, KafkaEventDto<?>> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put(JsonDeserializer.TYPE_MAPPINGS, "KafkaEventDto:com.mongs.play.module.kafka.dto.KafkaEventDto");


//        List<Class<?>> classes = scan("com.mongs.play.module.kafka.dto");
//
//        StringBuilder typeMappings = new StringBuilder();
//        classes.forEach(clazz -> typeMappings.append(clazz.getSimpleName()).append(":").append(clazz.getCanonicalName()).append(","));
//        props.put(JsonDeserializer.TYPE_MAPPINGS, typeMappings.substring(0, typeMappings.length() - 1));

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaEventDto<?>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaEventDto<?>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

//    private static List<Class<?>> scan(String basePackageName) {
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        String path = basePackageName.replace('.', '/');
//
//        List<Class<?>> classes = new ArrayList<Class<?>>();
//
//        try {
//            List<File> files = new ArrayList<File>();
//            Enumeration<URL> resources = classLoader.getResources(path);
//            while (resources.hasMoreElements()) {
//                URL resource = resources.nextElement();
//                files.add(new File(resource.getFile()));
//            }
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    System.out.println("[Directory] " + file.getAbsolutePath());
//                    classes.addAll(findClasses(file, basePackageName));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return classes;
//    }
//
//    private static List<Class<?>> findClasses(File directory, String packageName) {
//        List<Class<?>> classes = new ArrayList<Class<?>>();
//        if (!directory.exists()) {
//            return classes;
//        }
//
//        File[] files = directory.listFiles();
//        assert files != null;
//        for (File file : files) {
//            if (file.isDirectory()) {
//                System.out.println("[Directory] " + file.getAbsolutePath());
//                classes.addAll(findClasses(file, packageName + "." + file.getName()));
//            } else if (file.getName().endsWith(".class")) {
//                System.out.println("[File] " + file.getAbsolutePath());
//                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
//                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//                try {
//                    classes.add(Class.forName(className, false, classLoader));
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return classes;
//    }
}
