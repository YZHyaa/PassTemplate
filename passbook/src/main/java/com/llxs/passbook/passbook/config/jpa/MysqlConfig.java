package com.llxs.passbook.passbook.config.jpa;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.springframework.stereotype.Component;
 
@Component
public class MysqlConfig extends MySQL5InnoDBDialect {
	
	@Override
	public String getTableTypeString() {
		return "ENGINE=InnoDB DEFAULT CHARSET=utf8";
	}
}