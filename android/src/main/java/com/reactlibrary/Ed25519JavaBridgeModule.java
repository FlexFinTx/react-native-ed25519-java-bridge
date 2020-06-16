package com.ed25519.bridge;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import com.goterl.lazycode.lazysodium.LazySodiumAndroid;
import com.goterl.lazycode.lazysodium.SodiumAndroid;
import com.goterl.lazycode.lazysodium.exceptions.SodiumException;
import com.goterl.lazycode.lazysodium.interfaces.*;
import com.goterl.lazycode.lazysodium.utils.Key;
import com.goterl.lazycode.lazysodium.utils.KeyPair;

import org.json.JSONObject;
import org.json.JSONException;

public class Ed25519JavaBridgeModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    private final LazySodiumAndroid lazySodium;

    public Ed25519JavaBridgeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.lazySodium = new LazySodiumAndroid(new SodiumAndroid());
    }

    @Override
    public String getName() {
        return "Ed25519JavaBridge";
    }

    @ReactMethod
    public void generateEd25519Keypair(Promise promise) {
        try {
            JSONObject keypair = new JSONObject();

            KeyPair kp = lazySodium.cryptoSignKeypair();
            Key pk = kp.getPublicKey();
            Key sk = kp.getSecretKey();

            String pkBase58 = Base58.encode(pk.getAsBytes());
            String skBase58 = Base58.encode(sk.getAsBytes());

            keypair.put("publicKeyBase58", pkBase58);
            keypair.put("privateKeyBase58", skBase58);
            keypair.put("type", "Ed25519VerificationKey2019");

            promise.resolve(keypair.toString());
        } catch (SodiumException | JSONException e) {
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void sign(String privateKeyBase58, String message, Promise promise) {
        try {
            byte[] privateKeyBytes = Base58.decode(privateKeyBase58);
            byte[] messageBytes = lazySodium.bytes(message);
            byte[] signatureBytes = new byte[Sign.BYTES];

            lazySodium.cryptoSignDetached(signatureBytes, messageBytes, messageBytes.length, privateKeyBytes);

            promise.resolve(lazySodium.toHex(signatureBytes));
        } catch (RuntimeException e) {
            promise.reject(e.getMessage());
        }
    }
}
