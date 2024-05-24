package id.ac.ui.cs.advprog.eshop.mcstracking.repository;

import id.ac.ui.cs.advprog.eshop.mcstracking.model.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    Shipping findByOrderId(Long orderId);

    Shipping findByIdAndUserId(Long id, Long userId);

    Shipping findByPaymentId(Long paymentId);

    Shipping findByStatus(String status);

    List<Shipping> findByUserIdOrderByCreatedDateDesc(Long userid);

    @Query(value = "UPDATE PAYMENT  " +
            "SET VALIDATION_STATUS = 'VALID' " +
            "WHERE id=:paymentId", nativeQuery = true)
    void updatePaymentValid(@Param("paymentId") Long paymentId);

    @Query(value = "UPDATE PAYMENT  " +
            "SET VALIDATION_STATUS = 'NOT_VALID' " +
            "WHERE id=:paymentId", nativeQuery = true)
    void updatePaymentNotValid(@Param("paymentId") Long paymentId);

    @Query(value = "SELECT ORDER_ID FROM PAYMENT  " +
            "WHERE id=:paymentId", nativeQuery = true)
    Long getOrderIdByPayment(@Param("paymentId") Long paymentId);

    @Query(value = "UPDATE \"order\"  " +
            "SET STATUS = 'CANCELLED' " +
            "WHERE id=:orderId",
            nativeQuery = true)
    void updateOrderStatusCancelled(@Param("orderId") Long orderId);

    @Query(value = "UPDATE \"order\"  " +
            "SET STATUS = 'IN_PROGRESS' " +
            "WHERE id=:orderId",
            nativeQuery = true)
    void updateOrderStatusInProgress(@Param("orderId") Long orderId);

    @Query(value = "UPDATE \"order\"  " +
            "SET STATUS = 'DONE' " +
            "WHERE id=:orderId",
            nativeQuery = true)
    void updateOrderStatusDone(Long orderId);
}
