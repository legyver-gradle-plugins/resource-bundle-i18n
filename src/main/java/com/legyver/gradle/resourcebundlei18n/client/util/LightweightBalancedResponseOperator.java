package com.legyver.gradle.resourcebundlei18n.client.util;

public class LightweightBalancedResponseOperator {
    private final char l;
    private final char r;

    public LightweightBalancedResponseOperator(char l, char r) {
        this.l = l;
        this.r = r;
    }

    public String trimBalanced(String original) {
        String result = original;
        int indexL = original.indexOf(l);
        int indexR = original.lastIndexOf(r);
        if (indexL > -1 && indexR > indexL + 1) {
            result = result.substring(indexL + 1, indexR);
        } else if (indexL > -1 && indexR == indexL + 1) {
            //empty, '[]', '{}', etc
            result = "";
        }
        return result.trim();
    }
}
