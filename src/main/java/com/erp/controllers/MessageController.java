package com.erp.controllers;

import com.erp.dtos.response.message.MessageResponseDTO;
import com.erp.models.Employee;
import com.erp.models.Message;
import com.erp.services.IEmployeeService;
import com.erp.services.IMessageService;
import com.erp.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Tag(name = "Message Management", description = "APIs for managing messages")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final IMessageService messageService;
    private final IEmployeeService employeeService;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    @Operation(summary = "Get all messages")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<MessageResponseDTO>> getAllMessages() {
        List<Message> messages = messageService.findAllMessages();
        List<MessageResponseDTO> messageResponseDTOs = messages.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageResponseDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get message by ID")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<MessageResponseDTO> getMessageById(@PathVariable UUID id) {
        Message message = messageService.findMessageById(id);
        return ResponseEntity.ok(convertToResponseDTO(message));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get messages by employee ID")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN') or @userSecurity.isCurrentEmployee(#employeeId)")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesByEmployee(@PathVariable UUID employeeId) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        List<Message> messages = messageService.findMessagesByEmployee(employee);
        List<MessageResponseDTO> messageResponseDTOs = messages.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageResponseDTOs);
    }

    @GetMapping("/employee/{employeeId}/month-year/{monthYear}")
    @Operation(summary = "Get messages by employee ID and month/year")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN') or @userSecurity.isCurrentEmployee(#employeeId)")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesByEmployeeAndMonthYear(
            @PathVariable UUID employeeId,
            @PathVariable String monthYear) {
        
        Employee employee = employeeService.findEmployeeById(employeeId);
        List<Message> messages = messageService.findMessagesByEmployeeAndMonthYear(employee, monthYear);
        List<MessageResponseDTO> messageResponseDTOs = messages.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageResponseDTOs);
    }

    @GetMapping("/current")
    @Operation(summary = "Get messages for current employee")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesForCurrentEmployee() {
        Employee employee = employeeService.findEmployeeByUser(userService.getLoggedInUser());
        List<Message> messages = messageService.findMessagesByEmployee(employee);
        List<MessageResponseDTO> messageResponseDTOs = messages.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageResponseDTOs);
    }

    @GetMapping("/current/month-year/{monthYear}")
    @Operation(summary = "Get messages for current employee by month/year")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesForCurrentEmployeeByMonthYear(
            @PathVariable String monthYear) {
        
        Employee employee = employeeService.findEmployeeByUser(userService.getLoggedInUser());
        List<Message> messages = messageService.findMessagesByEmployeeAndMonthYear(employee, monthYear);
        List<MessageResponseDTO> messageResponseDTOs = messages.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageResponseDTOs);
    }

    private MessageResponseDTO convertToResponseDTO(Message message) {
        MessageResponseDTO responseDTO = modelMapper.map(message, MessageResponseDTO.class);
        return responseDTO;
    }
}