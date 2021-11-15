IMAGE_NAME = loremipsumsharp/grpc-demo
CURRENT_VERSION := $(shell xmllint --xpath "/*[local-name() = 'project']/*[local-name() = 'version']/text()" pom.xml)
RELEASE_VERSION  :=  $(shell echo $(CURRENT_VERSION) | perl -pe 's/-SNAPSHOT//')

clean:
	mvn clean
build:clean
	mvn compile
run: build
	mvn exec:java -Dexec.mainClass=io.loremipsum.camel.data.distribution.CamelDataDistributionApplication
pack: clean build
	 mvn package spring-boot:repackage -Dmaven.test.skip=true

build-image:pack
	docker build -f Dockerfile --no-cache --rm -t $(IMAGE_NAME):$(RELEASE_VERSION) .
push-image:build-image
	docker push $(IMAGE_NAME):$(RELEASE_VERSION)

test:
	 grpcurl -plaintext -d '{"name":"hello"}' localhost:61018 greeter.GreeterBlocking/SayHello
