package com.oxchains.themis.arbitrate.repo;

import com.oxchains.themis.arbitrate.entity.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xuqi on 2017/11/3.
 * @author huohuo
 */
@Repository
public interface PaymentRepo extends CrudRepository<Payment,Long> {

}
