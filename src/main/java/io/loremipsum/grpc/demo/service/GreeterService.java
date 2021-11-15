package io.loremipsum.grpc.demo.service;

import io.grpc.stub.StreamObserver;


import io.loremipsum.grpc.demo.proto.Demo;
import io.loremipsum.grpc.demo.proto.GreeterBlockingGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
public class GreeterService extends GreeterBlockingGrpc.GreeterBlockingImplBase {

    @Override
    public void sayHello(Demo.HelloRequest request, StreamObserver<Demo.HelloReply> responseObserver) {
        log.info("received request!");
        responseObserver.onNext(Demo.HelloReply.newBuilder().setMessage(request.getName()).build());
        responseObserver.onCompleted();
    }
}
