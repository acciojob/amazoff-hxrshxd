package com.driver;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public void addPartner(String partner) {
        orderRepository.addPartner(partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderRepository.addOrderPartnerPair(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        return orderRepository.getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        List<DeliveryPartner> partnerList = orderRepository.getPartnersList();
        for (DeliveryPartner partner: partnerList) {
            if (partner.getId().equals(partnerId)) return partner;
        }
        return null;
    }

    public int getOrderCountByPartnerId(String partnerId) {
        List<String> orderList = orderRepository.getOrderByPartnerList(partnerId);
        return orderList.size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderRepository.getOrderByPartnerList(partnerId);
    }

    public List<String> getAllOrder() {
        List<String> orderList = new ArrayList<>();
        List<Order> orders = orderRepository.getOrdersList();
        for (Order order: orders) {
            orderList.add(order.getId());
        }
        return orderList;
    }

    public int getCountOfUnassignedOrders() {
        List<Order> orders = orderRepository.getOrdersList();
        List<DeliveryPartner> partners = orderRepository.getPartnersList();

        Set<String> unquieOrders = new HashSet<>();
        for (DeliveryPartner partner: partners) {
            List<String> orderByPartner = orderRepository.getOrderByPartnerList(partner.getId());
            for (String str: orderByPartner) {
                unquieOrders.add(str);
            }
        }
        return orders.size() - unquieOrders.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        List<String> orderByPartner = orderRepository.getOrderByPartnerList(partnerId);
        int count = 0;

        int hour = Integer.parseInt(time.substring(0, 2));
        int min = Integer.parseInt(time.substring(3));
        int timeInt = hour*60 + min;

        for (String orderId: orderByPartner) {
            Order order = orderRepository.getOrderById(orderId);
            if (timeInt < order.getDeliveryTime()) count++;
        }

        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orderByPartner = orderRepository.getOrderByPartnerList(partnerId);
        int max = Integer.MIN_VALUE;

        for (String orderId: orderByPartner) {
            Order order = orderRepository.getOrderById(orderId);
            max = Math.max(max, order.getDeliveryTime());
        }

        // to convert int to string
        int hour = max/60;
        int min = max - hour*60;
        String str = hour + ":" + min;

        return str;
    }

    public void deletePartnerById(String partnerId) {
        orderRepository.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderRepository.deleteOrderById(orderId);
    }
}
