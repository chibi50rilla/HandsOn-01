package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Review;
import com.example.demo.form.ReviewRemoveForm;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RemoveController {
	
	/*--- レビュー削除リクエスト（一覧画面より） ---*/
	@PostMapping("/remove-review")
	public String removeReview(
			@Validated @ModelAttribute ReviewRemoveForm form,
			BindingResult result) {

		// 項目内容にエラーがある場合には 例外を発生させる
		if (result.hasErrors()) {
			throw new IllegalArgumentException("**removeReview()**");
		}
		
		// 正常な場合に レビュー削除確認画面に 遷移する
		return "confirm-remove-review";
	}
	
	/*--- レビュー削除リクエスト（削除確認画面より） ---*/
	@PostMapping("/confirm-remove-review")
	public String confirmRemoveReview(
			@Validated ReviewRemoveForm form,
			BindingResult result,
			RedirectAttributes redirectAttributes) {

		// 項目内容にエラーがある場合には 例外を発生させる
		if (result.hasErrors()) {
			throw new IllegalArgumentException("**confirmRemoveReview()**");
		}

		Review r = new Review();
		r.setReviewId(form.getReviewId());
		r.setRestaurantId(form.getRestaurantId());
		r.setUserId(form.getUserId());
		r.setVisitDate(form.getVisitDate());
		r.setRating(form.getRating());
		r.setComment(form.getComment());
		
		// 暫定で表示
		System.out.println("--レビュー削除");
		System.out.println(r);
		// 暫定で表示(End)
				
		redirectAttributes.addFlashAttribute("msg", "(レビュー削除)");
		
		return "redirect:/complete";
	}

}
