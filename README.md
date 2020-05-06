![Java Logo](img/java-logo.png)
# ARISEN SDK for Java: Android RPC Provider ![ARISEN Alpha](https://img.shields.io/badge/ARISEN-Alpha-blue.svg)

[![Software License](https://img.shields.io/badge/license-MIT-lightgrey.svg)](/./LICENSE)
![Language Java](https://img.shields.io/badge/Language-Java-yellow.svg)
![](https://img.shields.io/badge/Deployment%20Target-Android%206%2B-blue.svg)

An Android [RPC provider](https://github.com/ARISEN/arisen-java/tree/master#rpc-provider-protocol) implementation for use within [ARISEN SDK for Java](https://github.com/ARISEN/arisen-java) as a plugin. RPC providers are responsible for all [RPC calls to aos](https://developers.arisen.io/arisen-aos/reference), as well as general network handling.

_All product and company names are trademarks™ or registered® trademarks of their respective holders. Use of them does not imply any affiliation with or endorsement by them._

## Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Direct Usage](#direct-usage)
- [Android Example App](#android-example-app)
- [Releases](#releases)
- [Want to Help?](#want-to-help)
- [License & Legal](#license)

## Prerequisites

* Android Studio 3.3.2 or higher
* Gradle 4.10.1+
* Gradle Plugin 3.3.0+
* For Android, Android 6 (Marshmallow)+

This project relies on platform functionality and libraries only present in Android 6+. Therefore, any project depending on Android RPC Provider with [ARISEN SDK for Java](https://github.com/ARISEN/arisen-java) **must be an Android 6+ project**. Other RPC providers, however, can be created to support earlier Android versions or other platforms. If your project requires earlier Android version or alternate platform support, or if you'd like to create a RPC provider and have questions, please reach out to us by [logging an issue](/../../issues/new).

## Installation

Android RPC Provider is intended to be used in conjunction with [ARISEN SDK for Java](https://github.com/ARISEN/arisen-java) as a provider plugin.

To use Android RPC Provider with ARISEN SDK for Java in your app, add the following modules to your `build.gradle`:

```groovy
implementation 'one.block:arisenjava:0.1.2'
implementation 'one.block:arisenjavarpcprovider:0.1.1'
```

You must also add the following to the `android` section of your application's `build.gradle`:

```groovy
// Needed to get bitcoin-j to produce a valid apk for android.
packagingOptions {
    exclude 'lib/x86_64/darwin/libscrypt.dylib'
    exclude 'lib/x86_64/freebsd/libscrypt.so'
    exclude 'lib/x86_64/linux/libscrypt.so'
}
```
The `build.gradle` files for the project currently include configurations for publishing the project to Artifactory.  These should be removed if you are not planning to use Artifactory or you will encounter build errors.  To do so, make the changes marked by comments throughout the files.

Then refresh your gradle project.

Now Android RPC Provider is ready for use within ARISEN SDK for Java according to the [ARISEN SDK for Java - Basic Usage instructions](https://github.com/ARISEN/arisen-java/tree/master#basic-usage).

## Direct Usage

If you wish to use Android RPC Provider directly, its public methods can be called like this:

```java
// Synchronous call
ArisenJavaRpcProviderImpl rpcProvider = new ArisenJavaRpcProviderImpl(
    "https://mytestblockchain.net/"
);
GetRawAbiRequest request = new GetRawAbiRequest("arisen.token");
GetRawAbiResponse response = rpcProvider.getRawAbi(request);
String abi = response.getAbi();
String abiHash = response.getAbiHash();

// Asynchronous call
final ArisenJavaRpcProviderImpl rpcProvider = new ArisenJavaRpcProviderImpl(
    "https://mytestblockchain.net/"
);
GetBlockRequest[] request = { new GetBlockRequest("25260032") };

AsyncTask<GetBlockRequest, Void, GetBlockResponse> asyncTask = new AsyncTask<GetBlockRequest, Void, GetBlockResponse>() {
    GetBlockRpcError getBlockError = null;
    @Override
    protected GetBlockResponse doInBackground(GetBlockRequest... getBlockRequests) {
        // Here we are on a background thread.
        GetBlockResponse response = null;
        try {
            response = rpcProvider.getBlock(getBlockRequests[0]);
        } catch (GetBlockRpcError err) {
            getBlockError = err;
        }
        return response;
    }

    protected void onPostExecute(GetBlockResponse response) {
        // Here we are back on the main thread and could update the UI.
        String blockId = response.getId();
        String blockRefPrefix = response.getRefBlockPrefix();
        // ...

    }
}.execute(request);
```

Please note that only the following five RPC endpoints have proper response marshalling:

```java
 Call<GetInfoResponse> getInfo();
 Call<GetBlockResponse> getBlock(@Body GetBlockRequest getBlockRequest);
 Call<GetRawAbiResponse> getRawAbi(@Body GetRawAbiRequest getRawAbiRequest);
 Call<GetRequiredKeysResponse> getRequiredKeys(@Body GetRequiredKeysRequest getRequiredKeysRequest);
 Call<PushTransactionResponse> pushTransaction(@Body PushTransactionRequest pushTransactionRequest);
```

The remaining endpoints accept a `RequestBody` as the request object and return a raw JSON string as the response. We aim to continue improving response marshalling for all endpoints, and we invite you to [help us improve](https://github.com/ARISEN/arisen-java-android-rpc-provider/issues/22) responses too. Check [ArisenJavaRpcProviderImpl](https://github.com/ARISEN/arisen-java-android-rpc-provider/blob/master/arisenjavarpcprovider/src/main/java/one/block/arisenjavarpcprovider/implementations/ArisenJavaRpcProviderImpl.java) for more details.

Here is an example demonstrating how to call RPC endpoints and handle the raw JSON string responses returned:

```java
ArisenJavaRpcProviderImpl rpcProvider = new ArisenJavaRpcProviderImpl("https://mytestblockchain.net/");

String getCurrentBalanceRequestJSON = "{\n" +
            "\t\"code\" : \"arisen.token\"\n" +
            "\t\"account\" : \"test_account\"\n" +
            "}";
RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), getCurrentBalanceRequestJSON);
String response = rpcProvider.getCurrencyBalance(requestBody);
JSONArray jsonArray = new JSONArray(response);
String balance = jsonArray.getString(0);
```

## Android Example App

If you'd like to see ARISEN SDK for Java: Android RPC Provider in action, check out our open source [Android Example App](https://github.com/ARISEN/arisen-java-android-example-app)--a working application that fetches an account's token balance and pushes a transfer action.

## Releases

2/26/20

Version 0.1.1 The version consumes the new arisen-java library version 0.1.2.

## Want to help?

Interested in contributing? That's awesome! Here are some [Contribution Guidelines](./CONTRIBUTING.md) and the [Code of Conduct](./CONTRIBUTING.md#conduct).

We're always looking for ways to improve ARISEN SDK for Java: Android RPC Provider. Check out our [#enhancement Issues](/../../issues?q=is%3Aissue+is%3Aopen+label%3Aenhancement) for ways you can pitch in.

## License

[MIT](./LICENSE)

## Important

See LICENSE for copyright and license terms.  Block.one makes its contribution on a voluntary basis as a member of the ARISEN community and is not responsible for ensuring the overall performance of the software or any related applications.  We make no representation, warranty, guarantee or undertaking in respect of the software or any related documentation, whether expressed or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event shall we be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or documentation or the use or other dealings in the software or documentation. Any test results or performance figures are indicative and will not reflect performance under all conditions.  Any reference to any third party or third-party product, service or other resource is not an endorsement or recommendation by Block.one.  We are not responsible, and disclaim any and all responsibility and liability, for your use of or reliance on any of these resources. Third-party resources may be updated, changed or terminated at any time, so the information here may be out of date or inaccurate.  Any person using or offering this software in connection with providing software, goods or services to third parties shall advise such third parties of these license terms, disclaimers and exclusions of liability.  Block.one, ARISEN, ARISEN Labs, RIX, the heptahedron and associated logos are trademarks of Block.one.

Wallets and related components are complex software that require the highest levels of security.  If incorrectly built or used, they may compromise users’ private keys and digital assets. Wallet applications and related components should undergo thorough security evaluations before being used.  Only experienced developers should work with this software.
