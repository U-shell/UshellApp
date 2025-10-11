package ru.ushell.app.data.common.webSocket.stomp;


import java.util.Map;

public class StompMessageSerializer {

    public static String serialize(StompMessage message) {

        StringBuilder buffer = new StringBuilder();

        buffer.append(message.getCommand()).append("\n");

        for (Map.Entry<String, String> header : message.getHeaders().entrySet()) {
            buffer.append(header.getKey()).append(":").append(header.getValue()).append("\n");
        }

        buffer.append("\n");
        buffer.append(message.getContent());
        buffer.append('\0');

        return buffer.toString();
    }

    public static StompMessage deserialize(String rawMessage) {
        if (rawMessage == null || rawMessage.isEmpty()) {
            throw new IllegalArgumentException("Raw message is empty");
        }

        // Удаляем завершающий нулевой байт, если есть
        if (rawMessage.charAt(rawMessage.length() - 1) == '\0') {
            rawMessage = rawMessage.substring(0, rawMessage.length() - 1);
        }

        // Находим позицию тела: после первой пустой строки
        int bodyStart = rawMessage.indexOf("\n\n");
        String headersPart;
        String body;

        if (bodyStart != -1) {
            headersPart = rawMessage.substring(0, bodyStart);
            body = rawMessage.substring(bodyStart + 2); // +2 для пропуска \n\n
        } else {
            // Резерв: если нет \n\n, ищем начало JSON
            int jsonStart = rawMessage.indexOf('{');
            if (jsonStart == -1) jsonStart = rawMessage.indexOf('[');
            if (jsonStart != -1) {
                headersPart = rawMessage.substring(0, jsonStart).trim();
                body = rawMessage.substring(jsonStart);
            } else {
                headersPart = rawMessage;
                body = "";
            }
        }

        String[] headerLines = headersPart.split("\n");
        if (headerLines.length == 0) {
            throw new IllegalArgumentException("Invalid STOMP frame");
        }

        String command = headerLines[0].trim();
        StompMessage result = new StompMessage(command);

        // Парсим заголовки (начиная со 2-й строки)
        for (int i = 1; i < headerLines.length; i++) {
            String line = headerLines[i].trim();
            if (line.isEmpty()) continue;

            int colonIndex = line.indexOf(':');
            if (colonIndex > 0) {
                String key = line.substring(0, colonIndex);
                String value = line.substring(colonIndex + 1);
                result.put(key, value);
            }
        }

        result.setContent(body.trim());
        return result;
    }

}
