//
//  Extension+UIView.swift
//  ComponentKit
//
//  Created by derTurke on 1.02.2025.
//

import UIKit

public extension UIView {
    func setCornerRadius(_ radius: CGFloat) {
        self.layer.cornerRadius = radius
    }
    
    func setBorderWidth(_ borderWidth: CGFloat) {
        self.layer.borderWidth = borderWidth
    }
    
    @objc func setBorderColor(_ color: UIColor) {
        self.layer.borderColor = color.cgColor
    }
}
