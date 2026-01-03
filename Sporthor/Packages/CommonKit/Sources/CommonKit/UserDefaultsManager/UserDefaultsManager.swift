//
//  UserDefaultsManager.swift
//  Sporthor
//
//  Created by derTurke on 9.03.2025.
//

import Foundation

public final class UserDefaultsManager: Sendable {
    public static let shared = UserDefaultsManager()
    
    private init() {}
    
    public func set<T: Encodable>(_ value: T, forKey key: String) {
        if let encoded = try? JSONEncoder().encode(value) {
            UserDefaults.standard.set(encoded, forKey: key)
        }
    }
    
    public func setString(_ value: String, forKey key: String) {
        UserDefaults.standard.set(value, forKey: key)
    }
    
    public func setBool(_ value: Bool, forKey key: String) {
        UserDefaults.standard.set(value, forKey: key)
    }
    
    public func setInt(_ value: Int, forKey key: String) {
        UserDefaults.standard.set(value, forKey: key)
    }
    
    public func setDouble(_ value: Double, forKey key: String) {
        UserDefaults.standard.set(value, forKey: key)
    }
    
    public func get<T: Decodable>(_ type: T.Type, forKey key: String) -> T? {
        if let data = UserDefaults.standard.data(forKey: key),
           let decodedValue = try? JSONDecoder().decode(T.self, from: data) {
            return decodedValue
        }
        return nil
    }
    
    public func getString(forKey key: String) -> String? {
        return UserDefaults.standard.string(forKey: key)
    }
    
    public func getBool(forKey key: String) -> Bool? {
        return UserDefaults.standard.bool(forKey: key)
    }
    
    public func getInt(forKey key: String) -> Int? {
        return UserDefaults.standard.integer(forKey: key)
    }
    
    public func getDouble(forKey key: String) -> Double? {
        return UserDefaults.standard.double(forKey: key)
    }
    
    public func removeObject(forKey key: String) {
        UserDefaults.standard.removeObject(forKey: key)
    }
    
    public func removeAllObject() {
        if let appDomain = Bundle.main.bundleIdentifier {
            UserDefaults.standard.removePersistentDomain(forName: appDomain)
            UserDefaults.standard.synchronize()
        }
    }
    
    public func removeAllObject(except keysToKeep: [String] = []) {
        let defaults = UserDefaults.standard
        let allKeys = defaults.dictionaryRepresentation().keys
        
        for key in allKeys {
            if !keysToKeep.contains(key) {
                defaults.removeObject(forKey: key)
            }
        }
        
        defaults.synchronize()
    }
}
