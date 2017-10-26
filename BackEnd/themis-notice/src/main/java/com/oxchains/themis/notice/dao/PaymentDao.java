package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.Notice;
import com.oxchains.themis.notice.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Luo_xuri on 2017/10/20.
 */
@Repository
public interface PaymentDao extends CrudRepository<Payment,Long> {

}