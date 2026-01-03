//
//  AutoLayout+Pinnable.swift
//  
//
//  Created by Mesut Canbaz on 03.04.2025.
//

import UIKit

public protocol Pinnable {

    @discardableResult
    func pin(_ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets) -> NSLayoutConstraint
}

public extension LayoutProxy {

    @discardableResult
    func pin(
        edges: [LayoutEdge] = .all, to item: LayoutProxiable, with insets: UIEdgeInsets = .zero
    ) -> [NSLayoutConstraint] {
        var result: [NSLayoutConstraint] = []

        for edge in edges {
            let constraint = edge.pin(self, item, insets: insets)
            result.append(constraint)
        }

        return result
    }
}
