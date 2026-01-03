//
//  KeychainManager.swift
//  UserKit
//
//  Created by derTurke on 9.03.2025.
//

import Security
import Foundation

public final class KeychainManager: Sendable {
    public static let shared = KeychainManager()
    
    private init() {}

    public func set<T: Encodable>(_ value: T, forKey key: String) {
        if let encodedData = try? JSONEncoder().encode(value) {
            saveToKeychain(encodedData, forKey: key)
        }
    }

    public func setString(_ value: String, forKey key: String) {
        if let data = value.data(using: .utf8) {
            saveToKeychain(data, forKey: key)
        }
    }

    public func setBool(_ value: Bool, forKey key: String) {
        let data = Data([value ? 1 : 0])
        saveToKeychain(data, forKey: key)
    }

    public func setInt(_ value: Int, forKey key: String) {
        let data = withUnsafeBytes(of: value) { Data($0) }
        saveToKeychain(data, forKey: key)
    }

    public func setDouble(_ value: Double, forKey key: String) {
        let data = withUnsafeBytes(of: value) { Data($0) }
        saveToKeychain(data, forKey: key)
    }

    public func get<T: Decodable>(_ type: T.Type, forKey key: String) -> T? {
        guard let data = getFromKeychain(forKey: key) else { return nil }
        return try? JSONDecoder().decode(T.self, from: data)
    }

    public func getString(forKey key: String) -> String? {
        guard let data = getFromKeychain(forKey: key) else { return nil }
        return String(data: data, encoding: .utf8)
    }

    public func getBool(forKey key: String) -> Bool? {
        guard let data = getFromKeychain(forKey: key) else { return nil }
        return data.first == 1
    }

    public func getInt(forKey key: String) -> Int? {
        guard let data = getFromKeychain(forKey: key) else { return nil }
        return data.withUnsafeBytes { $0.load(as: Int.self) }
    }

    public func getDouble(forKey key: String) -> Double? {
        guard let data = getFromKeychain(forKey: key) else { return nil }
        return data.withUnsafeBytes { $0.load(as: Double.self) }
    }

    public func removeObject(forKey key: String) {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: key
        ]
        SecItemDelete(query as CFDictionary)
    }

    public func clearAll() {
        let query: [String: Any] = [kSecClass as String: kSecClassGenericPassword]
        SecItemDelete(query as CFDictionary)
    }
    
    
    private func saveToKeychain(_ data: Data, forKey key: String) {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: key,
            kSecValueData as String: data,
            kSecAttrAccessible as String: kSecAttrAccessibleAfterFirstUnlock
        ]
        
        SecItemDelete(query as CFDictionary)
        SecItemAdd(query as CFDictionary, nil)
    }

    private func getFromKeychain(forKey key: String) -> Data? {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: key,
            kSecReturnData as String: kCFBooleanTrue!,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]
        
        var dataTypeRef: AnyObject?
        let status = SecItemCopyMatching(query as CFDictionary, &dataTypeRef)
        
        if status == errSecSuccess {
            return dataTypeRef as? Data
        }
        return nil
    }
}
