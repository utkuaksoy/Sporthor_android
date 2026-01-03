//
//  ImageURL.swift
//  
//
//  Created by Mesut Canbaz on 11.03.2025.
//

import UIKit

public enum ImageURL: Decodable {
    case url(URL)
    case image(UIImage)
    case color(UIColor)
    case colorHex(String)
    case sizedColorHex(String, CGSize)

    public init(from decoder: Decoder) throws {
        self = try ImageURL.safeDecode(from: decoder)
    }
}

extension ImageURL: SafeDecodable {

    public static func safeDecode(from decoder: Decoder) throws -> ImageURL {
        let url = try URL.safeDecode(from: decoder)
        var result: ImageURL = .url(url)
        #if DEBUG
        result = result.convertForTestIfNeeded()
        #endif
        return result
    }

    private func convertForTestIfNeeded() -> Self {
        guard case let .url(url) = self, let info = url.convertedInfoForTest() else { return self }
        return .sizedColorHex(info.colorHex, info.size)
    }
}

public extension URL {
    init(staticString: StaticString) {
        // swiftlint:disable:next force_unwrapping
        self = .init(string: .init(staticString))!
    }
}

extension URL {
    func convertedInfoForTest() -> (colorHex: String, size: CGSize)? {
        guard isInTesting, isDummyImage,
              let sizeStrings = pathComponents.first?.split(separator: "x"),
              let widthString = sizeStrings.first, let heightString = sizeStrings.last,
              let width = Double(widthString), let height = Double(heightString)
        else { return nil }
        let backgroundColor = pathComponents.count > 1 ? pathComponents[1] : "000000"
        return (backgroundColor, .init(width: width, height: height))
    }

    private var isDummyImage: Bool { host == "dummyimage.com" || host == "www.dummyimage.com" }
}

private extension String {
    init(_ staticString: StaticString) {
        self = staticString.withUTF8Buffer {
            String(decoding: $0, as: UTF8.self)
        }
    }
}

private var isInTesting: Bool = { ProcessInfo.processInfo.environment["XCTestConfigurationFilePath"] != nil }()
