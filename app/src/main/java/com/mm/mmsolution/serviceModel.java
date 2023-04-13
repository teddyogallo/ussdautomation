package com.mm.mmsolution;


public class serviceModel {

    // string course_name for storing course_name
    // and imgid for storing image id.
    private String servicename,servicedescription,shortcode,serviceid,serviceussdcode;
    private int imgid;

    public serviceModel(String service_name,String description, String shortcode, String ussdcode) {
        this.servicename = service_name;
        this.servicedescription = description;
        this.shortcode =shortcode;
        this.serviceussdcode=ussdcode;

    }

    public String getService_name() {
        return servicename;
    }

    public void setService_name(String course_name) {
        this.servicename = course_name;
    }

    //get desctiption
    public String getDescription() {
        return servicedescription;
    }

    public void setDescription(String servicedesc) {
        this.servicedescription = servicedesc;
    }

    //get shortcode
    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String serviceshortcode) {
        this.shortcode = serviceshortcode;
    }
    //get ussd code
    public String getUssdcode() {
        return serviceussdcode;
    }

    public void setUssdcode(String ussdcodethis) {
        this.serviceussdcode = ussdcodethis;
    }


}
