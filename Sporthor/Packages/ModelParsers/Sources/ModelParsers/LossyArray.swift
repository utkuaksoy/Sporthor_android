//
//  LossyArray.swift
//  HepsiX
//
//  Created by Mesut Canbaz on 11.03.2025.
//

import Foundation

@propertyWrapper
public struct LossyArray<T: Decodable>: Decodable, Sendable {
    public typealias Element = T
    private struct AnyDecodableValue: Codable {}

    private struct LossyDecodableValue: Decodable {
        let value: T?

        public init(from decoder: Decoder) throws {
            guard let container = try? decoder.singleValueContainer() else {
                value = nil
                return
            }

            do {
                let result: T = try container.decode(T.self)
                value = result
            } catch {
                LossyDecodableValue.log(error)
                _ = try? container.decode(AnyDecodableValue.self)
                value = nil
            }
        }

        static func log(_ error: Error) {
            // TODO: - Add event
            debugPrint("\(error)")
        }
    }

    public var wrappedValue: [T]

    public init(wrappedValue: [T]) {
        self.wrappedValue = wrappedValue
    }

    public init(from decoder: Decoder) throws {
        var elements: [T] = []
        if var container = try? decoder.unkeyedContainer() {
            while !container.isAtEnd {
                let decodedItem = try? container.decode(LossyDecodableValue.self)
                if let value = decodedItem?.value {
                    elements.append(value)
                }
            }
        }
        self.wrappedValue = elements
    }
}

extension LossyArray: Equatable where Self.Element: Equatable {}
extension LossyArray: Encodable where Self.Element: Encodable {

    public func encode(to encoder: Encoder) throws {
        try wrappedValue.encode(to: encoder)
    }
}

extension LossyArray: Hashable where T: Hashable {
    public static func == (lhs: LossyArray<T>, rhs: LossyArray<T>) -> Bool {
        lhs.wrappedValue == rhs.wrappedValue
    }

    public func hash(into hasher: inout Hasher) {
        hasher.combine(wrappedValue)
    }
}

public extension KeyedDecodingContainer {
    func decode<T: Decodable>(_ type: LossyArray<T>.Type, forKey key: Key) throws -> LossyArray<T> {
        return try decodeIfPresent(type, forKey: key) ?? .init(wrappedValue: [])
    }
}
