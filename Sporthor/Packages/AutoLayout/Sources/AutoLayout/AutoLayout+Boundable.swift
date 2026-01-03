//
//  AutoLayout+Boundable.swift
//  
//
//  Created by Mesut Canbaz on 03.04.2025.
//

import UIKit

public protocol Boundable {

    @discardableResult
    func bound(_ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets) -> NSLayoutConstraint
}

public extension LayoutProxy {

    @discardableResult
    func bound(
        edges: [LayoutEdge] = .all, in item: LayoutProxiable, with insets: UIEdgeInsets = .zero
    ) -> [NSLayoutConstraint] {
        var result: [NSLayoutConstraint] = []

        for edge in edges {
            let constraint = edge.bound(self, item, insets: insets)
            result.append(constraint)
        }

        return result
    }
}
