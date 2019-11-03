package com.luo.ptn.behavior.memento;

import java.util.Stack;

public class ArticleMementoManager {

    private final Stack<ArticleMemento> ARTICLE_MEMENTO_STACK = new Stack<>();

    public ArticleMemento getArticleMemento() {
        return ARTICLE_MEMENTO_STACK.pop();
    }

    public void addArticleMento(ArticleMemento articleMemento) {
        ARTICLE_MEMENTO_STACK.push(articleMemento);
    }
}
