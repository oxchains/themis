package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.BlockChainInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author luoxuri
 * @create 2017-11-02 16:03
 **/
public interface BlockChainInfoDao extends CrudRepository<BlockChainInfo, Long> {

    List<BlockChainInfo> findBySymbol(String symbol);
}
