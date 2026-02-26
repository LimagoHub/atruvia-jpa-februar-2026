package de.atruvia.projekt_02.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class OrderDetailId implements Serializable {
    private static final long serialVersionUID = 7631333704662130749L;
    @Column(name = "OrderID", nullable = false)
    private Integer orderID;
    @Column(name = "ProductID", nullable = false)
    private Integer productID;

    @Override
    public int hashCode() {
        return Objects.hash(productID, orderID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderDetailId entity = (OrderDetailId) o;
        return Objects.equals(this.productID, entity.productID) &&
                Objects.equals(this.orderID, entity.orderID);
    }
}