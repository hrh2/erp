package com.erp.services;

import com.erp.models.Employee;
import com.erp.models.Message;
import com.erp.models.Payslip;

import java.util.List;
import java.util.UUID;

public interface IMessageService {
    Message createMessage(Message message);
    Message createPayslipApprovalMessage(Payslip payslip);
    Message findMessageById(UUID id);
    List<Message> findMessagesByEmployee(Employee employee);
    List<Message> findMessagesByEmployeeAndMonthYear(Employee employee, String monthYear);
    List<Message> findAllMessages();
}