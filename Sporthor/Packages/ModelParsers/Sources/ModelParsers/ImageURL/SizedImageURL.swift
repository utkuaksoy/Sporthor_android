//
//  SizedImageURL.swift
//  
//
//  Created by Mesut Canbaz on 11.03.2025.
//

import UIKit

public enum SizedImageURL: Decodable {
    case parameterSizeURL(String)
    case sizedColorHex(String, CGSize)

    public init(from decoder: Decoder) throws {
        self = try SizedImageURL.safeDecode(from: decoder)
    }
}

extension SizedImageURL: SafeDecodable {

    public static func safeDecode(from decoder: Decoder) throws -> SizedImageURL {
        let value = try String(from: decoder)
        var result: SizedImageURL = .parameterSizeURL(value)
#if DEBUG
        result = result.convertForTestIfNeeded()
#endif
        return result
    }

    private func convertForTestIfNeeded() -> Self {
        guard case let .parameterSizeURL(urlString) = self,
              let url = URL(string: urlString),
              let info = url.convertedInfoForTest()
        else { return self }
        return .sizedColorHex(info.colorHex, info.size)
    }
}
