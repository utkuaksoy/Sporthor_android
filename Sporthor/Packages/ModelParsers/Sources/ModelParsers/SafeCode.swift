//
//  SafeCode.swift
//  
//
//  Created by Mesut Canbaz on 11.03.2025.
//

import UIKit

@propertyWrapper
public struct SafeCode<T: Codable & SafeDecodable>: Codable where T.SafeDecodeType == T {
    public var wrappedValue: T

    public init(wrappedValue: T) {
        self.wrappedValue = wrappedValue
    }

    public init(from decoder: Decoder) throws {
        wrappedValue = try T.safeDecode(from: decoder)
    }

    public func encode(to encoder: Encoder) throws {
        try wrappedValue.encode(to: encoder)
    }
}

extension SafeCode: Equatable where T.SafeDecodeType: Equatable {}

public extension KeyedDecodingContainer {
    func decode<D: HasDefaultDecode>(
        _ type: SafeCode<D>.Type, forKey key: Key
    ) throws -> SafeCode<D> where D.DefaultDecodeValueType: Decodable, D.DefaultDecodeValueType == D {
        return (try decodeIfPresent(type, forKey: key)) ?? .init(wrappedValue: D.defaultDecodeValue)
    }
}

@propertyWrapper
public struct SafeOptionalCode<T: Codable & SafeDecodable>: Codable where T.SafeDecodeType == T {
    public var wrappedValue: T?

    public init(wrappedValue: T?) {
        self.wrappedValue = wrappedValue
    }

    public init(from decoder: Decoder) throws {
        wrappedValue = try? T.safeDecode(from: decoder)
    }

    public func encode(to encoder: Encoder) throws {
        try? wrappedValue.encode(to: encoder)
    }
}

public extension KeyedDecodingContainer {
    func decode<D: Codable & SafeDecodable>(
        _ type: SafeOptionalCode<D>.Type, forKey key: Key
    ) throws -> SafeOptionalCode<D> where D.SafeDecodeType: Codable, D.SafeDecodeType == D {
        return (try decodeIfPresent(type, forKey: key)) ?? .init(wrappedValue: nil)
    }
}
