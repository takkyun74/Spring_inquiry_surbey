package com.example.demo.app.inquiry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Inquiry;
import com.example.demo.service.InquiryNotFoundException;
import com.example.demo.service.InquiryService;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {
	
	private final InquiryService inquiryService;
	
	@Autowired
	public InquiryController(InquiryService inquiryService) {
		this.inquiryService = inquiryService;
	}
	
	//お問い合わせ一覧を表示する機能
	@GetMapping
	public String index(Model model) {
		List<Inquiry> list = inquiryService.getAll();
		
//		Inquiry inquiry = new Inquiry();
//		inquiry.setId(4);
//		inquiry.setName("Jamie");
//		inquiry.setEmail("sample4@example.com");
//		inquiry.setContents("Hello.");
//		
//		inquiryService.update(inquiry);
		
//		try {
//			inquiryService.update(inquiry);
//		} catch (InquiryNotFoundException e) {
//			model.addAttribute("message", e);
//			return "error/CustomPage";
//		}
		
		model.addAttribute("inquiryList", list);
		model.addAttribute("title", "Inquiry Index");
		
		return "inquiry/index_boot";
	}
	//URLでアクセス
	@GetMapping("/form")
	public String form(InquiryForm inquiryForm, Model model,
			//フラッシュスコープを受け取る場合はアノテーションを使う
			//フラッシュスコープを設定したときのkeyのcompleteを入れる
			@ModelAttribute("complete") String complete)
			{
		model.addAttribute("title", "Inquiry Form");
		return "inquiry/form_boot";
	}
	//戻るボタンでアクセス
	@PostMapping("/form")
	public String formGoBack(InquiryForm inquiryForm, Model model) {
		model.addAttribute("title", "InquiryForm");
		return "inquiry/form_boot";
	}
	
	
	@PostMapping("/confirm")
	public String confirm(@Validated InquiryForm inquiryForm, BindingResult result, Model model) {
		//エラーがあった場合、フォームの画面に戻るように
		if(result.hasErrors()) {
			model.addAttribute("title", "inquiry Form");
			return "inquiry/form_boot";
		}
		model.addAttribute("title", "Confirm Page");
		return "inquiry/confirm_boot";
	}
	
	@PostMapping("/complete")
	//@V~ 確認フォームの内容は裏でいじることができる
	public String complete(@Validated InquiryForm inquiryForm, BindingResult result, Model model,
			//フラッシュスコープを使うため
			//フラッシュスコープはセッションを使ってできている
			//セッションはページ間を飛び越えてデータを保持
			RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			model.addAttribute("title", "InquiryForm");
			return "inquiry/form_boot";
		}
		
		//リダイレクトする前のタイミングでDB操作を入れていく
		//InquiryFormクラスからInquiryというEntityのクラスにデータを詰め替える必要がある
		//フォームの内容がテーブル2つにまたがっている場合などは重要
		Inquiry inquiry = new Inquiry();
		inquiry.setName(inquiryForm.getName());
		inquiry.setEmail(inquiryForm.getEmail());
		inquiry.setContents(inquiryForm.getContents());
		inquiry.setCreated(LocalDateTime.now());
		
		//DBに登録
		inquiryService.save(inquiry);
		
		//model.addAttributeの代わりになる
		//セッションにデータが格納されている状態
		redirectAttributes.addFlashAttribute("complete", "Registered!");	
		//htmlファイルをさしているんじゃなくてURLをさしている
		//クライアントに一度レスポンスを返し、クライアントから再びリクエストがとんでくる
		//リクエストスコープではデータをつなぐことができない
		return "redirect:/inquiry/form";
	}
	
//	@ExceptionHandler(InquiryNotFoundException.class)
//	public String handleException(InquiryNotFoundException e, Model model) {
//		model.addAttribute("message", e);
//		return "error/CustomPage";
//	}
	
}
