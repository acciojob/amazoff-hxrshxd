package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    Map<String, Order> orderDb = new HashMap<>();
    Map<String, DeliveryPartner> partnerDb = new HashMap<>();
    Map<String, List<String>> partnerOrderDb = new HashMap<>();
    Map<String, String> orderPartnerDb = new HashMap<>();

    public void addOrder(Order order) {
        String key = order.getId();
        orderDb.put(key, order);
    }

    public void addPartner(String partner) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partner);
        partnerDb.put(partner, deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        List<String> temp = new ArrayList<>();
        if (partnerOrderDb.containsKey(partnerId)) {
            temp = partnerOrderDb.get(partnerId);
        }
        temp.add(orderId);
        partnerOrderDb.put(partnerId, temp);

        orderPartnerDb.put(orderId, partnerId);
        // increasing order count in partner
        if (partnerDb.containsKey(partnerId)) {
            DeliveryPartner partner = partnerDb.get(partnerId);
            partner.setNumberOfOrders(temp.size());
        }
    }

    public List<Order> getOrdersList() {
        return orderDb.values().stream().toList();
    }

    public Order getOrderById(String orderId) {
        return orderDb.get(orderId);
    }

    public List<DeliveryPartner> getPartnersList() {
        return partnerDb.values().stream().toList();
    }

    public List<String> getOrderByPartnerList(String partnerId) {
        List<String> temp = new ArrayList<>();
        if (partnerOrderDb.containsKey(partnerId)) {
            temp = partnerOrderDb.get(partnerId);
        }
        return temp;
    }

    public void deletePartnerById(String partnerId) {
        partnerDb.remove(partnerId);
        partnerOrderDb.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderDb.remove(orderId);
        String partnerId = orderPartnerDb.get(orderId);
        List<String> orderList = partnerOrderDb.get(partnerId);
        orderList.remove(orderId);
        partnerOrderDb.put(partnerId, orderList);
    }
}
