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

    @ReactMethod
    public void verify(String publicKeyBase58, String signatureHex, String message, Promise promise) {
        try {
            byte[] publicKeyBytes = Base58.decode(publicKeyBase58);
            byte[] messageBytes = lazySodium.bytes(message);
            byte[] signatureBytes = hexStringToByteArray(signatureHex);

            boolean verification = lazySodium.cryptoSignVerifyDetached(signatureBytes, messageBytes, messageBytes.length, publicKeyBytes);
        
            promise.resolve(verification);
        } catch (RuntimeException e) {
            promise.reject(e.getMessage());
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }
}
