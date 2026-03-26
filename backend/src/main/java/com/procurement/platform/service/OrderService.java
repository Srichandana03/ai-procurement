package com.procurement.platform.service;

import com.procurement.platform.dto.OrderDto;
import com.procurement.platform.entity.Order;
import com.procurement.platform.entity.PurchaseRequest;
import com.procurement.platform.entity.Supplier;
import com.procurement.platform.exception.ResourceNotFoundException;
import com.procurement.platform.repository.OrderRepository;
import com.procurement.platform.repository.PurchaseRequestRepository;
import com.procurement.platform.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PurchaseRequestRepository requestRepository;
    private final SupplierRepository supplierRepository;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrderById(Long id) {
        return toDto(orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id)));
    }

    public OrderDto createOrder(OrderDto dto) {
        PurchaseRequest request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase request not found"));

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        // Update request status to APPROVED
        request.setStatus("APPROVED");
        requestRepository.save(request);

        Order order = Order.builder()
                .purchaseRequest(request)
                .supplier(supplier)
                .status("PENDING")
                .totalAmount(request.getBudget() * request.getQuantity())
                .build();

        return toDto(orderRepository.save(order));
    }

    public OrderDto updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        return toDto(orderRepository.save(order));
    }

    private OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setRequestId(order.getPurchaseRequest().getId());
        dto.setSupplierId(order.getSupplier().getId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderDate(order.getOrderDate() != null ? order.getOrderDate().toString() : null);
        dto.setProductName(order.getPurchaseRequest().getProduct());
        dto.setSupplierName(order.getSupplier().getName());
        return dto;
    }
}
