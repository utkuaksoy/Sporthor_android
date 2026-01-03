//
//  LayoutEdge.swift
//  
//
//  Created by Mesut Canbaz on 03.04.2025.
//

import UIKit

public extension Array where Element == LayoutEdge {
    static let all: [LayoutEdge] = [.leading, .trailing, .top, .bottom]
    static let horizontal: [LayoutEdge] = [.leading, .trailing]
    static let nonDirectionalHorizontal: [LayoutEdge] = [.left, .right]
    static let vertical: [LayoutEdge] = [.top, .bottom]
    static let withoutTop: [LayoutEdge] = [.leading, .trailing, .bottom]
    static let withoutBottom: [LayoutEdge] = [.leading, .trailing, .top]
}

public class LayoutEdge: Pinnable, Boundable {

    public static let leading: LayoutEdge = Leading()
    public static let left: LayoutEdge = Left()
    public static let trailing: LayoutEdge = Trailing()
    public static let right: LayoutEdge = Right()
    public static let top: LayoutEdge = Top()
    public static let bottom: LayoutEdge = Bottom()


    @discardableResult
    public func pin(
        _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
    ) -> NSLayoutConstraint { return NSLayoutConstraint() }

    @discardableResult
    public func bound(
        _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
    ) -> NSLayoutConstraint { return NSLayoutConstraint() }

    private final class Leading: LayoutEdge {

        @discardableResult
        public override func pin(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.leading == rhs.leadingAnchor + insets.left
        }

        @discardableResult
        public override func bound(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.leading >= rhs.leadingAnchor + insets.left
        }
    }

    private final class Left: LayoutEdge {

        @discardableResult
        public override func pin(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.left == rhs.leftAnchor + insets.left
        }

        @discardableResult
        public override func bound(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.left >= rhs.leftAnchor + insets.left
        }
    }

    private final class Trailing: LayoutEdge {

        @discardableResult
        public override func pin(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.trailing == rhs.trailingAnchor - insets.right
        }

        @discardableResult
        public override func bound(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.trailing <= rhs.trailingAnchor - insets.right
        }
    }

    private final class Right: LayoutEdge {

        @discardableResult
        public override func pin(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.right == rhs.rightAnchor - insets.right
        }

        @discardableResult
        public override func bound(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.right <= rhs.rightAnchor - insets.right
        }
    }

    private final class Top: LayoutEdge {

        @discardableResult
        public override func pin(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.top == rhs.topAnchor + insets.top
        }

        @discardableResult
        public override func bound(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.top >= rhs.topAnchor + insets.top
        }
    }

    private final class Bottom: LayoutEdge {

        @discardableResult
        public override func pin(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.bottom == rhs.bottomAnchor - insets.bottom
        }

        @discardableResult
        public override func bound(
            _ lhs: LayoutProxy, _ rhs: LayoutProxiable, insets: UIEdgeInsets
        ) -> NSLayoutConstraint {
            lhs.bottom <= rhs.bottomAnchor - insets.bottom
        }
    }
}
