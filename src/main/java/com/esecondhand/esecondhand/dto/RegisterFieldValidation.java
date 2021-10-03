package com.esecondhand.esecondhand.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

public class RegisterFieldValidation {

    private boolean email;
    private boolean displayName;
    private boolean isRegistered;


    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isDisplayName() {
        return displayName;
    }

    public void setDisplayName(boolean displayName) {
        this.displayName = displayName;
    }

    public RegisterFieldValidation( boolean email, boolean displayName, boolean isRegistered) {
        this.email = email;
        this.displayName = displayName;
        this.isRegistered = isRegistered;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

}
