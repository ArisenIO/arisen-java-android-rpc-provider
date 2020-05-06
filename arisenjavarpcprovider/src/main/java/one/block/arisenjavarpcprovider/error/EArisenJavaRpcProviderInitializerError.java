package one.block.arisenjavarpcprovider.error;

import one.block.arisenjava.error.rpcProvider.RpcProviderError;
import org.jetbrains.annotations.NotNull;

//
// Copyright © 2017-2019 block.one.
//

/**
 * Error thrown when there is an issue initializing the RPC Provider.
 */
public class ArisenJavaRpcProviderInitializerError extends RpcProviderError {

    public ArisenJavaRpcProviderInitializerError() {
    }

    public ArisenJavaRpcProviderInitializerError(
            @NotNull String message) {
        super(message);
    }

    public ArisenJavaRpcProviderInitializerError(
            @NotNull String message,
            @NotNull Exception exception) {
        super(message, exception);
    }

    public ArisenJavaRpcProviderInitializerError(
            @NotNull Exception exception) {
        super(exception);
    }

}
