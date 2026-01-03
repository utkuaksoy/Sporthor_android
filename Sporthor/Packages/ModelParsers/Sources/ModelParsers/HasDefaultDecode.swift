//
//  HasDefaultDecode.swift
//  
//
//  Created by Mesut Canbaz on 11.03.2025.
//

import UIKit

public protocol HasDefaultDecode: SafeDecodable {
    associatedtype DefaultDecodeValueType
    static var defaultDecodeValue: DefaultDecodeValueType { get }
}

public extension HasDefaultDecode where DefaultDecodeValueType: Decodable {

    static func safeDecode(from decoder: Decoder) throws -> DefaultDecodeValueType {
        let result = try? DefaultDecodeValueType(from: decoder)
        return result ?? defaultDecodeValue
    }
}

public extension KeyedDecodingContainer {
    func decode<D: HasDefaultDecode>(
        _ type: SafeDecode<D>.Type, forKey key: Key
    ) throws -> SafeDecode<D> where D.DefaultDecodeValueType: Decodable, D.DefaultDecodeValueType == D {
        return (try decodeIfPresent(type, forKey: key)) ?? .init(wrappedValue: D.defaultDecodeValue)
    }
}

@propertyWrapper
public struct CustomDefaultDecode<D: HasDefaultDecode>: Decodable where D.DefaultDecodeValueType: Decodable {
    public var wrappedValue: D.DefaultDecodeValueType

    public init(_ defaultHolder: D.Type) {
        self.wrappedValue = defaultHolder.defaultDecodeValue
    }

    public init(from decoder: Decoder) throws {
        let result = try? D.DefaultDecodeValueType(from: decoder)
        wrappedValue = result ?? D.defaultDecodeValue
    }
}

extension CustomDefaultDecode: Encodable where D.DefaultDecodeValueType: Encodable {
    public func encode(to encoder: Encoder) throws {
        try wrappedValue.encode(to: encoder)
    }
}

public extension KeyedDecodingContainer {
    func decode<D: HasDefaultDecode>(
        _ type: CustomDefaultDecode<D>.Type, forKey key: Key
    ) throws -> CustomDefaultDecode<D> where D.DefaultDecodeValueType: Decodable {
        return (try decodeIfPresent(type, forKey: key)) ?? .init(D.self)
    }
}

extension CustomDefaultDecode: Equatable where D.DefaultDecodeValueType: Equatable {
    public static func == (lhs: CustomDefaultDecode, rhs: CustomDefaultDecode) -> Bool {
        lhs.wrappedValue == rhs.wrappedValue
    }
}

// NativeTypes

extension String: HasDefaultDecode {
    public static let defaultDecodeValue: String = ""
}

extension Bool: HasDefaultDecode {
    public static let defaultDecodeValue: Bool = false
}

extension Int: HasDefaultDecode {
    public static let defaultDecodeValue: Int = .zero
}

extension Double: HasDefaultDecode {
    public static let defaultDecodeValue: Double = .zero
}

extension CGFloat: HasDefaultDecode {
    public static let defaultDecodeValue: CGFloat = .zero
}

extension UUID: HasDefaultDecode {
    public static var defaultDecodeValue: UUID { .init() }
}

extension CGSize: HasDefaultDecode {
    public static var defaultDecodeValue: CGSize { .zero }
}

extension Dictionary: HasDefaultDecode where Self: Decodable {
    public static var defaultDecodeValue: Dictionary { [:] }
}

public struct DefaultDecodes {
    public struct BoolTrue: HasDefaultDecode {
        public static let defaultDecodeValue: Bool = true
    }

    public struct UUIDString: HasDefaultDecode {
        public static var defaultDecodeValue: String { UUID().uuidString }
    }
}
