//
//  Autolayout.swift
//
//  Created by Mesut Canbaz on 03.04.2025.
//

import UIKit

public protocol LayoutAnchor {

    func constraint(equalTo anchor: Self, constant: CGFloat) -> NSLayoutConstraint
    func constraint(greaterThanOrEqualTo anchor: Self, constant: CGFloat) -> NSLayoutConstraint
    func constraint(lessThanOrEqualTo anchor: Self, constant: CGFloat) -> NSLayoutConstraint
}

public protocol LayoutDimension: LayoutAnchor {

    func constraint(equalToConstant constant: CGFloat) -> NSLayoutConstraint
    func constraint(greaterThanOrEqualToConstant constant: CGFloat) -> NSLayoutConstraint
    func constraint(lessThanOrEqualToConstant constant: CGFloat) -> NSLayoutConstraint

    func constraint(equalTo anchor: Self, multiplier: CGFloat) -> NSLayoutConstraint
}

extension NSLayoutAnchor: LayoutAnchor {}
extension NSLayoutDimension: LayoutDimension {}

public class LayoutProperty<Anchor: LayoutAnchor> {

    fileprivate let anchor: Anchor

    public init(anchor: Anchor) {
        self.anchor = anchor
    }
}

public class LayoutAttribute<Dimension: LayoutDimension>: LayoutProperty<Dimension> {

    fileprivate let dimension: Dimension

    public init(dimension: Dimension) {
        self.dimension = dimension

        super.init(anchor: dimension)
    }
}

public protocol LayoutProxiable {
    var leadingAnchor: NSLayoutXAxisAnchor { get }
    var leftAnchor: NSLayoutXAxisAnchor { get }
    var trailingAnchor: NSLayoutXAxisAnchor { get }
    var rightAnchor: NSLayoutXAxisAnchor { get }
    var topAnchor: NSLayoutYAxisAnchor { get }
    var bottomAnchor: NSLayoutYAxisAnchor { get }
    var firstBaselineAnchor: NSLayoutYAxisAnchor { get }
    var widthAnchor: NSLayoutDimension { get }
    var heightAnchor: NSLayoutDimension { get }
    var centerXAnchor: NSLayoutXAxisAnchor { get }
    var centerYAnchor: NSLayoutYAxisAnchor { get }
}

extension UIView: LayoutProxiable {}
extension UILayoutGuide: LayoutProxiable {
    public var firstBaselineAnchor: NSLayoutYAxisAnchor { topAnchor }
}

private extension NSLayoutXAxisAnchor {
    var proxy: LayoutProperty<NSLayoutXAxisAnchor> { .init(anchor: self) }
}

private extension NSLayoutYAxisAnchor {
    var proxy: LayoutProperty<NSLayoutYAxisAnchor> { .init(anchor: self) }
}

private extension NSLayoutDimension {
    var proxy: LayoutAttribute<NSLayoutDimension> { .init(dimension: self) }
}

public final class LayoutProxy {

    public lazy var leading = base.leadingAnchor.proxy
    public lazy var left = base.leftAnchor.proxy
    public lazy var trailing = base.trailingAnchor.proxy
    public lazy var right = base.rightAnchor.proxy
    public lazy var top = base.topAnchor.proxy
    public lazy var bottom = base.bottomAnchor.proxy
    public lazy var firstBaseline = base.firstBaselineAnchor.proxy
    public lazy var centerX = base.centerXAnchor.proxy
    public lazy var centerY = base.centerYAnchor.proxy
    public lazy var width = base.widthAnchor.proxy
    public lazy var height = base.heightAnchor.proxy

    private let base: LayoutProxiable

    fileprivate init(base: LayoutProxiable) {
        self.base = base
    }
}

public extension LayoutAttribute {

    @discardableResult
    func equal(to constant: CGFloat, priority: UILayoutPriority? = nil, isActive: Bool = true) -> NSLayoutConstraint {
        let constraint = dimension.constraint(equalToConstant: constant)
        return constraint.activate(isActive, with: priority)
    }

    @discardableResult
    func greaterThanOrEqual(to constant: CGFloat, priority: UILayoutPriority? = nil,
                            isActive: Bool = true) -> NSLayoutConstraint {
        let constraint = dimension.constraint(greaterThanOrEqualToConstant: constant)
        return constraint.activate(isActive, with: priority)
    }

