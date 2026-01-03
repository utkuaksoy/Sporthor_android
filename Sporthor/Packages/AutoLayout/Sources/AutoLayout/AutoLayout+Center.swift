//
//  AutoLayout+Center.swift
//  
//
//  Created by Mesut Canbaz on 03.04.2025.
//

import UIKit

public extension LayoutProxy {

    @discardableResult
    func center(
        to item: LayoutProxiable, with offset: UIOffset = .zero, priority: UILayoutPriority? = nil
    ) -> [NSLayoutConstraint] {
        let firstConstraint = self.centerX.equal(
            to: item.centerXAnchor, offsetBy: offset.horizontal, priority: priority
        )
        let secondConstraint = self.centerY.equal(
            to: item.centerYAnchor, offsetBy: offset.vertical, priority: priority
        )
        return [firstConstraint, secondConstraint]
    }
}
