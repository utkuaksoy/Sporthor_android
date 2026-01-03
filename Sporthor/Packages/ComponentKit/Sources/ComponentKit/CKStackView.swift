//
//  CKStackView.swift
//  ComponentKit
//
//  Created by derTurke on 1.02.2025.
//

import UIKit

public final class CKStackView: UIStackView {    
    // MARK: - Initializers
    public init(axis: NSLayoutConstraint.Axis = .vertical,
                distribution: UIStackView.Distribution = .fill,
                alignment: UIStackView.Alignment = .fill,
                spacing: CGFloat = 0,
                backgroundColor: UIColor = .clear,
                cornerRadius: CGFloat = 0,
                borderWidth: CGFloat = 0,
                borderColor: UIColor = .clear) {
        super.init(frame: .zero)
        self.axis = axis
        self.distribution = distribution
        self.alignment = alignment
        self.spacing = spacing
        self.backgroundColor = backgroundColor
        self.layer.cornerRadius = cornerRadius
        self.layer.borderWidth = borderWidth
        self.layer.borderColor = borderColor.cgColor
    }
    
    required init(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    public override func addArrangedSubview(_ view: UIView) {
        super.addArrangedSubview(view)
        setNeedsLayout()
    }
    
    public func addArrangedSubviews(_ views: [UIView]) {
        for view in views {
            super.addArrangedSubview(view)
        }
        setNeedsLayout()
    }
    
    public func removeAllArrangedSubviews() {
        for subview in arrangedSubviews {
            removeArrangedSubview(subview)
            subview.removeFromSuperview()
        }
    }
    
    public func setSpacing(_ spacing: CGFloat) {
        self.spacing = spacing
    }
    
    public func setAlignment(_ alignment: UIStackView.Alignment) {
        self.alignment = alignment
    }
    
    public func setAxis(_ axis: NSLayoutConstraint.Axis) {
        self.axis = axis
    }
    
    public func setDistribution(_ distribution: UIStackView.Distribution) {
        self.distribution = distribution
    }
}
