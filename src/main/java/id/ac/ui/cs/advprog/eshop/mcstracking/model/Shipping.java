package id.ac.ui.cs.advprog.eshop.mcstracking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "SHIPPING")
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private Long orderId;

    private Long paymentId;

    private Long userId;

    private String shippingMethod;

    private String receiptCode;

    private String status;
}
