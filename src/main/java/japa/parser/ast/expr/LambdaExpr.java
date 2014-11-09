/*
 * Copyright (C) 2014 JavaParser GitHub project
 * 
 * This file is part of Java 1.8 parser and Abstract Syntax Tree.
 *
 * Java 1.8 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.8 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.8 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
package japa.parser.ast.expr;

import japa.parser.ast.body.Parameter;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Federico Tomassetti
 */
public final class LambdaExpr extends Expression {

    private Expression body;

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    private List<Parameter> parameters;

    public Expression getBody() {
        return body;
    }

    public void setBody(Expression body) {
        this.body = body;
    }

	public LambdaExpr(final Expression body) {
		setBody(body);
	}

	public LambdaExpr(final int beginLine, final int beginColumn, final int endLine, final int endColumn,
                      final Expression body, List<Parameter> parameters) {
		super(beginLine, beginColumn, endLine, endColumn);
        setBody(body);
        setParameters(parameters);
	}

	@Override public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
		return v.visit(this, arg);
	}

	@Override public <A> void accept(final VoidVisitor<A> v, final A arg) {
		v.visit(this, arg);
	}

	public List<Expression> getArgs() {
		return args;
	}

	public String getName() {
		return name.getName();
	}

	public NameExpr getNameExpr() {
		return name;
	}

	public Expression getScope() {
		return scope;
	}

	public List<Type> getTypeArgs() {
		return typeArgs;
	}

	public void setArgs(final List<Expression> args) {
		this.args = args;
		setAsParentNodeOf(this.args);
	}

	public void setName(final String name) {
		this.name = new NameExpr(name);
	}

	public void setNameExpr(NameExpr name) {
		this.name = name;
	}

	public void setScope(final Expression scope) {
		this.scope = scope;
		setAsParentNodeOf(this.scope);
	}

	public void setTypeArgs(final List<Type> typeArgs) {
		this.typeArgs = typeArgs;
		setAsParentNodeOf(this.typeArgs);
	}
}
