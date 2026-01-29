package com.electricitybilling.billing.repository;

import com.electricitybilling.billing.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByConsumerId(String consumerId);

    List<Bill> findByConsumerIdOrderByBillDateDesc(String consumerId);
}
