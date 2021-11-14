package io.loremipsum.grpc.demo;


import io.grpc.CallCredentials;
import io.grpc.stub.StreamObserver;
import io.loremipsum.grpc.demo.proto.Demo;
import io.loremipsum.grpc.demo.proto.GreeterBlockingGrpc;


import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GrpcDemoApplicationTests {

	@GrpcClient("greeter-java")
	GreeterBlockingGrpc.GreeterBlockingBlockingStub greeterBlockingStub;

	@Test
	void TestGrpcBlockingInvoke(){

		Demo.HelloReply helloWorld = greeterBlockingStub.sayHello(Demo.HelloRequest.newBuilder().setName("HelloWorld").build());

		System.out.println("done");



	}

}
