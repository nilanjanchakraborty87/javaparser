package japa.parser.ast.visitor;

import java.util.List;
import java.util.LinkedList;
import japa.parser.ast.Comment;
import japa.parser.ast.Node;

public class CommentsCollectorTreeVisitor extends TreeVisitor {

    private List<Comment> comments = new LinkedList<Comment>();

    @Override
    protected void visitAction(Node node) {
        Comment comment = node.getComment();
        if (comment!=null){
            System.out.println("COMMENT "+comment);
            comments.add(comment);
        }
    }

    public List<Comment> getComments(){
        return comments;
    }
}
