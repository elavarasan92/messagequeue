package com.elavarasan.messagequeue.listener;

import java.util.concurrent.ThreadLocalRandom;

import com.elavarasan.messagequeue.model.Result;
import com.elavarasan.messagequeue.model.Student;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class StudentResultCalculator {

	@KafkaListener(topics="${kafka.request.topic}",groupId = "${kafka.group.id}")
	@SendTo
	public Result handle(Student student){
		System.out.println("Calculating Result...");
		double total = ThreadLocalRandom.current().nextDouble(2.5,9.9);
		Result result = new Result();
		result.setName(student.getName());
		result.setResult((total> 3.5) ? "Pass" : "Fail");
		result.setPercentage(String.valueOf(total*10).substring(0,4)+"%");
		return result;
	}
}
