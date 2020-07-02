package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Inquiry;
import com.example.demo.repository.InquiryDao;

//@ServiceをつけることでDIコンテナの方で自動的にシングルトンとしてインスタンス化される
@Service
public class InquiryServiceImpl implements InquiryService{

		//インターフェース名の型にしておく
	private final InquiryDao dao;
	
	@Autowired
	public InquiryServiceImpl(InquiryDao dao) {
		//実装クラスのインスタンスが代入される形になる
		this.dao = dao;
	}
	
	@Override
	public void save(Inquiry inquiry) {
		dao.insertInquiry(inquiry);
	}

	
	@Override
	public List<Inquiry> getAll() {
			return dao.getAll();
	}

	@Override
	public void update(Inquiry inquiry) {
		if(dao.updateInquiry(inquiry) == 0) {
			throw new InquiryNotFoundException("can't find the same ID");
		}
	}
	
	
	
}
