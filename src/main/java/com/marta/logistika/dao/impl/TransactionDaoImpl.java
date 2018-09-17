package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.TransactionDao;
import com.marta.logistika.entity.TransactionEntity;
import org.springframework.stereotype.Repository;

@Repository("transactionRepository")
public class TransactionDaoImpl extends AbstractDao<TransactionEntity> implements TransactionDao {
}
