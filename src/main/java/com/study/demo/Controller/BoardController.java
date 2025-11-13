package com.study.demo.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.demo.DTO.BoardRequest;
import com.study.demo.DTO.BoardResponse;
import com.study.demo.DTO.BoardSearchRequest;
import com.study.demo.DTO.PagingResponse;
import com.study.demo.Service.BoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public String openBoardList(Model model, final BoardSearchRequest params) {

        int totalCount = boardService.countPosts(params);

        List<BoardResponse> posts = boardService.findAllPosts(params);

        PagingResponse<BoardResponse> pagingResponse = new PagingResponse<>(posts, totalCount, params); 

        model.addAttribute("response", pagingResponse);

        model.addAttribute("list", posts);

        model.addAttribute("params", params);

        return "board/list";
    }

    @GetMapping("/write")
    public String openBoardWrite(@RequestParam(required = false) final Long id, Model model) {

        if (id != null) {
            model.addAttribute("post", boardService.findPostById(id));
        } else {
            model.addAttribute("post", new BoardRequest());
        }

        return "board/write";
    }

    @PostMapping("/save")
        public String savePost(@ModelAttribute("post") @Valid final BoardRequest params, BindingResult bindingResult) {
            if (bindingResult.hasErrors()) {
                return "board/write";
            }
            
            boardService.savePost(params);
            
            return "redirect:/board/list";
        }

    @GetMapping("/view/{id}")
    public String openBoardView(@PathVariable final Long id, Model model) {
        model.addAttribute("post", boardService.findPostById(id));

        return "board/view";
    }

    @GetMapping("/edit/{id}")
    public String openBoardEdit(@PathVariable final Long id, Model model) {
        model.addAttribute("post", boardService.findPostById(id));
        return "board/edit";
    }

    @PatchMapping("/edit/{id}")
    public String editPost(@PathVariable final Long id, @Valid final BoardRequest params, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", boardService.findPostById(id));
            return "board/edit";
        }

        boardService.updatePost(params);
        return "redirect:/board/view/" + id;
    }

    @DeleteMapping("/delete/{id}")
    public String deletePost(@PathVariable final Long id) {
        boardService.deletePost(id);
        
        return "redirect:/board/list";
    }


    
}
