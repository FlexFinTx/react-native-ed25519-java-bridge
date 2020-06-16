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
