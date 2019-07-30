package com.oocl.ita.parkinglot.service;

import com.oocl.ita.parkinglot.dto.GetEmployeeParkingLotDTO;
import com.oocl.ita.parkinglot.model.Employee;
import com.oocl.ita.parkinglot.model.Orders;
import com.oocl.ita.parkinglot.model.ParkingLot;
import com.oocl.ita.parkinglot.vo.PageVO;
import com.oocl.ita.parkinglot.vo.ParkingLotVO;

import java.util.List;

public interface EmployeeService {
    List<ParkingLot> getEmployeeAllParkingLots(String parkingBoyId);

    Employee getEmployeeById(String employeeId);

    List<Orders> getEmployeeOrdersByFinish(String id,boolean finish);


    int updateEmployeeParkingLotCapacityById(String id, ParkingLot parkingLot);

    ParkingLot addEmployeeNewParkingLot(String id, ParkingLot parkingLot);

    PageVO<ParkingLot> findByConditions(String id , GetEmployeeParkingLotDTO getEmployeeParkingLotDTO);


    List<ParkingLotVO> getParkingLotVOsByEmployeeId(String id,int page, int pageSize);

}
