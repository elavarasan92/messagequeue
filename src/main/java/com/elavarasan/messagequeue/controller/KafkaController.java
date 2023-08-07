package com.elavarasan.messagequeue.controller;

import java.util.concurrent.ExecutionException;

import com.elavarasan.messagequeue.model.Result;
import com.elavarasan.messagequeue.model.Student;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

	@Value("${kafka.request.topic}")
	private String requestTopic;

	private final ReplyingKafkaTemplate<String, Student, Result> replyingKafkaTemplate;

	@PostMapping("get-result")
	public ResponseEntity<Result> getObject(@RequestBody Student student)
			throws ExecutionException, InterruptedException {
		ProducerRecord<String,Student> producerRecord = new ProducerRecord<>(requestTopic,null,student.getRegistrationNumber(),student);
		RequestReplyFuture<String, Student, Result> future = replyingKafkaTemplate.sendAndReceive(producerRecord);
		ConsumerRecord<String, Result> response = future.get();
		assert response != null;
		return new ResponseEntity<>(response.value(), HttpStatus.OK);
	}

}
