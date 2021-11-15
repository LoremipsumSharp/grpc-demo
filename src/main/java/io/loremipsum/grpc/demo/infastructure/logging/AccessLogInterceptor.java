package io.loremipsum.grpc.demo.infastructure.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.google.common.base.Stopwatch;
import com.google.protobuf.Message;
import io.grpc.*;
import io.grpc.internal.GrpcUtil;
import io.loremipsum.grpc.demo.util.DateUtils;
import io.loremipsum.grpc.demo.util.NetworkUtils;
import io.loremipsum.grpc.demo.util.ProtoBufUtils;
import lombok.Builder;
import lombok.Data;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Builder
@Data
class AccessLog {
    private String time;
    private String status;
    private long elapsed_time;
    private String client_ip;
    private String user_agent;
    private String interface_name;
    private String request_content;
    private String response_content;
    private Map<String, String> request_header;
    private Map<String, String> response_header;
    private String msg;
    private String level;
    private String trace_id;
}

@Component
@GrpcGlobalServerInterceptor
public class AccessLogInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger("accesslog");
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {

        final Stopwatch stopwatch = Stopwatch.createStarted();
        final String interfaceName = serverCall.getMethodDescriptor().getFullMethodName();
        final String clientIp = NetworkUtils.getPeerIp(serverCall.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR));
        final String userAgent = Optional.ofNullable(metadata.get(GrpcUtil.USER_AGENT_KEY)).orElse("unknown-user-agent");
        final String traceId = UUID.randomUUID().toString();
        final MethodDescriptor.MethodType type = serverCall.getMethodDescriptor().getType();

        AccessLog.AccessLogBuilder builder = AccessLog.builder();
        builder.interface_name(interfaceName).client_ip(clientIp).user_agent(userAgent)
                .request_header(headersToMap(metadata))
                .trace_id(traceId)
                .time(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS_fff));
        final ServerCall<ReqT, RespT> wrappedCall = new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(serverCall) {

            @Override
            public void close(Status status, Metadata trailers) {

                builder.elapsed_time(stopwatch.elapsed(TimeUnit.MILLISECONDS))
                        .status(status.getCode().name());
                try {
                    if (status != Status.OK) {
                        builder.level("Error")
                                .msg(status.asException().toString());
                        log.error(mapper.writeValueAsString(builder.build()));
                    } else {
                        builder.level("Info");

                        log.info(mapper.writeValueAsString(builder.build()));
                    }
                } catch (JsonProcessingException e) {
                    log.error("error occure while trying to write the access log");
                }

                super.close(status, trailers);

            }

            @Override
            public void sendMessage(RespT message) {
                builder.response_content(ProtoBufUtils.toJson((Message) message));
                super.sendMessage(message);
            }

            @Override
            public void sendHeaders(Metadata headers) {
                builder.response_header(headersToMap(headers));
                super.sendHeaders(headers);
            }
        };


        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(
                serverCallHandler.startCall(wrappedCall, metadata)) {

            @Override
            public void onMessage(ReqT message) {
                builder.request_content(ProtoBufUtils.toJson((Message) message));
                super.onMessage(message);
            }
        };


    }

    private static Map<String, String> headersToMap(Metadata headers) {
        Map<String, String> headersMap = headers.keys().stream()
                .filter(key -> !key.endsWith(Metadata.BINARY_HEADER_SUFFIX))
                .map(key -> Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER))
                .collect(Collectors.toMap(Metadata.Key::name, headers::get));
        return headersMap;
    }


}
