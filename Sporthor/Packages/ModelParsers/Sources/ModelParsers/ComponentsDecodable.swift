//
//  ComponentsDecodable.swift
//  
//
//  Created by Mesut Canbaz on 11.03.2025.
//

import Foundation

public protocol ComponentDecoderKeyType: CodingKey {
    static var kindKey: Self { get }
}

public protocol ComponentItemGeneratorFromKind {
    associatedtype Result
    static func item(for kind: String, with decoder: Decoder) throws -> Result?
}

@propertyWrapper
public struct ComponentsDecodable<G: ComponentItemGeneratorFromKind, K: ComponentDecoderKeyType>: Decodable {

    public let wrappedValue: [G.Result]

    public init(wrappedValue: [G.Result] = [], generable: G.Type, key: K.Type) {
        self.wrappedValue = wrappedValue
    }

    public init(from decoder: Decoder) throws {
        var container = try decoder.unkeyedContainer()
        var elements: [G.Result] = []

        while !container.isAtEnd {
            let tempDecoding = try container.superDecoder()
            if let item = try? ComponentDecodable<G, K>(from: tempDecoding).wrappedValue {
                elements.append(item)
            }
        }
        self.wrappedValue = elements
    }
}

public extension KeyedDecodingContainer {
    func decode<G: ComponentItemGeneratorFromKind>(_ type: ComponentsDecodable<G, Key>.Type, forKey key: Key) throws -> ComponentsDecodable<G, Key> {
        return try decodeIfPresent(type, forKey: key) ?? .init(wrappedValue: [], generable: G.self, key: Key.self)
    }
}

@propertyWrapper
public struct ComponentDecodable<G: ComponentItemGeneratorFromKind, K: ComponentDecoderKeyType>: Decodable {

    public let wrappedValue: G.Result?

    public init(wrappedValue: G.Result? = nil, generable: G.Type, key: K.Type) {
        self.wrappedValue = wrappedValue
    }

    public init(from decoder: Decoder) throws {
        var result: G.Result?
        if let container = try? decoder.container(keyedBy: K.self),
           let kind = try? container.decode(String.self, forKey: K.kindKey),
           let item = try? G.item(for: kind, with: decoder) {

            result = item
        }
        self.wrappedValue = result
    }
}

public extension KeyedDecodingContainer {
    func decode<G: ComponentItemGeneratorFromKind>(_ type: ComponentDecodable<G, Key>.Type, forKey key: Key) throws -> ComponentDecodable<G, Key> {
        return try decodeIfPresent(type, forKey: key) ?? .init(wrappedValue: nil, generable: G.self, key: Key.self)
    }
}
