package com.oocl.ita.parkinglot.service.impl;

import com.oocl.ita.parkinglot.enums.OrdersStatusEnum;
import com.oocl.ita.parkinglot.model.Employee;
import com.oocl.ita.parkinglot.model.Orders;
import com.oocl.ita.parkinglot.model.ParkingLot;
import com.oocl.ita.parkinglot.repository.EmployeeRepository;
import com.oocl.ita.parkinglot.repository.OrdersRepository;
import com.oocl.ita.parkinglot.repository.ParkingLotRepository;
import com.oocl.ita.parkinglot.service.EmployeeService;
import com.oocl.ita.parkinglot.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public List<ParkingLot> getEmployeeAllParkingLots(String parkingBoyId) {
        Employee employee = employeeRepository.findById(parkingBoyId).orElse(null);
        return (employee == null) ? null : employee.getParkingLots();
    }

    @Override
    public Employee getEmployeeById(String employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }

    @Override
    public List<Orders> getEmployeeOrdersByFinish(String id, boolean finish) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null)
            return null;
        else {
            if (!finish) {
                List<Orders> orders = ordersRepository.findEmployeeUnfinishOrders(id);

                List<Orders> unfinishOrders = orders.stream()
                        .filter(element -> element.getStatus() == OrdersStatusEnum.PARK_ORDER_RECEIVED.ordinal()
                                || element.getStatus() == OrdersStatusEnum.PARK_ORDER_PICKED_UP_THE_CAR.ordinal()
                                || element.getStatus() == OrdersStatusEnum.FETCH_ORDER_RECEIVED.ordinal()
                                || element.getStatus() == OrdersStatusEnum.FETCH_ORDER_PICKED_UP_THE_CAR.ordinal())
                        .collect(Collectors.toList());

                return unfinishOrders;
            } else {
                List<Orders> parkingFinishorders = ordersRepository.findEmployeeParkingFinishOrders(id);

                for (Orders order : parkingFinishorders) {
                    order.setStatus(OrdersStatusEnum.PARK_ORDER_CAR_IS_PARKED_AND_FETCH_ORDER_NOT_RECEIVED.ordinal());
                }

                List<Orders> findFetchingFinishOrders = ordersRepository.findEmployeeFetchingFinishOrders(id);
                ArrayList<Orders> cloneOrders = new ArrayList<>();

                for (Orders order : findFetchingFinishOrders) {
                    if (parkingFinishorders.contains(order)) {
                        Orders cloneOrder = new Orders();
                        cloneOrder.setStatus(OrdersStatusEnum.FETCH_ORDER_COMPLETED.ordinal());
                        BeanUtils.copyProperties(order, cloneOrder, "status");
                        cloneOrders.add(cloneOrder);
                    }
                }

                List<Orders> resultFetchingFinishOrders = findFetchingFinishOrders.stream()
                        .filter(element -> !parkingFinishorders.contains(element))
                        .collect(Collectors.toList());

                ArrayList<Orders> finishOrders = new ArrayList<>();
                finishOrders.addAll(parkingFinishorders);
                finishOrders.addAll(resultFetchingFinishOrders);
                finishOrders.addAll(cloneOrders);
                return finishOrders;
            }

        }
    }
}
