package com.example.billingsystem.events;

import com.example.billingsystem.entity.Orders;
import org.springframework.context.ApplicationEvent;


public class InvoiceGeneratedEvent extends ApplicationEvent {

    private final Orders order;

    public InvoiceGeneratedEvent(Object source, Orders order){
        super(source);
        this.order = order;
    }

    public Orders getOrder(){
        return order;
    }


}
