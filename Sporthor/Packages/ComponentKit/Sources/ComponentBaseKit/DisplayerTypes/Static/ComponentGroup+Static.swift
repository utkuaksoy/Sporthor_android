//
//  ComponentGroup+Static.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import UIKit

public protocol StaticComponentGroup: ComponentGroup {

    static var sources: [String: any StaticComponent.Type] { get }
}

public extension StaticComponentGroup {

    static func item(for kind: String, with decoder: Decoder) throws -> (any StaticComponent)? {
        guard let contentType = sources[kind] else { return nil }
        return try contentType.init(from: decoder)
    }
}
