//
//  NSLayoutConstraint+Multiplier.swift
//  
//
//  Created by Mesut Canbaz on 03.04.2025.
//

import UIKit

public extension NSLayoutConstraint {

    func constraintWithMultiplier(_ multiplier: CGFloat) -> NSLayoutConstraint {
        return NSLayoutConstraint(
            item: self.firstItem as Any,
            attribute: self.firstAttribute,
            relatedBy: self.relation,
            toItem: self.secondItem,
            attribute: self.secondAttribute,
            multiplier: multiplier,
            constant: self.constant
        )
    }

    func multipliered(_ multiplier: CGFloat) -> NSLayoutConstraint {
        NSLayoutConstraint.deactivate([self])

        let constraint = constraintWithMultiplier(multiplier)

        constraint.priority = priority
        constraint.shouldBeArchived = shouldBeArchived
        constraint.identifier = identifier

        NSLayoutConstraint.activate([constraint])
        return constraint
    }
}
