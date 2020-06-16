import { NativeModules } from "react-native";

const { Ed25519JavaBridge } = NativeModules;

export async function generateEd25519Keypair() {
  try {
    const keypairObjectString = await Ed25519JavaBridge.generateEd25519Keypair();
    const keypairObject = JSON.parse(keypairObjectString);
    return keypairObject;
  } catch (e) {
    throw e;
  }
}

export async function sign(privateKeyBase58, message) {
  try {
    const signedHex = await Ed25519JavaBridge.sign(privateKeyBase58, message);
    return signedHex;
  } catch (e) {
    throw e;
  }
}

export async function verify(publicKeyBase58, signatureHex, message) {
  try {
    const verification = await Ed25519JavaBridge.verify(
      publicKeyBase58,
      signatureHex,
      message
    );
    return verification;
  } catch (e) {
    throw e;
  }
}
