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
import rozchepiy.dev.logisticsaggregator.model.enums.OrderType;
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
        order.setTitle(orderDTO.getTitle());
        order.setDescription(orderDTO.getDescription());
        order.setStatus(OrderStatus.CREATED);

        order.setOrderType(OrderType.valueOf(orderDTO.getOrderType()));
        order.setScheduledTime(orderDTO.getScheduledTime());
        order.setRequiredCarVolume(orderDTO.getRequiredCarVolume());
        order.setRequiredCarWeight(orderDTO.getRequiredCarWeight());
        order.setTruckPricePerHour(orderDTO.getTruckPricePerHour());
        order.setRequiredLoadersCount(orderDTO.getRequiredLoadersCount());
        order.setLoaderPricePerHour(orderDTO.getLoaderPricePerHour());
        order.setTotalPrice(orderDTO.getTotalPrice());

        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDTO assignDriver(Long orderId, Long userId) { // Тепер приймаємо userId
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Замовлення не знайдено з ID: " + orderId));

        // 1. Шукаємо Юзера
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Користувача не знайдено з ID: " + userId));

        // 2. Дістаємо його профіль водія
        DriverProfile driver = user.getDriverProfile();
        if (driver == null) {
            throw new RuntimeException("У цього користувача немає профілю водія!");
        }

        if (order.getDriver() != null) {
            throw new RuntimeException("Це замовлення вже має водія!");
        }

        order.setDriver(driver);

        // Розумна перевірка статусу
        updateStatusIfFullyStaffed(order);

        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO assignLoader(Long orderId, Long userId) { // Тепер приймаємо userId
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Замовлення не знайдено з ID: " + orderId));

        // 1. Шукаємо Юзера
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Користувача не знайдено з ID: " + userId));

        // 2. Дістаємо його профіль вантажника
        LoaderProfile loader = user.getLoaderProfile();
        if (loader == null) {
            throw new RuntimeException("У цього користувача немає профілю вантажника!");
        }

        order.getLoaders().add(loader);

        // Розумна перевірка статусу
        updateStatusIfFullyStaffed(order);

        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }

    // --- ДОПОМІЖНИЙ МЕТОД: Перевірка чи зібрана команда ---
    private void updateStatusIfFullyStaffed(Order order) {
        boolean isDriverFulfilled = true;
        boolean areLoadersFulfilled = true;

        if (order.getOrderType() == OrderType.TRUCK_ONLY || order.getOrderType() == OrderType.BOTH) {
            isDriverFulfilled = (order.getDriver() != null);
        }

        if (order.getOrderType() == OrderType.LOADERS_ONLY || order.getOrderType() == OrderType.BOTH) {
            int currentLoaders = order.getLoaders() != null ? order.getLoaders().size() : 0;
            int requiredLoaders = order.getRequiredLoadersCount() != null ? order.getRequiredLoadersCount() : 0;
            areLoadersFulfilled = (currentLoaders >= requiredLoaders);
        }

        // Якщо всі є - переводимо в ACCEPTED, інакше залишаємо CREATED
        if (isDriverFulfilled && areLoadersFulfilled) {
            order.setStatus(OrderStatus.ACCEPTED);
        } else {
            order.setStatus(OrderStatus.CREATED);
        }
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Замовлення не знайдено з ID: " + orderId));
        order.setStatus(OrderStatus.valueOf(newStatus.toUpperCase()));
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
            dto.setDriverUserId(order.getDriver().getUser().getId());
        }

        if (order.getLoaders() != null && !order.getLoaders().isEmpty()) {
            Set<Long> loaderUserIds = order.getLoaders().stream()
                    .map(loader -> loader.getUser().getId())
                    .collect(Collectors.toSet());
            dto.setLoaderUserIds(loaderUserIds);
        }

        return dto;
    }
}