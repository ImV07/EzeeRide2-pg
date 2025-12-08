package com.project.dto;

import jakarta.validation.constraints.Pattern;

public class CustomerUpdateDTO {

    private String cname;

    @Pattern(regexp = "[0-9]{10}", message = "Invalid contact format. Please provide a valid 10-digit mobile number.")
    private String contact;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


}
