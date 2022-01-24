package com.esecondhand.esecondhand.controller;


import com.esecondhand.esecondhand.domain.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class OnCreateReportCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private String cause;
    private Locale locale;
    private Item item;

    public OnCreateReportCompleteEvent(
            Item item, String cause, Locale locale, String appUrl) {
        super(item);

        this.item = item;
        this.locale = locale;
        this.appUrl = appUrl;
        this.cause = cause;
    }
}