    @discardableResult
    func lessThanOrEqual(to constant: CGFloat, priority: UILayoutPriority? = nil,
                         isActive: Bool = true) -> NSLayoutConstraint {
        let constraint = dimension.constraint(lessThanOrEqualToConstant: constant)
        return constraint.activate(isActive, with: priority)
    }

    @discardableResult
    func equal(to otherDimension: Dimension, multiplier: CGFloat,
               priority: UILayoutPriority? = nil, isActive: Bool = true) -> NSLayoutConstraint {
        let constraint = dimension.constraint(equalTo: otherDimension, multiplier: multiplier)
        return constraint.activate(isActive, with: priority)
    }
}

public extension LayoutProperty {

    @discardableResult
    func equal(
        to otherAnchor: Anchor,
        offsetBy constant: CGFloat = 0,
        priority: UILayoutPriority? = nil,
        multiplier: CGFloat? = nil,
        isActive: Bool = true
    ) -> NSLayoutConstraint {
            var constraint = anchor.constraint(equalTo: otherAnchor, constant: constant)

            if let multiplier = multiplier {
                constraint = constraint.constraintWithMultiplier(multiplier)
            }

            return constraint.activate(isActive, with: priority)
        }

    @discardableResult
    func greaterThanOrEqual(to otherAnchor: Anchor, offsetBy constant: CGFloat = 0,
                            priority: UILayoutPriority? = nil, isActive: Bool = true) -> NSLayoutConstraint {
        let constraint = anchor.constraint(greaterThanOrEqualTo: otherAnchor, constant: constant)
        return constraint.activate(isActive, with: priority)
    }

    @discardableResult
    func lessThanOrEqual(to otherAnchor: Anchor, offsetBy constant: CGFloat = 0,
                         priority: UILayoutPriority? = nil, isActive: Bool = true) -> NSLayoutConstraint {
        let constraint = anchor.constraint(lessThanOrEqualTo: otherAnchor, constant: constant)
        return constraint.activate(isActive, with: priority)
    }
}

public extension LayoutProxiable where Self: UIView {

    func layout(using closure: (LayoutProxy) -> Void) {
        translatesAutoresizingMaskIntoConstraints = false
        closure(LayoutProxy(base: self))
    }

    func layout(
        in superview: UIView,
        with insets: UIEdgeInsets = .zero,
        usingSafeArea: Bool = false
    ) {
        superview.addSubview(self)
        if usingSafeArea {
            pin(to: superview.safeAreaLayoutGuide, with: insets)
        } else {
            pin(to: superview, with: insets)
        }
    }

    @available(*, deprecated, message: "Please use pin(edges:to:with:) instead")
    func pinEdges(to view: UIView, with insets: UIEdgeInsets = .zero, usingSafeArea: Bool = false) {
        if usingSafeArea {
            pin(to: view.safeAreaLayoutGuide, with: insets)
        } else {
            pin(to: view, with: insets)
        }
    }

    func addSubview(_ view: UIView, using closure: (LayoutProxy) -> Void) {
        view.translatesAutoresizingMaskIntoConstraints = false
        addSubview(view)
        closure(LayoutProxy(base: view))
    }

    func pin(edges: [LayoutEdge] = .all, to item: LayoutProxiable, with insets: UIEdgeInsets = .zero) {
        layout { proxy in
            proxy.pin(edges: edges, to: item, with: insets)
        }
    }
}

public func +<A: LayoutAnchor>(lhs: A, rhs: CGFloat) -> (A, CGFloat) {
    return (lhs, rhs)
}

public func -<A: LayoutAnchor>(lhs: A, rhs: CGFloat) -> (A, CGFloat) {
    return (lhs, -rhs)
}

@discardableResult
public func ==<A: LayoutAnchor>(lhs: LayoutProperty<A>, rhs: (A, CGFloat)) -> NSLayoutConstraint {
    return lhs.equal(to: rhs.0, offsetBy: rhs.1)
}

@discardableResult
public func ==<A: LayoutAnchor>(lhs: LayoutProperty<A>, rhs: ((A, CGFloat), UILayoutPriority)) -> NSLayoutConstraint {
    return lhs.equal(to: rhs.0.0, offsetBy: rhs.0.1, priority: rhs.1)
}

