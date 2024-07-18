package leets.weeth.domain.post.controller;

import leets.weeth.domain.post.dto.RequestCommentDTO;
import leets.weeth.domain.post.dto.ResponseCommentDTO;
import leets.weeth.domain.post.service.CommentService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.error.exception.custom.InvalidAccessException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping()
    public CommonResponse<String> create(@PathVariable Long postId, @RequestBody RequestCommentDTO dto,
                                         @CurrentUser Long userId){
        commentService.create(userId, postId, dto);
        return CommonResponse.createSuccess();
    }

    @GetMapping()
    public CommonResponse<List<ResponseCommentDTO>> getAllCommentsFromPost(@PathVariable Long postId){
        List<ResponseCommentDTO> comments = commentService.getAllCommentsFromPost(postId);
        return CommonResponse.createSuccess(comments);
    }

    @PatchMapping("/{commentId}")
    public CommonResponse<String> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @CurrentUser Long userId,
                                                @RequestBody RequestCommentDTO dto) throws InvalidAccessException {
        commentService.updateComment(userId, postId, commentId, dto);
        return CommonResponse.createSuccess();
    }

    @DeleteMapping("/{commentId}")
    public CommonResponse<String> delete(@PathVariable Long commentId, @CurrentUser Long userId) throws InvalidAccessException {
        commentService.delete(userId, commentId);
        return CommonResponse.createSuccess();
    }


}
