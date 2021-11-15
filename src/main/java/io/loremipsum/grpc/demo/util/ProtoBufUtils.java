package io.loremipsum.grpc.demo.util;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;


public class ProtoBufUtils {

    private static final JsonFormat jsonFormat = new JsonFormat();

    public static  String toJson(Message sourceMessage) {
        final String string = jsonFormat.printToString(sourceMessage);
        return string;
    }
}
