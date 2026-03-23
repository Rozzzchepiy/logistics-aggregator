package rozchepiy.dev.logisticsaggregator.service;

import rozchepiy.dev.logisticsaggregator.dto.OrderDTO;
import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(Long id);

    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO assignDriver(Long orderId, Long driverId);

    OrderDTO assignLoader(Long orderId, Long loaderId);

    OrderDTO updateOrderStatus(Long orderId, String newStatus);

    void deleteOrder(Long id);
}