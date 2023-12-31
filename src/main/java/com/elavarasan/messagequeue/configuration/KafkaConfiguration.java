package com.elavarasan.messagequeue.configuration;

import com.elavarasan.messagequeue.model.Result;
import com.elavarasan.messagequeue.model.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaConfiguration {
	@Value("${kafka.group.id}")
	private String groupId;
	@Value("${kafka.reply.topic}")
	private String replyTopic;
	@Bean
	public ReplyingKafkaTemplate<String, Student, Result> replyingKafkaTemplate(ProducerFactory<String,Student> pf,
			ConcurrentKafkaListenerContainerFactory<String,Result> containerFactory){
		ConcurrentMessageListenerContainer<String,Result> replyContainer = containerFactory.createContainer(replyTopic);
		replyContainer.getContainerProperties().setMissingTopicsFatal(false);
		replyContainer.getContainerProperties().setGroupId(groupId);
		return new ReplyingKafkaTemplate<>(pf,replyContainer);
	}
	@Bean
	public KafkaTemplate<String,Result> replyTemplate(ProducerFactory<String,Result> pf,
			ConcurrentKafkaListenerContainerFactory<String,Result> factory){
		KafkaTemplate<String,Result> kafkaTemplate = new KafkaTemplate<>(pf);
		factory.getContainerProperties().setMissingTopicsFatal(false);
		factory.setReplyTemplate(kafkaTemplate);
		return kafkaTemplate;
	}
}
