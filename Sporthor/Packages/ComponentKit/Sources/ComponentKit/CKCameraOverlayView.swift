//
//  CKCameraOverlayView.swift
//  ComponentKit
//
//  Created by derTurke on 10.05.2025.
//

import UIKit

public final class CKCameraOverlayView: UIView {
    private let transparentRect: CGRect

    public init(frame: CGRect, transparentRect: CGRect) {
        self.transparentRect = transparentRect
        super.init(frame: frame)
        backgroundColor = UIColor.clear
        isUserInteractionEnabled = false
    }

    public required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    public override func draw(_ rect: CGRect) {
        guard let context = UIGraphicsGetCurrentContext() else { return }
        context.setFillColor(UIColor.black.withAlphaComponent(0.6).cgColor)
        context.fill(bounds)
        context.clear(transparentRect)
        context.setStrokeColor(UIColor.white.cgColor)
        context.setLineWidth(2)
        context.stroke(transparentRect)
    }
}
