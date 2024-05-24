package id.ac.ui.cs.advprog.eshop.mcstracking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "SHIPPING")
@Entity
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "SHIPPING_METHOD")
    private String shippingMethod;

    @Column(name = "RECEIPT_CODE")
    private String receiptCode;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;
}
