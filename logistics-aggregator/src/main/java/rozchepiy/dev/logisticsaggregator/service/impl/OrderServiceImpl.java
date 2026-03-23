package rozchepiy.dev.logisticsaggregator.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rozchepiy.dev.logisticsaggregator.dto.OrderDTO;
import rozchepiy.dev.logisticsaggregator.exception.NotFoundException;
import rozchepiy.dev.logisticsaggregator.model.DriverProfile;
import rozchepiy.dev.logisticsaggregator.model.LoaderProfile;
import rozchepiy.dev.logisticsaggregator.model.Order;
import rozchepiy.dev.logisticsaggregator.model.User;
import rozchepiy.dev.logisticsaggregator.model.enums.OrderStatus;
import rozchepiy.dev.logisticsaggregator.repository.DriverProfileRepository;
import rozchepiy.dev.logisticsaggregator.repository.LoaderProfileRepository;
import rozchepiy.dev.logisticsaggregator.repository.OrderRepository;
import rozchepiy.dev.logisticsaggregator.repository.UserRepository;
import rozchepiy.dev.logisticsaggregator.service.OrderService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DriverProfileRepository driverRepository;
    private final LoaderProfileRepository loaderRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Замовлення не знайдено з ID: " + id));
        return convertToDto(order);
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        User customer = userRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Замовника не знайдено з ID: " + orderDTO.getCustomerId()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setStatus(OrderStatus.CREATED);

        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDTO assignDriver(Long orderId, Long driverId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Замовлення не знайдено з ID: " + orderId));

        DriverProfile driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new NotFoundException("Водія не знайдено з ID: " + driverId));

        if (order.getDriver() != null) {
            throw new RuntimeException("Це замовлення вже має водія!");
        }

        order.setDriver(driver);
        order.setStatus(OrderStatus.ACCEPTED);

        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO assignLoader(Long orderId, Long loaderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Замовлення не знайдено з ID: " + orderId));

        LoaderProfile loader = loaderRepository.findById(loaderId)
                .orElseThrow(() -> new NotFoundException("Вантажника не знайдено з ID: " + loaderId));

        order.getLoaders().add(loader);

        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Замовлення не знайдено з ID: " + orderId));

        try {
            order.setStatus(OrderStatus.valueOf(newStatus.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Невірний статус замовлення. Доступні: CREATED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED");
        }

        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Замовлення не знайдено з ID: " + id));
        orderRepository.delete(order);
    }

    private OrderDTO convertToDto(Order order) {
        OrderDTO dto = modelMapper.map(order, OrderDTO.class);

        dto.setCustomerId(order.getCustomer().getId());

        if (order.getDriver() != null) {
            dto.setDriverId(order.getDriver().getId());
        }

        if (order.getLoaders() != null && !order.getLoaders().isEmpty()) {
            Set<Long> loaderIds = order.getLoaders().stream()
                    .map(LoaderProfile::getId)
                    .collect(Collectors.toSet());
            dto.setLoaderIds(loaderIds);
        }

        return dto;
    }
}