@discardableResult
public func ==<A: LayoutAnchor>(lhs: LayoutProperty<A>, rhs: (A, UILayoutPriority)) -> NSLayoutConstraint {
    return lhs.equal(to: rhs.0, priority: rhs.1)
}

@discardableResult
public func ==<A: LayoutAnchor>(lhs: LayoutProperty<A>, rhs: A) -> NSLayoutConstraint {
    return lhs.equal(to: rhs)
}

@discardableResult
public func >=<A: LayoutAnchor>(lhs: LayoutProperty<A>, rhs: (A, CGFloat)) -> NSLayoutConstraint {
    return lhs.greaterThanOrEqual(to: rhs.0, offsetBy: rhs.1)
}

@discardableResult
public func >=<A: LayoutAnchor>(lhs: LayoutProperty<A>, rhs: A) -> NSLayoutConstraint {
    return lhs.greaterThanOrEqual(to: rhs)
}

@discardableResult
public func >=<A: LayoutAnchor>(
    lhs: LayoutProperty<A>, rhs: (A, CGFloat, UILayoutPriority)
) -> NSLayoutConstraint {
    return lhs.greaterThanOrEqual(to: rhs.0, offsetBy: rhs.1, priority: rhs.2)
}

@discardableResult
public func <=<A: LayoutAnchor>(lhs: LayoutProperty<A>, rhs: (A, CGFloat)) -> NSLayoutConstraint {
    return lhs.lessThanOrEqual(to: rhs.0, offsetBy: rhs.1)
}

@discardableResult
public func <=<A: LayoutAnchor>(lhs: LayoutProperty<A>, rhs: A) -> NSLayoutConstraint {
    return lhs.lessThanOrEqual(to: rhs)
}

@discardableResult
public func <=<D: LayoutDimension>(lhs: LayoutAttribute<D>, rhs: CGFloat) -> NSLayoutConstraint {
    return lhs.lessThanOrEqual(to: rhs)
}

@discardableResult
public func ==<D: LayoutDimension>(lhs: LayoutAttribute<D>, rhs: CGFloat) -> NSLayoutConstraint {
    return lhs.equal(to: rhs)
}

@discardableResult
public func ==<D: LayoutDimension>(lhs: LayoutAttribute<D>, rhs: (CGFloat, UILayoutPriority)) -> NSLayoutConstraint {
    return lhs.equal(to: rhs.0, priority: rhs.1)
}

@discardableResult
public func ==<D: LayoutDimension>(lhs: LayoutAttribute<D>, rhs: LayoutAttribute<D>) -> NSLayoutConstraint {
    return lhs.equal(to: rhs.dimension)
}

@discardableResult
public func *=<D: LayoutDimension>(lhs: LayoutAttribute<D>,
                                   rhs: (LayoutAttribute<D>, CGFloat, UILayoutPriority)) -> NSLayoutConstraint {
    return lhs.equal(to: rhs.0.dimension, multiplier: rhs.1, priority: rhs.2)
}

@discardableResult
public func *=<D: LayoutDimension>(lhs: LayoutAttribute<D>,
                                   rhs: (D, CGFloat)) -> NSLayoutConstraint {
    return lhs.equal(to: rhs.0, multiplier: rhs.1)
}

@discardableResult
public func *=<D: LayoutDimension>(lhs: LayoutAttribute<D>,
                                   rhs: (D, CGFloat, UILayoutPriority)) -> NSLayoutConstraint {
    return lhs.equal(to: rhs.0, multiplier: rhs.1, priority: rhs.2)
}

@discardableResult
public func >=<D: LayoutDimension>(lhs: LayoutAttribute<D>, rhs: CGFloat) -> NSLayoutConstraint {
    return lhs.greaterThanOrEqual(to: rhs)
}

@discardableResult
public func >=<D: LayoutDimension>(lhs: LayoutAttribute<D>, rhs: (CGFloat, UILayoutPriority)) -> NSLayoutConstraint {
    return lhs.greaterThanOrEqual(to: rhs.0, priority: rhs.1)
}

public extension Collection where Element == NSLayoutConstraint {
    func disableAll() {
        self.forEach { $0.isActive = false }
    }
}

private extension NSLayoutConstraint {

    func activate(_ isActive: Bool, with priority: UILayoutPriority?) -> NSLayoutConstraint {
        if let priority = priority {
            self.priority = priority
        }
        self.isActive = isActive
        return self
    }
}
