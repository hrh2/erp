package com.erp.services.impl;

import com.erp.enums.IEmailTemplate;
import com.erp.exceptions.AppException;
import com.erp.models.Employee;
import com.erp.models.Message;
import com.erp.models.Payslip;
import com.erp.repositories.IMessageRepository;
import com.erp.services.IMessageService;
import com.erp.standalone.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final IMessageRepository messageRepository;
    private final EmailService emailService;

    @Override
    public Message createMessage(Message message) {
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public Message createPayslipApprovalMessage(Payslip payslip) {
        Employee employee = payslip.getEmployee();
        String monthYear = payslip.getMonth() + "/" + payslip.getYear();

        // Create message content
        String messageContent = String.format(
                "Dear %s,\nYour salary for %s from Government of Rwanda amounting to %s has been credited to your account %s successfully.",
                employee.getUser().getFirstName(),
                monthYear,
                payslip.getNetSalary().toString(),
                employee.getCode()
        );

        // Create and save message
        Message message = Message.builder()
                .employee(employee)
                .message(messageContent)
                .monthYear(monthYear)
                .sentAt(LocalDateTime.now())
                .build();

        Message savedMessage = messageRepository.save(message);

        // Send email notification
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("firstName", employee.getUser().getFirstName());
            variables.put("monthYear", monthYear);
            variables.put("amount", payslip.getNetSalary().toString());
            variables.put("employeeId", employee.getCode());
            variables.put("payslipId", payslip.getId().toString());
            variables.put("baseUrl", "http://localhost:8000/api/v1");

            emailService.sendEmail(
                    employee.getUser().getEmail(),
                    employee.getUser().getFirstName(),
                    "Salary Payment Notification",
                    IEmailTemplate.SALARY_PAYMENT,
                    variables
            );
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to send email notification: " + e.getMessage());
        }

        return savedMessage;
    }

    @Override
    public Message findMessageById(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new AppException("Message not found with id: " + id));
    }

    @Override
    public List<Message> findMessagesByEmployee(Employee employee) {
        return messageRepository.findByEmployee(employee);
    }

    @Override
    public List<Message> findMessagesByEmployeeAndMonthYear(Employee employee, String monthYear) {
        return messageRepository.findByEmployeeAndMonthYear(employee, monthYear);
    }

    @Override
    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }
}
