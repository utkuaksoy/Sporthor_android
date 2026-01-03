//
//  Extension+Encodable.swift
//  NetworkKit
//
//  Created by derTurke on 9.03.2025.
//

import Foundation

public extension Encodable {
    func dictionary() -> [String: Any]? {
        do {
            let encodedData = try JSONEncoder().encode(self)
            let jsonObject = try JSONSerialization.jsonObject(with: encodedData, options: .fragmentsAllowed)
            return jsonObject as? [String: Any]
        } catch {
            return nil
        }
    }
}

public extension Encodable {
    func asDictionary() throws -> [String: Any] {
        let data = try JSONEncoder().encode(self)
        guard let dictionary = try JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [String: Any] else {
            throw NSError()
        }
        return dictionary
    }
    
    func asArrayDictionary() throws -> [[String: Any]] {
        let data = try JSONEncoder().encode(self)
        guard let arrayDictionary = try JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [[String: Any]] else {
            throw NSError()
        }
        return arrayDictionary
    }
    
    func toJSONString() throws -> String {
        let data = try JSONEncoder().encode(self)
        guard let jsonString = String(data: data, encoding: .utf8) else {
            throw NSError()
        }
        return jsonString
    }
    
    var asData: Data? {
        return try? JSONEncoder().encode(self)
    }
    
    var asDictionaryForQueryString: [String: String]? {
        guard let data = try? JSONEncoder().encode(self) else {
            return nil
        }
        return (try? JSONSerialization.jsonObject(with: data, options: .allowFragments)).flatMap { $0 as? [String: String] }
    }
}
