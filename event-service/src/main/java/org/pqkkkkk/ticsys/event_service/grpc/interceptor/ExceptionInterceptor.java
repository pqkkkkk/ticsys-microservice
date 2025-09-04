package org.pqkkkkk.ticsys.event_service.grpc.interceptor;

import org.springframework.stereotype.Component;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

@Component
@GrpcGlobalServerInterceptor
@Slf4j
public class ExceptionInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        ServerCall.Listener<ReqT> listener = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(listener) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception ex) {
                    log.error("Exception caught in gRPC call: {}", ex);
                    Status status;
                    if (ex instanceof IllegalArgumentException) {
                        status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
                    } else {
                        status = Status.INTERNAL.withDescription("Internal server error");
                    }

                    call.close(status, new Metadata());
                }
            }
        };
    }
}
