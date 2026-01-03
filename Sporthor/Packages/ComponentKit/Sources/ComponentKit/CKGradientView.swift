//
//  CKGradientView.swift
//  ComponentKit
//
//  Created by derTurke on 17.02.2025.
//

import UIKit
import DesignKit

public final class CKGradientView: UIView {
    
    private let gradientLayer = CAGradientLayer()

    public init(colors: [UIColor],
                startPoint: CGPoint = CGPoint(x: 0.5, y: 0),
                endPoint: CGPoint = CGPoint(x: 0.5, y: 1.0)) {
        super.init(frame: .zero)
        setupGradient(colors: colors,
                      startPoint: startPoint,
                      endPoint: endPoint)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupGradient(colors: [ColorName.successLighter100.color, .white],
                      startPoint: .zero,
                      endPoint: .init(x: 1, y: 0))
    }

    private func setupGradient(colors: [UIColor], startPoint: CGPoint, endPoint: CGPoint) {
        gradientLayer.colors = colors.map { $0.cgColor }
        gradientLayer.startPoint = startPoint
        gradientLayer.endPoint = endPoint
        gradientLayer.cornerRadius = layer.cornerRadius
        layer.insertSublayer(gradientLayer, at: 0)
    }

    public override func layoutSubviews() {
        super.layoutSubviews()
        gradientLayer.frame = bounds
    }
}
