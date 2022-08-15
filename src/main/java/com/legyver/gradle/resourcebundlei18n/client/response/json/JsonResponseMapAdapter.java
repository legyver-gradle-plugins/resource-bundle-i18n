package com.legyver.gradle.resourcebundlei18n.client.response.json;

import com.legyver.gradle.resourcebundlei18n.client.util.LightweightBalancedResponseOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonResponseMapAdapter implements JsonResponseAdapter<Map> {
    private final LightweightBalancedResponseOperator bracketTrimmer = new LightweightBalancedResponseOperator('[', ']');
    private final LightweightBalancedResponseOperator curlyBracketTrimmer = new LightweightBalancedResponseOperator('{', '}');
    private static final Pattern endsNonWord = Pattern.compile("((\\W)+)$");

    @Override
    public Map<String, String> adapt(String responseAsString) {
        Map<String, String> valueMap = new HashMap<>();
        if (responseAsString != null && !responseAsString.isBlank()) {
            //[
            //  {
            //    "confidence": 92,
            //    "language": "en"
            //  }
            //]
            //  {
            //      "translatedText": "hola, senor"
            //  }
            responseAsString = bracketTrimmer.trimBalanced(responseAsString);
            responseAsString = curlyBracketTrimmer.trimBalanced(responseAsString);

            Scanner scanner = new Scanner(responseAsString);

            String variable = null;
            String value = null;
            Scope scope = null;
            while (scanner.hasNext()) {
                String next = scanner.next();
                if ("null".equals(next) || "null,".equals(next)) {
                    //literal null - will only be a value
                    value = null;
                    valueMap.put(variable, value);
                    variable = null;
                } else {
                    int offset = 0;
                    if (next.startsWith("\"")) {
                        offset = 1;
                    }

                    Matcher m = endsNonWord.matcher(next);
                    if (m.find()) {
                        String nonWordEnding = m.group(0);
                        if ("\":".equals(nonWordEnding)) {
                            scope = Scope.VARIABLE;
                            next = next.substring(offset, next.length() - nonWordEnding.length());
                        } else if ("\",".equals(nonWordEnding) //end string value followed by a comma
                                || "\"".equals(nonWordEnding) //end string value
                                || (offset == 0 && ",".equals(nonWordEnding))) {//boolean and integer values won't have started with a quote
                            scope = scope.VALUE;
                            next = next.substring(offset, next.length() - nonWordEnding.length());
                        } else {
                            scope = Scope.OPEN;
                            if (offset > 0) {
                                next = next.substring(offset);
                            }
                        }
                    }

                    if (Scope.VARIABLE == scope) {
                        variable = next;
                    } else if (Scope.VALUE == scope) {
                        value = value == null ? next : value + " " + next;
                        valueMap.put(variable, value);
                        variable = null;
                        value = null;
                    } else if (Scope.OPEN == scope) {
                        value = value == null ? next : value + " " + next;
                    }
                }
            }
        }
        return valueMap;
    }

    private enum Scope {
        VARIABLE,
        VALUE,
        OPEN
    }

}
