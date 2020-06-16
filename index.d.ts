export interface IEd25519Keypair {
  publicKeyBase58: string;
  privateKeyBase58: string;
  type: string;
}

/**
 * @function generateEd25519Keypair()
 * @returns IEd25519Keypair
 */
export function generateEd25519Keypair(): Promise<IEd25519Keypair>;

/**
 * @function sign
 * @param privateKeyBase58
 * @param message
 * @returns hex encoded signed string
 */
export function sign(
  privateKeyBase58: string,
  message: string
): Promise<string>;
