import CryptoKit
import Foundation

@objc public class CryptoKitAes: NSObject {

    @objc public static func generateKey() -> Data {
        let key = SymmetricKey(size: .bits256)
        return key.withUnsafeBytes {
            return Data(Array($0))
        }
    }

    @objc public static func encryptGCM(
        key: NSData,
        plaintext: NSData,
        iv: NSData,
    ) throws -> [Data] {
        let result = try AES.GCM.seal(
            plaintext as Data,
            using: SymmetricKey(data: key as Data),
            nonce: try AES.GCM.Nonce(data: iv as Data)
        )
        return [result.ciphertext, result.tag]
    }

    @objc public static func decryptGCM(
        key: NSData,
        ciphertext: NSData,
        iv: NSData,
        tag: NSData
    ) throws -> Data {
        return try AES.GCM.open(
            try AES.GCM.SealedBox(
                nonce: try AES.GCM.Nonce(data: iv),
                ciphertext: ciphertext,
                tag: tag
            ),
            using: SymmetricKey(data: key as Data)
        )
    }
}
