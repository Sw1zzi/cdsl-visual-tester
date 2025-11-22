package com.morro.cdsl.parser;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    private String type;
    private Object value;
    private List<ASTNode> children;

    public ASTNode(String type) {
        this.type = type;
        this.children = new ArrayList<>();
    }

    public ASTNode(String type, Object value) {
        this.type = type;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }

    public List<ASTNode> getChildren() { return children; }
    public void addChild(ASTNode node) { children.add(node); }

    public ASTNode getChild(int index) {
        return children.size() > index ? children.get(index) : null;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
        sb.append(type);
        if (value != null) {
            sb.append(": ").append(value);
        }
        sb.append("\n");

        for (ASTNode child : children) {
            sb.append(child.toString(indent + 1));
        }

        return sb.toString();
    }
}
