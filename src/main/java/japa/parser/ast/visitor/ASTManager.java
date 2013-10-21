package japa.parser.ast.visitor;

/*
  Copyright (C) 2013 Raquel Pau and Albert Coroleu.

 Walkmod is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Walkmod is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with Walkmod.  If not, see <http://www.gnu.org/licenses/>.*/

import japa.parser.ast.*;

public class ASTManager {


    public static boolean isNewNode(Node node) {
        return (0 == node.getEndColumn()) && (node.getBeginLine() == 0);
    }

    public static boolean isPrevious(Node node, Node previous) {
        if (previous.getEndLine() < node.getBeginLine()) {
            return true;
        } else if ((previous.getEndLine() == node.getBeginLine())
                && (previous.getEndColumn() <= node.getBeginColumn())) {
            return true;
        }
        return false;
    }


    public static boolean contains(Node node1, Node node2) {
        if ((node1.getBeginLine() < node2.getBeginLine())
                || ((node1.getBeginLine() == node2.getBeginLine()) && node1
                .getBeginColumn() <= node2.getBeginColumn())) {
            if (node1.getEndLine() > node2.getEndLine()) {
                return true;
            } else if ((node1.getEndLine() == node2.getEndLine())
                    && node1.getEndColumn() >= node2.getEndColumn()) {
                return true;
            }
        }
        return false;
    }


}