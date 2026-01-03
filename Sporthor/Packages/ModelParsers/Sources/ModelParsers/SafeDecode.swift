//
//  SafeDecode.swift
//  
//
//  Created by Mesut Canbaz on 11.03.2025.
//

import Foundation

public protocol SafeDecodable {
    associatedtype SafeDecodeType
    static func safeDecode(from decoder: Decoder) throws -> SafeDecodeType
}

@propertyWrapper
public struct SafeDecode<T: SafeDecodable>: Decodable where T.SafeDecodeType == T {
    public var wrappedValue: T

    public init(wrappedValue: T) {
        self.wrappedValue = wrappedValue
    }

    public init(from decoder: Decoder) throws {
        wrappedValue = try T.safeDecode(from: decoder)
    }
}

extension SafeDecode: Equatable where T.SafeDecodeType: Equatable {}
extension SafeDecode: Encodable where T.SafeDecodeType: Encodable {
    public func encode(to encoder: Encoder) throws {
        try wrappedValue.encode(to: encoder)
    }
}
extension SafeDecode: Hashable where T.SafeDecodeType: Hashable {}

@propertyWrapper
public struct SafeOptionalDecode<T: SafeDecodable>: Decodable where T.SafeDecodeType == T {
    public var wrappedValue: T?

    public init(wrappedValue: T?) {
        self.wrappedValue = wrappedValue
    }

    public init(from decoder: Decoder) throws {
        wrappedValue = try? T.safeDecode(from: decoder)
    }
}

extension SafeOptionalDecode: Equatable where T.SafeDecodeType: Equatable {}
extension SafeOptionalDecode: Encodable where T.SafeDecodeType: Encodable {
    public func encode(to encoder: Encoder) throws {
        guard let wrappedValue else { return }
        try wrappedValue.encode(to: encoder)
    }
}
extension SafeOptionalDecode: Hashable where T.SafeDecodeType: Hashable {}

public extension KeyedDecodingContainer {
    func decode<D: SafeDecodable>(
        _ type: SafeOptionalDecode<D>.Type, forKey key: Key
    ) throws -> SafeOptionalDecode<D> where D.SafeDecodeType: Decodable, D.SafeDecodeType == D {
        return (try decodeIfPresent(type, forKey: key)) ?? .init(wrappedValue: nil)
    }
}

// MARK: - URL

extension URL: SafeDecodable {

    public static func safeDecode(from decoder: Decoder) throws -> URL {
        let container = try decoder.singleValueContainer()
        if let value = try? container.decode(String.self), let result = URL(string: value) {
            return result
        } else {
            let result = try container.decode(URL.self)
            return result
        }
    }
}

// MARK: - Dictionary

extension Dictionary: SafeDecodable where Self: Decodable {
    public static func safeDecode(from decoder: Decoder) throws -> Dictionary<Key, Value> {
        return try Self.init(from: decoder)
    }
}
