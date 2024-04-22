package com.danli.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danli.annotation.AccessLimit;
import com.danli.annotation.VisitLogger;
import com.danli.common.lang.Result;
import com.danli.common.lang.vo.PageComment;
import com.danli.entity.Comment;
import com.danli.service.CommentService;
import com.danli.service.MailService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;


@RestController
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    MailService mailService;
    Logger logger = LoggerFactory.getLogger(CommentController.class);





    /**
     * 获取某个博客下的所有评论
     */
    @GetMapping("/comment/{blogId}")
    public Result getCommentByBlogId(@PathVariable(name = "blogId") Long blogId) {

        //实体模型集合对象转换为VO对象集合
        List<PageComment> pageComments = commentService.getPageCommentListByDesc(blogId, (long) -1);

        for (PageComment pageComment : pageComments) {

            List<PageComment> reply = commentService.getPageCommentList(blogId, pageComment.getId());
            pageComment.setReplyComments(reply);
        }
        //Assert.notNull(blog, "该博客已删除！");
        return Result.succ(pageComments);

    }



    /**
     * 提交评论
     */
    @AccessLimit(seconds = 30, maxCount = 1, msg = "30秒内只能提交一次评论")
    @VisitLogger(behavior = "提交评论")
    @PostMapping("/comment/add")
    public Result edit(@Validated @RequestBody Comment comment, HttpServletRequest request) {

        if (comment.getContent().contains("<script>") || comment.getEmail().contains("<script>") || comment.getNickname().contains("<script>") || comment.getWebsite().contains("<script>")) {
            return Result.fail("非法输入");
        }
        System.out.println(comment.toString());
        Comment temp = new Comment();
        temp.setCreateTime(LocalDateTime.now());
        temp.setIp(request.getHeader("x-forwarded-for"));
        BeanUtil.copyProperties(comment, temp, "id", "ip", "createTime");
        commentService.saveOrUpdate(temp);


        //博主的回复向被回复者发送提示邮件
        if(comment.getIsAdminComment()==1&&comment.getParentCommentId()!=-1){
            Comment parentComment = commentService.getOne(new QueryWrapper<Comment>().eq("nickname", comment.getParentCommentNickname()));
            String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
            if (parentComment.getEmail().matches(regex)) {
                mailService.sendSimpleMail(parentComment.getEmail(), "Skymo博客评论回复", "您的的评论："+parentComment.getContent()+"\n博主回复内容："+comment.getContent());
                logger.info("邮件发送成功");
            }

        }

        return Result.succ(null);
    }

}
