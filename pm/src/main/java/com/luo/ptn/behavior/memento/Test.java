package com.luo.ptn.behavior.memento;

public class Test {
    public static void main(String[] args) {
        ArticleMementoManager mementoManager = new ArticleMementoManager();
        Article article = new Article("java", "java se", "d");

        ArticleMemento articleMemento = article.saveToMemento();

        mementoManager.addArticleMento(articleMemento);
        System.out.println("article:" + article);
        System.out.println("修改他");

        article.setTitle("java 2");
        article.setContent("javase 2");
        article.setImgs("d2");
        System.out.println("修改他end");

        articleMemento = article.saveToMemento();
        mementoManager.addArticleMento(articleMemento);
        article.setTitle("java 3");
        article.setContent("javase 3");
        article.setImgs("d3");
        System.out.println("回退1次");
        articleMemento = mementoManager.getArticleMemento();
        article.undoMemento(articleMemento);
        System.out.println("回退2次");
        articleMemento = mementoManager.getArticleMemento();
        article.undoMemento(articleMemento);

        System.out.println("last art:" + article.getContent() + "," + article.getTitle());

    }
}
