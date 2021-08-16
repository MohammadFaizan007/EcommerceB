package com.inlog.ecommerce.model;

import org.json.JSONObject;

import java.util.ArrayList;

public class myorder {
    private int shop_order_id,order_id;
    private String name;
    private String state;
    private String order_received_date;
    private String ready_to_ship_date;
    private String shipped_date;
    private String accepted_date;
    private String out_for_delivery_date;
    private String delivered_date;
    private String cancel_date;
    private String total_amount;
    private JSONObject tempDict;
    private int overall_shop_rating;
    private int delivery_rating;
    private int professional_rating;
    private int good_quality_rating;
    private int responsive_rating;


    static ArrayList<myorder> myorderlist = new ArrayList<>();

    public myorder(int shop_order_id, int order_id, String name, String state, String order_received_date, String accepted_date,String ready_to_ship_date, String shipped_date,  String out_for_delivery_date, String delivered_date,String cancel_date,String total_amount,JSONObject tempDict,ArrayList<myorder> Myorderval
    ,int overall_shop_rating,int delivery_rating,int professional_rating,int good_quality_rating,int responsive_rating){
        this.shop_order_id = shop_order_id;
        this.order_id = order_id;
        this.name = name;
        this.state = state;
        this.order_received_date = order_received_date;
        this.ready_to_ship_date = ready_to_ship_date;
        this.shipped_date = shipped_date;
        this.accepted_date = accepted_date;
        this.out_for_delivery_date = out_for_delivery_date;
        this.delivered_date = delivered_date;
        this.cancel_date =  cancel_date;
        this.total_amount = total_amount;
        this.tempDict = tempDict;
        this.myorderlist = Myorderval;
        this.overall_shop_rating = overall_shop_rating;
        this.delivery_rating = delivery_rating;
        this.professional_rating = professional_rating;
        this.good_quality_rating = good_quality_rating;
        this.responsive_rating = responsive_rating;

    }
    public myorder()
    {
        name = null;
        state = null;
        order_received_date = null;
        ready_to_ship_date = null;
        shipped_date = null;
        accepted_date = null;
        out_for_delivery_date = null;
        delivered_date = null;
        cancel_date = null;
        total_amount = null;
        tempDict = null;
    }
    public int getShop_order_id() {
        return shop_order_id;
    }

    public void setShop_order_id(int shop_order_id) {
        this.shop_order_id = shop_order_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrder_received_date() {
        return order_received_date;
    }

    public void setOrder_received_date(String order_received_date) {
        this.order_received_date = order_received_date;
    }

    public String getReady_to_ship_date() {
        return ready_to_ship_date;
    }

    public void setReady_to_ship_date(String ready_to_ship_date) {
        this.ready_to_ship_date = ready_to_ship_date;
    }

    public String getShipped_date() {
        return shipped_date;
    }

    public void setShipped_date(String shipped_date) {
        this.shipped_date = shipped_date;
    }

    public String getAccepted_date() {
        return accepted_date;
    }

    public void setAccepted_date(String accepted_date) {
        this.accepted_date = accepted_date;
    }

    public String getOut_for_delivery_date() {
        return out_for_delivery_date;
    }

    public void setOut_for_delivery_date(String out_for_delivery_date) {
        this.out_for_delivery_date = out_for_delivery_date;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(String delivered_date) {
        this.delivered_date = delivered_date;
    }

    public String getCancel_date() {
        return cancel_date;
    }

    public void setCancel_date(String cancel_date) {
        this.cancel_date = cancel_date;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public JSONObject getTempDict() {
        return tempDict;
    }

    public void setTempDict(JSONObject tempDict) {
        this.tempDict = tempDict;
    }
    public static ArrayList<myorder> getMyorderlist() {
        return myorderlist;
    }

    public static void setMyorderlist(ArrayList<myorder> myorderlist) {
        myorder.myorderlist = myorderlist;
    }
    public myorder getorderitem(int position) {
        return myorderlist.get(position);
    }

    public int getOverall_shop_rating() {
        return overall_shop_rating;
    }

    public void setOverall_shop_rating(int overall_shop_rating) {
        this.overall_shop_rating = overall_shop_rating;
    }

    public int getDelivery_rating() {
        return delivery_rating;
    }

    public void setDelivery_rating(int delivery_rating) {
        this.delivery_rating = delivery_rating;
    }

    public int getProfessional_rating() {
        return professional_rating;
    }

    public void setProfessional_rating(int professional_rating) {
        this.professional_rating = professional_rating;
    }

    public int getGood_quality_rating() {
        return good_quality_rating;
    }

    public void setGood_quality_rating(int good_quality_rating) {
        this.good_quality_rating = good_quality_rating;
    }

    public int getResponsive_rating() {
        return responsive_rating;
    }

    public void setResponsive_rating(int responsive_rating) {
        this.responsive_rating = responsive_rating;
    }
}
