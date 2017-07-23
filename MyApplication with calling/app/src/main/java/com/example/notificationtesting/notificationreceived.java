package com.example.notificationtesting;

public class notificationreceived {
    public String Date;
    public String Message;
    public String status;
    public String Time;
    public String link;

    public notificationreceived()
    {

    }
    public notificationreceived(String Date, String Message, String status, String Time,String link)
    {
        this.Date=Date;
        this.Message=Message;
        this.status=status;
        this.Time=Time;
        this.link=link;
    }
}
