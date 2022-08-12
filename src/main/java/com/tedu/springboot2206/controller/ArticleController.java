package com.tedu.springboot2206.controller;

import com.tedu.springboot2206.entity.Article;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ArticleController {
    private static final File articleDir;

    static{
        articleDir = new File("./articles");
        if (!articleDir.exists()){
            articleDir.mkdirs();
        }
    }

    @RequestMapping("writeArticle")
    public void writeArticle(HttpServletRequest request, HttpServletResponse response){
        String author = request.getParameter("author");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        if (author==null||author.trim().isEmpty() ||
                title==null||title.trim().isEmpty()||
                content==null||content.trim().isEmpty()){
            try {
                response.sendRedirect("/article_fail.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }


        Article article = new Article(author,title,content);
        File file = new File("./articles/"+article.getTitle()+".obj");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))
        ){
            oos.writeObject(article);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response.sendRedirect("/article_success.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/articleList")
    public void articleList(HttpServletRequest request,HttpServletResponse response){
        List<Article>articleList = new ArrayList<>();
        File[] file = articleDir.listFiles(f->f.getName().endsWith(".obj"));
        for (File files : file){
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(files));
            ) {
                Article article = (Article) ois.readObject();
                articleList.add(article);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter pw = response.getWriter();
            pw.println("<!DOCTYPE html>");
            pw.println("<html lang=\"en\">");
            pw.println("<head>");
            pw.println("<meta charset=\"UTF-8\">");
            pw.println("<title>文章列表</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<center>");
            pw.println("<h1>文章列表</h1>");
            pw.println("<h5><a href=\"/index.html\">返回主页</a></h5>\n");
            pw.println("<table border=\"1\">");
            pw.println("<tr>");
            pw.println("<td>作者</td>");
            pw.println("<td>标题</td>");
            pw.println("<td>操作</td>");
            pw.println("</tr>");
            for (Article article : articleList){
                pw.println("<tr>");
                pw.println("<td>"+article.getAuthor()+"</td>");
                pw.println("<td>"+article.getTitle()+"</td>");
                pw.println("<td><a href='/deleteArticle?title="+article.getTitle()+"'>删除</a></td>");
                pw.println("</tr>");
            }
            pw.println("</table>");
            pw.println("</center>");
            pw.println("</body>");
            pw.println("</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("deleteArticle")
    public void delete(HttpServletRequest request,HttpServletResponse response){
        String title = request.getParameter("title");
        File file = new File("./articles/"+title+".obj");
        file.delete();
        try {
            response.sendRedirect("/articleList");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
