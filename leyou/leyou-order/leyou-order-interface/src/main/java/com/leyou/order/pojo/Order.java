package com.leyou.order.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Table(name = "tb_order")
public class Order {

    @Id
    private Long orderId;

    /**
     * 总金额
     */
    @NotNull
    @Column(name = "taotal_pay")
    private Double totalPay;
    /**
     * 实付金额
     */
    @NotNull
    @Column(name = "actual_pay")
    private Double actualPay;

    /**
     * 支付类型，1、在线支付，2、货到付款
     */
    @NotNull
    private Integer paymentType;

    /**
     * 参与促销活动的id
     */
    @Column(name = "promotion_ids")
    private String promotionIds;

    /**
     * 邮费
     */
    @Column(name = "post_fee")
    private String postFee;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 物流名称
     */
    @Column(name = "shipping_name")
    private String shippingName;

    /**
     * 物流单号
     */
    @Column(name = "shipping_code")
    private String shippingCode;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 买家留言
     */
    @Column(name = "buyer_message")
    private String buyerMessage;

    /**
     * 买家昵称
     */
    @Column(name = "buyer_nick")
    private String buyerNick;

    /**
     * 买家是否已经评价
     */
    @Column(name = "buyer_rate")
    private Boolean buyerRate;

    /**
     * 收货人全名
     */
    private String receiver;

    /**
     * 移动电话
     */
    @Column(name = "receiver_mobile")
    private String receiverMobile;

    /**
     * 省份
     */
    @Column(name = "receiver_state")
    private String receiverState;

    /**
     * 城市
     */
    @Column(name = "receiver_city")
    private String receiverCity;

    /**
     *  区/县
     */
    @Column(name = "receiver_district")
    private String receiverDistrict;

    /**
     * 收货地址，如：xx路xx号
     */
    @Column(name = "receiver_address")
    private String receiverAddress;

    /**
     * 邮政编码,如：310001
     */
    @Column(name = "receiver_zip")
    private String receiverZip;

    /**
     * 发票类型，0无发票，1普通发票，2电子发票，3增值税发票
     */
    @Column(name = "invoice_type")
    private Integer invoiceType;

    /**
     * 订单来源 1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
     */
    @Column(name = "source_type")
    private Integer sourceType;

    @Transient
    private List<OrderDetail> orderDetails;

    @Transient
    private Integer status;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(Double totalPay) {
        this.totalPay = totalPay;
    }

    public Double getActualPay() {
        return actualPay;
    }

    public void setActualPay(Double actualPay) {
        this.actualPay = actualPay;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getPromotionIds() {
        return promotionIds;
    }

    public void setPromotionIds(String promotionIds) {
        this.promotionIds = promotionIds;
    }

    public String getPostFee() {
        return postFee;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public Boolean getBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(Boolean buyerRate) {
        this.buyerRate = buyerRate;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", totalPay=" + totalPay +
                ", actualPay=" + actualPay +
                ", paymentType=" + paymentType +
                ", promotionIds='" + promotionIds + '\'' +
                ", postFee='" + postFee + '\'' +
                ", createTime=" + createTime +
                ", shippingName='" + shippingName + '\'' +
                ", shippingCode='" + shippingCode + '\'' +
                ", userId=" + userId +
                ", buyerMessage='" + buyerMessage + '\'' +
                ", buyerNick='" + buyerNick + '\'' +
                ", buyerRate=" + buyerRate +
                ", receiver='" + receiver + '\'' +
                ", receiverMobile='" + receiverMobile + '\'' +
                ", receiverState='" + receiverState + '\'' +
                ", receiverCity='" + receiverCity + '\'' +
                ", receiverDistrict='" + receiverDistrict + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", receiverZip='" + receiverZip + '\'' +
                ", invoiceType=" + invoiceType +
                ", sourceType=" + sourceType +
                ", orderDetails=" + orderDetails +
                ", status=" + status +
                '}';
    }
}
