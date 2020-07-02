package com.example.demo.app.inquiry;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InquiryForm{
	
	public InquiryForm() {};

    public InquiryForm(String name, String email, String contents) {
		super();
		this.name = name;
		this.email = email;
		this.contents = contents;
	}



	@Size(min = 1, max = 20, message="入力されていないか、20文字をこえています")
    private String name;
    
    @NotNull
    @Email(message = "フォーマットが正しくありません")
    private String email;

    @NotNull
    private String contents;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}


}